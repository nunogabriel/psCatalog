package com.cgi.pscatalog.service.impl;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cgi.pscatalog.config.Constants;
import com.cgi.pscatalog.domain.enumeration.ProductTypeEnum;
import com.cgi.pscatalog.security.SecurityUtils;
import com.cgi.pscatalog.service.CartOrderDetService;
import com.cgi.pscatalog.service.CustomersService;
import com.cgi.pscatalog.service.OrderDetService;
import com.cgi.pscatalog.service.OrderStatusService;
import com.cgi.pscatalog.service.OrdersService;
import com.cgi.pscatalog.service.ProductsService;
import com.cgi.pscatalog.service.dto.CartOrderDetDTO;
import com.cgi.pscatalog.service.dto.CustomersDTO;
import com.cgi.pscatalog.service.dto.OrderDetDTO;
import com.cgi.pscatalog.service.dto.OrderStatusDTO;
import com.cgi.pscatalog.service.dto.OrdersDTO;
import com.cgi.pscatalog.service.dto.ProductsDTO;
import com.cgi.pscatalog.service.util.PageDataUtil;
import com.cgi.pscatalog.web.rest.errors.BadRequestAlertException;
import com.cgi.pscatalog.web.rest.errors.FirstCreateCustomerException;
import com.cgi.pscatalog.web.rest.errors.InsufficientProductQuantityException;

/**
 * Service Implementation for managing OrderDet.
 */
@Service
@Transactional(rollbackFor=Exception.class)
public class CartOrderDetServiceImpl implements CartOrderDetService {

    private final Logger log = LoggerFactory.getLogger(CartOrderDetServiceImpl.class);

    @Autowired
    private OrdersService ordersService;

    @Autowired
    private OrderDetService orderDetService;

    @Autowired
    private OrderStatusService orderStatusService;

    @Autowired
    private ProductsService productsService;

    @Autowired
    private CustomersService customersService;

	@Override
	@Transactional(readOnly = true)
    public PageDataUtil<OrderDetDTO, CartOrderDetDTO> getAllCartOrderDets(Pageable pageable, String entityName) {
        log.debug("REST request to get a page of CartOrderDets");

        // Get login
        String login = SecurityUtils.getCurrentUserLogin().get();

        // Get customer identification by login
        Optional<CustomersDTO> optionalCustomersDTO = customersService.getCustomersByLogin(login);

        if ( !optionalCustomersDTO.isPresent() ) {
        	throw new FirstCreateCustomerException(entityName);
        }

    	log.debug("REST request to getAllCartOrderDets - customerId: {}", optionalCustomersDTO.get().getId());

        // 1 - Verify if there is any order created for customer (get Order by Customer identification)
        List<CartOrderDetDTO> listCartOrderDetDTO = new ArrayList<CartOrderDetDTO>();

        // Get customer order details by order id and status different from PENDING
        Page<OrderDetDTO> pageOrderDetDTO = orderDetService.getAllByLoginAndOrderStatusPending(login, pageable);

        List<OrderDetDTO> listOrderDetDTO = pageOrderDetDTO.getContent();

		for (Iterator<OrderDetDTO> iterator = listOrderDetDTO.iterator(); iterator.hasNext();) {
			OrderDetDTO orderDetDTO = iterator.next();

			CartOrderDetDTO cartOrderDetDTO = new CartOrderDetDTO();
			cartOrderDetDTO.setId(orderDetDTO.getId());
			cartOrderDetDTO.setOrderId(orderDetDTO.getOrderId());
			cartOrderDetDTO.setOrderOrderReference(orderDetDTO.getOrderOrderReference());
			cartOrderDetDTO.setOrderQuantity(orderDetDTO.getOrderQuantity());
			cartOrderDetDTO.setProductId(orderDetDTO.getProductId());
			cartOrderDetDTO.setProductProductName(orderDetDTO.getProductProductName());

			// Get all active products with or not with promotions by product id
			Page<Object[]> page = productsService.getAllProductsWithPromotionsByProductId(orderDetDTO.getProductId(), PageRequest.of(0, 1));

			for (Iterator<Object[]> iteratorProducts = page.iterator(); iteratorProducts.hasNext();) {
				Object[] products = iteratorProducts.next();

				cartOrderDetDTO.setProductDescription((String)products[2]);
				cartOrderDetDTO.setProductType(ProductTypeEnum.valueOf((String)products[3]));
				cartOrderDetDTO.setProductImg((byte[])products[4]);
				cartOrderDetDTO.setProductImgContentType((String)products[5]);
				cartOrderDetDTO.setUnitPrice((BigDecimal)products[6]);
				cartOrderDetDTO.setTotalPrice(((BigDecimal)products[6]).multiply(new BigDecimal(orderDetDTO.getOrderQuantity())));
				break;
			}

			listCartOrderDetDTO.add(cartOrderDetDTO);
		}

		// Set page information
		PageDataUtil<OrderDetDTO, CartOrderDetDTO> pageDataUtil = new PageDataUtil<OrderDetDTO, CartOrderDetDTO>();
		pageDataUtil.setPageInformation(pageOrderDetDTO);
		pageDataUtil.setContent(listCartOrderDetDTO);

		return pageDataUtil;
    }

