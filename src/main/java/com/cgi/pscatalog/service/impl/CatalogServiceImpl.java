package com.cgi.pscatalog.service.impl;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cgi.pscatalog.config.Constants;
import com.cgi.pscatalog.security.SecurityUtils;
import com.cgi.pscatalog.service.AddressesService;
import com.cgi.pscatalog.service.CatalogService;
import com.cgi.pscatalog.service.CustomersService;
import com.cgi.pscatalog.service.OrderDetService;
import com.cgi.pscatalog.service.OrderStatusService;
import com.cgi.pscatalog.service.OrdersService;
import com.cgi.pscatalog.service.ProductsService;
import com.cgi.pscatalog.service.dto.AddressesDTO;
import com.cgi.pscatalog.service.dto.CustomersDTO;
import com.cgi.pscatalog.service.dto.OrderDetDTO;
import com.cgi.pscatalog.service.dto.OrderStatusDTO;
import com.cgi.pscatalog.service.dto.OrdersDTO;
import com.cgi.pscatalog.service.dto.ProductsDTO;
import com.cgi.pscatalog.web.rest.errors.BadRequestAlertException;
import com.cgi.pscatalog.web.rest.errors.FirstCreateAddressException;
import com.cgi.pscatalog.web.rest.errors.InsufficientProductQuantityException;

/**
 * Service Implementation for managing OrderDet.
 */
@Service
@Transactional(rollbackFor=Exception.class)
public class CatalogServiceImpl implements CatalogService {

    private final Logger log = LoggerFactory.getLogger(CatalogServiceImpl.class);

    @Autowired
    private OrdersService ordersService;

    @Autowired
    private OrderDetService orderDetService;

    @Autowired
    private AddressesService addressesService;

    @Autowired
    private CustomersService customersService;

    @Autowired
    private OrderStatusService orderStatusService;

    @Autowired
    private ProductsService productsService;