	@Override
	@Transactional(readOnly = true)
    public Optional<CartOrderDetDTO> getCartOrderDet(Long id) {
        Optional<OrderDetDTO> orderDetDTOOpt = orderDetService.findOne(id);

        Optional<CartOrderDetDTO> cartOrderDetDTOOpt = Optional.empty();

		if (orderDetDTOOpt.isPresent()) {
			OrderDetDTO orderDetDTO = orderDetDTOOpt.get();

			CartOrderDetDTO cartOrderDetDTO = new CartOrderDetDTO();
			cartOrderDetDTO.setId(orderDetDTO.getId());
			cartOrderDetDTO.setOrderId(orderDetDTO.getOrderId());
			cartOrderDetDTO.setOrderOrderReference(orderDetDTO.getOrderOrderReference());
			cartOrderDetDTO.setOrderQuantity(orderDetDTO.getOrderQuantity());
			cartOrderDetDTO.setProductId(orderDetDTO.getProductId());
			cartOrderDetDTO.setProductProductName(orderDetDTO.getProductProductName());

			// Get all active products with or not with promotions by product id
			Page<Object[]> page = productsService.getAllProductsWithPromotionsByProductId(orderDetDTO.getProductId(), PageRequest.of(0, 1));

			for (Iterator<Object[]> iteratorProducts = page.iterator(); iteratorProducts.hasNext();) {
				Object[] products = iteratorProducts.next();

				cartOrderDetDTO.setProductDescription((String)products[2]);
				cartOrderDetDTO.setProductType(ProductTypeEnum.valueOf((String)products[3]));
				cartOrderDetDTO.setProductImg((byte[])products[4]);
				cartOrderDetDTO.setProductImgContentType((String)products[5]);
				cartOrderDetDTO.setUnitPrice((BigDecimal)products[6]);
				cartOrderDetDTO.setTotalPrice(((BigDecimal)products[6]).multiply(new BigDecimal(orderDetDTO.getOrderQuantity())));
				break;
			}

			cartOrderDetDTOOpt = Optional.of(cartOrderDetDTO);
		}

        return cartOrderDetDTOOpt;
    }