    @Override
	public void addBasket(Long productId, Integer orderQuantity, BigDecimal productPrice, String entityName) {
    	// Get login
    	String login = SecurityUtils.getCurrentUserLogin().get();

    	// Get available product quantity
    	Optional<ProductsDTO> optionalProductsDTO = productsService.findOne(productId);

        Integer productQuantity = new Integer(0);

    	if ( optionalProductsDTO.isPresent() ) {
    		ProductsDTO productsDTO = optionalProductsDTO.get();

    		productQuantity = productsDTO.getProductQuantity();
    	}

    	// 1 - Verify if there is any PENDING order created for customer
        Page<OrdersDTO> pageOrdersDTO = ordersService.getAllByLoginAndOrderStatusPending(login, PageRequest.of(0, 1));

        List<OrdersDTO> listOrdersDTO = pageOrdersDTO.getContent();

        Long orderId = new Long(0);

        Integer newOrderQuantity = new Integer(0);

        for (Iterator<OrdersDTO> iterator = listOrdersDTO.iterator(); iterator.hasNext();) {
        	OrdersDTO oldOrdersDTO = iterator.next();

        	orderId = oldOrdersDTO.getId();

            // 2 - Verify if the product already exists in Order Detail (get order detail by Order and Product identification)
        	Optional<OrderDetDTO> optionalOrderDetDTO = orderDetService.getOrderDetByOrderIdAndProductId(orderId, productId);

        	if ( optionalOrderDetDTO.isPresent() ) {
        		OrderDetDTO orderDetDTO = optionalOrderDetDTO.get();

        		// Validate product quantity
        		newOrderQuantity = orderQuantity + orderDetDTO.getOrderQuantity();

        		if ( newOrderQuantity.intValue() > productQuantity.intValue()) {
        			throw new InsufficientProductQuantityException(entityName);
        		}

            	// Update quantity in Order Detail
            	orderDetDTO.setOrderQuantity(newOrderQuantity);
                orderDetDTO.setLastModifiedBy(login);
                orderDetDTO.setLastModifiedDate(Instant.now());

                orderDetService.save(orderDetDTO);
        	} else {
        		// Validate product quantity
        		newOrderQuantity = orderQuantity;

        		if ( newOrderQuantity.intValue() > productQuantity.intValue()) {
        			throw new InsufficientProductQuantityException(entityName);
        		}

    	        // Add Order Detail to the Order
            	OrderDetDTO newOrderDetDTO = new OrderDetDTO();
            	newOrderDetDTO.setOrderId(orderId);
            	newOrderDetDTO.setOrderQuantity(newOrderQuantity);
            	newOrderDetDTO.setUnitPrice(productPrice);
            	newOrderDetDTO.setProductId(productId);
            	newOrderDetDTO.setCreatedBy(login);
            	newOrderDetDTO.setCreatedDate(Instant.now());

            	orderDetService.save(newOrderDetDTO);
    	        // End Create Order Detail
        	}

        	break;
        }

        if ( orderId.longValue() == 0 ) {
        	// There is not any PENDING order created for customer
        	//
            // Get customer identification by login
        	Optional<CustomersDTO> optionalCustomerDTO = customersService.getCustomersByLogin(login);

            if ( !optionalCustomerDTO.isPresent() ) {
            	throw new BadRequestAlertException("You must create a customer first", entityName, "idnull");
            }

        	Long customerId = optionalCustomerDTO.get().getId();

        	log.debug("REST request to addBasket - customerId: {}", customerId);

            // Get default address identification by login
            Long addressId = new Long(0);

            Page<AddressesDTO> pageAddressesDTO = addressesService.getAddressesByLogin(login, PageRequest.of(0, 1));
            List<AddressesDTO> listAddressesDTO = pageAddressesDTO.getContent();

            for (Iterator<AddressesDTO> iterator = listAddressesDTO.iterator(); iterator.hasNext();) {
    			AddressesDTO addressesDTO = iterator.next();
    			addressId = addressesDTO.getId();
    			break;
    		}

            if ( addressId.longValue() == 0 ) {
            	throw new FirstCreateAddressException(entityName);
            }

            log.debug("REST request to addBasket - addressId: {}", addressId);

            // Get Order Status Id of PENDING status
            Optional<OrderStatusDTO> optionalOrderStatusDTO = orderStatusService.getOrderStatusByDescription(Constants.ORDER_STATUS_PENDING);

            Long orderStatusId = new Long(0);

            if ( optionalOrderStatusDTO.isPresent() ) {
            	OrderStatusDTO orderStatusDTO = optionalOrderStatusDTO.get();

            	orderStatusId = orderStatusDTO.getId();

                log.debug("REST request to addBasket - orderStatusId: {}", orderStatusId);
            } else {
                throw new BadRequestAlertException("Bad configuration, configure order status first", entityName, "idnull");
            }

	        // Start Create Order
	        OrdersDTO newOrdersDTO = new OrdersDTO();
	        newOrdersDTO.setCustomerId(customerId);
	        newOrdersDTO.setAddressId(addressId);
	        newOrdersDTO.setDeliveryAddressId(addressId);
	        newOrdersDTO.setOrderStatusId(orderStatusId);

	        Instant currentDate = Instant.now();

	        newOrdersDTO.setOrderDate(currentDate);
	        newOrdersDTO.setCreatedBy(login);
	        newOrdersDTO.setCreatedDate(currentDate);
	        newOrdersDTO.setOrderReference("REF_"+ currentDate.getEpochSecond());

	        OrdersDTO newOrdersDTOAux = ordersService.save(newOrdersDTO);
	        // End Create Order

	        // Validate product quantity
	        newOrderQuantity = orderQuantity;

    		if ( newOrderQuantity.intValue() > productQuantity.intValue()) {
    			throw new InsufficientProductQuantityException(entityName);
    		}

	        // Add Order Detail to the Order
        	OrderDetDTO newOrderDetDTO = new OrderDetDTO();
        	newOrderDetDTO.setOrderId(newOrdersDTOAux.getId());
        	newOrderDetDTO.setOrderQuantity(newOrderQuantity);
        	newOrderDetDTO.setUnitPrice(productPrice);
        	newOrderDetDTO.setProductId(productId);
        	newOrderDetDTO.setCreatedBy(login);
        	newOrderDetDTO.setCreatedDate(Instant.now());

        	orderDetService.save(newOrderDetDTO);
	        // End Create Order Detail
        }

        log.debug("REST request to addBasket - orderId: {}", orderId);
    }

}