    /**
     * Search for the orderDet corresponding to the query.
     *
     * @param query the query of the search
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public PageDataUtil<OrderDetDTO, CartOrderDetDTO> searchCatOrderDets(String query, Pageable pageable) {
        log.debug("Request to search for a page of searchCatOrderDets for query {}", query);

        Page<OrderDetDTO> page = orderDetService.search(query, pageable);

		List<OrderDetDTO> listOrderDetDTO = page.getContent();
		List<CartOrderDetDTO> listCartOrderDetDTO = new ArrayList<CartOrderDetDTO>();

		for (Iterator<OrderDetDTO> iterator = listOrderDetDTO.iterator(); iterator.hasNext();) {
			OrderDetDTO orderDetDTO = iterator.next();

			CartOrderDetDTO cartOrderDetDTO = new CartOrderDetDTO();
			cartOrderDetDTO.setId(orderDetDTO.getId());
			cartOrderDetDTO.setOrderId(orderDetDTO.getOrderId());
			cartOrderDetDTO.setOrderOrderReference(orderDetDTO.getOrderOrderReference());
			cartOrderDetDTO.setOrderQuantity(orderDetDTO.getOrderQuantity());
			cartOrderDetDTO.setProductId(orderDetDTO.getProductId());
			cartOrderDetDTO.setProductProductName(orderDetDTO.getProductProductName());
			cartOrderDetDTO.setUnitPrice(orderDetDTO.getUnitPrice());
			cartOrderDetDTO.setTotalPrice(orderDetDTO.getUnitPrice().multiply(new BigDecimal(orderDetDTO.getOrderQuantity())));

			Optional<ProductsDTO> productsDTOopt = productsService.findOne(orderDetDTO.getProductId());

			if (productsDTOopt.isPresent()) {
				ProductsDTO productsDTO = productsDTOopt.get();

				cartOrderDetDTO.setProductDescription(productsDTO.getProductDescription());
				cartOrderDetDTO.setProductType(productsDTO.getProductType());
				cartOrderDetDTO.setProductImg(productsDTO.getProductImg());
				cartOrderDetDTO.setProductImgContentType(productsDTO.getProductImgContentType());
			}

			listCartOrderDetDTO.add(cartOrderDetDTO);
		}

		// Set page information
		PageDataUtil<OrderDetDTO, CartOrderDetDTO> pageDataUtil = new PageDataUtil<OrderDetDTO, CartOrderDetDTO>();
		pageDataUtil.setPageInformation(page);
		pageDataUtil.setContent(listCartOrderDetDTO);

		return pageDataUtil;

    }

	@Override
    public OrdersDTO createCartOrderDet(Long addressId, Long deliveryAddressId, String entityName) {
        log.debug("REST request to save CartOrderDet");

        // Get login
        String login = SecurityUtils.getCurrentUserLogin().get();

        // 1 - Verify if there is any pending order created for customer
        Page<OrdersDTO> pageOrdersDTO = ordersService.getAllByLoginAndOrderStatusPending(login, PageRequest.of(0, 1));

        List<OrdersDTO> listOrdersDTO = pageOrdersDTO.getContent();

        OrdersDTO ordersDTOAux = new OrdersDTO();

        if ( listOrdersDTO.size() == 0 ) {
        	// There is no need to create the order because already exists
        } else {
	        // The order already exists in status PENDING, change his status to NEW
        	// Get Order Status Id of NEW status
	        Optional<OrderStatusDTO> optionalOrderStatusDTO = orderStatusService.getOrderStatusByDescription(Constants.ORDER_STATUS_NEW);

	        Long orderStatusId = new Long (0);

	        if ( optionalOrderStatusDTO.isPresent() ) {
	        	OrderStatusDTO orderStatusDTO = optionalOrderStatusDTO.get();

				orderStatusId = orderStatusDTO.getId();

		        if (orderStatusId.longValue() == 0) {
		            throw new BadRequestAlertException("Bad configuration, configure order status first", entityName, "idnull");
		        }

		        log.debug("REST request to createCartOrderDet - orderStatusId: {}", orderStatusId);
	        }

	        // Change order status from PENDING to NEW
	        OrdersDTO ordersDTO = listOrdersDTO.get(0);

			ordersDTO.setOrderStatusId(orderStatusId);
			ordersDTO.setLastModifiedBy(login);
			ordersDTO.setLastModifiedDate(Instant.now());
			ordersDTO.setOrderDate(Instant.now());
			ordersDTO.setAddressId(addressId);
			ordersDTO.setDeliveryAddressId(deliveryAddressId);

			ordersDTOAux = ordersService.save(ordersDTO);

			// Get all order details from order
			Page<OrderDetDTO> pageOrderDetDTO = orderDetService.getAllByOrderId(ordersDTO.getId(), PageRequest.of(0, 1000));

			List<OrderDetDTO> listOrderDetDTO = pageOrderDetDTO.getContent();

			for (Iterator<OrderDetDTO> iterator = listOrderDetDTO.iterator(); iterator.hasNext();) {
				OrderDetDTO orderDetDTO = iterator.next();

				// Get all active products with or not with promotions by product id
				Page<Object[]> page = productsService.getAllProductsWithPromotionsByProductId(orderDetDTO.getProductId(), PageRequest.of(0, 1));

				for (Iterator<Object[]> iteratorProducts = page.iterator(); iteratorProducts.hasNext();) {
					Object[] products = iteratorProducts.next();

	        		// Validate product quantity
	        		if ( orderDetDTO.getOrderQuantity().intValue() > ((Integer)products[7]).intValue()) {
	        			throw new InsufficientProductQuantityException(entityName);
	        		}

					// Update unit price because of promotions
					orderDetDTO.setUnitPrice((BigDecimal)products[6]);
			        orderDetDTO.setLastModifiedBy(login);
			        orderDetDTO.setLastModifiedDate(Instant.now());

			        // Get Product to decrease product quantity
			        Optional<ProductsDTO> optionalProductsDTO = productsService.findOne(orderDetDTO.getProductId());

			        if ( optionalProductsDTO.isPresent() ) {
			        	ProductsDTO productsDTO = optionalProductsDTO.get();

			        	// Decrease product quantity
			        	productsDTO.setProductQuantity(productsDTO.getProductQuantity() - orderDetDTO.getOrderQuantity());

			        	productsService.save(productsDTO);
			        }

					break;
				}

				if ( page.getSize() != 0) {
					// Update order detail with unit price
					orderDetService.save(orderDetDTO);
				}
			}
        }

        return ordersDTOAux;
    }

	@Override
    public void updateCartOrderDet(CartOrderDetDTO cartOrderDetDTO, String entityName) {
    	// Get available product quantity
    	Optional<ProductsDTO> optionalProductsDTO = productsService.findOne(cartOrderDetDTO.getProductId());

        Integer productQuantity = new Integer(0);

    	if ( optionalProductsDTO.isPresent() ) {
    		ProductsDTO productsDTO = optionalProductsDTO.get();

    		productQuantity = productsDTO.getProductQuantity();
    	}

		// Validate product quantity
		if ( cartOrderDetDTO.getOrderQuantity().intValue() > productQuantity.intValue()) {
			throw new InsufficientProductQuantityException(entityName);
		}

		// Update cart order detail
		Optional<OrderDetDTO> optionalOrderDetDTO = orderDetService.findOne(cartOrderDetDTO.getId());

		if ( optionalOrderDetDTO.isPresent() ) {
			OrderDetDTO orderDetDTO = optionalOrderDetDTO.get();

	        orderDetDTO.setOrderQuantity(cartOrderDetDTO.getOrderQuantity());
	        orderDetDTO.setLastModifiedBy(SecurityUtils.getCurrentUserLogin().get());
	        orderDetDTO.setLastModifiedDate(Instant.now());

	        orderDetService.save(orderDetDTO);
		}
    }

	@Override
    public void deleteCartOrderDet(Long id) {
        // Get login
        String login = SecurityUtils.getCurrentUserLogin().get();

		// Delete cart order detail
		orderDetService.delete(id);

        // Get customer order details by order id and status different from PENDING
        Page<OrderDetDTO> pageOrderDetDTO = orderDetService.getAllByLoginAndOrderStatusPending(login, PageRequest.of(0, 1));

        List<OrderDetDTO> listOrderDetDTO = pageOrderDetDTO.getContent();

		// Delete Order if there are not more products associated with it
		if (listOrderDetDTO.size() == 0) {
			// Get PENDING order
			Page<OrdersDTO> page = ordersService.getAllByLoginAndOrderStatusPending(login, PageRequest.of(0, 1));

	        List<OrdersDTO> listOrdersDTO = page.getContent();

	        if (listOrdersDTO.size() != 0) {
	        	OrdersDTO ordersDTO = listOrdersDTO.get(0);

	        	// Delete PENDING ORDER
	        	ordersService.delete(ordersDTO.getId());
	        }
		}
	}
}
