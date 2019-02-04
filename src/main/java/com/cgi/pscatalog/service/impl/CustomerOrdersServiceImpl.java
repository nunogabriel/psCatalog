package com.cgi.pscatalog.service.impl;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cgi.pscatalog.security.SecurityUtils;
import com.cgi.pscatalog.service.CustomerOrdersService;
import com.cgi.pscatalog.service.CustomersService;
import com.cgi.pscatalog.service.OrderDetService;
import com.cgi.pscatalog.service.OrdersService;
import com.cgi.pscatalog.service.dto.CustomerOrdersDTO;
import com.cgi.pscatalog.service.dto.CustomersDTO;
import com.cgi.pscatalog.service.dto.OrderDetDTO;
import com.cgi.pscatalog.service.dto.OrdersDTO;
import com.cgi.pscatalog.service.util.PageDataUtil;
import com.cgi.pscatalog.web.rest.errors.FirstCreateCustomerException;

/**
 * Service Implementation for managing Customer Orders.
 */
@Service
@Transactional(rollbackFor=Exception.class)
public class CustomerOrdersServiceImpl implements CustomerOrdersService {

    private final Logger log = LoggerFactory.getLogger(CustomerOrdersServiceImpl.class);

    @Autowired
    private OrdersService ordersService;

    @Autowired
    private OrderDetService orderDetService;

    @Autowired
	private CustomersService customersService;

    @Override
    @Transactional(readOnly = true)
    public PageDataUtil<OrdersDTO, CustomerOrdersDTO> getAllCustomerOrders(Pageable pageable, String entityName) {
    	// Get login
        String login = SecurityUtils.getCurrentUserLogin().get();

        // Get customer identification by login
        Optional<CustomersDTO> optionalCustomersDTO = customersService.getCustomersByLogin(SecurityUtils.getCurrentUserLogin().get());

        if ( !optionalCustomersDTO.isPresent() ) {
            throw new FirstCreateCustomerException(entityName);
        }

        // Get all orders with status different from PENDING by login
        Page<OrdersDTO> page = ordersService.getAllByLoginAndOrderStatus(login, pageable);

        List<OrdersDTO> listOrdersDTO = page.getContent();

		List<CustomerOrdersDTO> listCustomerOrdersDTO = new ArrayList<CustomerOrdersDTO>();

		for (Iterator<OrdersDTO> iterator = listOrdersDTO.iterator(); iterator.hasNext();) {
			OrdersDTO ordersDTO = iterator.next();

			CustomerOrdersDTO customerOrdersDTO = new CustomerOrdersDTO();
			customerOrdersDTO.setId(ordersDTO.getId());
			customerOrdersDTO.setAddressAddressReference(ordersDTO.getAddressAddressReference());
			customerOrdersDTO.setAddressId(ordersDTO.getAddressId());
			customerOrdersDTO.setCustomerCustomerName(ordersDTO.getCustomerCustomerName());
			customerOrdersDTO.setCustomerId(ordersDTO.getCustomerId());
			customerOrdersDTO.setDeliveryAddressAddressReference(ordersDTO.getDeliveryAddressAddressReference());
			customerOrdersDTO.setDeliveryAddressId(ordersDTO.getDeliveryAddressId());
			customerOrdersDTO.setCreatedDate(ordersDTO.getCreatedDate());
			customerOrdersDTO.setDeliveryDate(ordersDTO.getDeliveryDate());
			customerOrdersDTO.setOrderDate(ordersDTO.getOrderDate());
			customerOrdersDTO.setOrderReference(ordersDTO.getOrderReference());
			customerOrdersDTO.setOrderStatusId(ordersDTO.getOrderStatusId());
			customerOrdersDTO.setOrderStatusOrderStatusDescription(ordersDTO.getOrderStatusOrderStatusDescription());

			listCustomerOrdersDTO.add(customerOrdersDTO);
		}

		// Set page information
		PageDataUtil<OrdersDTO, CustomerOrdersDTO> pageDataUtil = new PageDataUtil<OrdersDTO, CustomerOrdersDTO>();
		pageDataUtil.setPageInformation(page);
		pageDataUtil.setContent(listCustomerOrdersDTO);

		return pageDataUtil;
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<CustomerOrdersDTO> getCustomerOrders(Long id) {
        Optional<OrdersDTO> ordersDTOOpt = ordersService.findOne(id);

        Optional<CustomerOrdersDTO> customerOrdersDTOopt = Optional.empty();

		if (ordersDTOOpt.isPresent()) {
			OrdersDTO ordersDTO = ordersDTOOpt.get();

			CustomerOrdersDTO customerOrdersDTO = new CustomerOrdersDTO();
			customerOrdersDTO.setId(ordersDTO.getId());
			customerOrdersDTO.setAddressAddressReference(ordersDTO.getAddressAddressReference());
			customerOrdersDTO.setAddressId(ordersDTO.getAddressId());
			customerOrdersDTO.setCustomerCustomerName(ordersDTO.getCustomerCustomerName());
			customerOrdersDTO.setCustomerId(ordersDTO.getCustomerId());
			customerOrdersDTO.setDeliveryAddressAddressReference(ordersDTO.getDeliveryAddressAddressReference());
			customerOrdersDTO.setDeliveryAddressId(ordersDTO.getDeliveryAddressId());
			customerOrdersDTO.setCreatedDate(ordersDTO.getCreatedDate());
			customerOrdersDTO.setDeliveryDate(ordersDTO.getDeliveryDate());
			customerOrdersDTO.setOrderDate(ordersDTO.getOrderDate());
			customerOrdersDTO.setOrderReference(ordersDTO.getOrderReference());
			customerOrdersDTO.setOrderStatusId(ordersDTO.getOrderStatusId());
			customerOrdersDTO.setOrderStatusOrderStatusDescription(ordersDTO.getOrderStatusOrderStatusDescription());

			customerOrdersDTOopt = Optional.of(customerOrdersDTO);
		}

        return customerOrdersDTOopt;
    }

    @Override
    @Transactional(readOnly = true)
    public PageDataUtil<OrdersDTO, CustomerOrdersDTO> searchCustomerOrders(String query, Pageable pageable) {
        Page<OrdersDTO> page = ordersService.search(query, pageable);

        List<OrdersDTO> listOrdersDTO = page.getContent();
		List<CustomerOrdersDTO> listCustomerOrdersDTO = new ArrayList<CustomerOrdersDTO>();

		for (Iterator<OrdersDTO> iterator = listOrdersDTO.iterator(); iterator.hasNext();) {
			OrdersDTO ordersDTO = iterator.next();

			CustomerOrdersDTO customerOrdersDTO = new CustomerOrdersDTO();
			customerOrdersDTO.setId(ordersDTO.getId());
			customerOrdersDTO.setAddressAddressReference(ordersDTO.getAddressAddressReference());
			customerOrdersDTO.setAddressId(ordersDTO.getAddressId());
			customerOrdersDTO.setCustomerCustomerName(ordersDTO.getCustomerCustomerName());
			customerOrdersDTO.setCustomerId(ordersDTO.getCustomerId());
			customerOrdersDTO.setDeliveryAddressAddressReference(ordersDTO.getDeliveryAddressAddressReference());
			customerOrdersDTO.setDeliveryAddressId(ordersDTO.getDeliveryAddressId());
			customerOrdersDTO.setCreatedDate(ordersDTO.getCreatedDate());
			customerOrdersDTO.setDeliveryDate(ordersDTO.getDeliveryDate());
			customerOrdersDTO.setOrderDate(ordersDTO.getOrderDate());
			customerOrdersDTO.setOrderReference(ordersDTO.getOrderReference());
			customerOrdersDTO.setOrderStatusId(ordersDTO.getOrderStatusId());
			customerOrdersDTO.setOrderStatusOrderStatusDescription(ordersDTO.getOrderStatusOrderStatusDescription());

			listCustomerOrdersDTO.add(customerOrdersDTO);
		}

		// Set page information
		PageDataUtil<OrdersDTO, CustomerOrdersDTO> pageDataUtil = new PageDataUtil<OrdersDTO, CustomerOrdersDTO>();
		pageDataUtil.setPageInformation(page);
		pageDataUtil.setContent(listCustomerOrdersDTO);

		return pageDataUtil;
    }

	@Override
    public void updateCustomerOrders(CustomerOrdersDTO customerOrdersDTO) {
        Optional<OrdersDTO> ordersDTOOpt = ordersService.findOne(customerOrdersDTO.getId());

		if (ordersDTOOpt.isPresent()) {
			OrdersDTO ordersDTO = ordersDTOOpt.get();

			ordersDTO.setOrderStatusId(customerOrdersDTO.getOrderStatusId());
			ordersDTO.setDeliveryDate(customerOrdersDTO.getDeliveryDate());
			ordersDTO.setLastModifiedBy((SecurityUtils.getCurrentUserLogin().isPresent())?(SecurityUtils.getCurrentUserLogin().get()):"anonymousUser");
			ordersDTO.setLastModifiedDate(Instant.now());

			ordersService.save(ordersDTO);
		}
    }

	@Override
	public void updatePendingOrders(List<OrdersDTO> listOrdersDTOPending, Map<Long,Long> mapOldNewAddresses, Long customerId) {
		// Update PENDING orders with customer data
    	for (Iterator<OrdersDTO> iterator = listOrdersDTOPending.iterator(); iterator.hasNext();) {
			OrdersDTO ordersDTOPending = iterator.next();

			ordersDTOPending.setCustomerId(customerId);

			if ( mapOldNewAddresses.containsKey(ordersDTOPending.getAddressId()) ) {
				log.debug("REST request to update pending orders : setAddressId {}", mapOldNewAddresses.get(ordersDTOPending.getAddressId()));
				ordersDTOPending.setAddressId(mapOldNewAddresses.get(ordersDTOPending.getAddressId()));
			}

			if ( mapOldNewAddresses.containsKey(ordersDTOPending.getDeliveryAddressId()) ) {
				log.debug("REST request to update pending orders : setDeliveryAddressId {}", mapOldNewAddresses.get(ordersDTOPending.getDeliveryAddressId()));
				ordersDTOPending.setDeliveryAddressId(mapOldNewAddresses.get(ordersDTOPending.getDeliveryAddressId()));
			}

			ordersDTOPending.setLastModifiedBy(SecurityUtils.getCurrentUserLogin().get());
			ordersDTOPending.setLastModifiedDate(Instant.now());

			ordersService.save(ordersDTOPending);
			break;
		}
	}

	@Override
	public void deletePendingOrders(Long customerId) {
		// Get login
        String login = SecurityUtils.getCurrentUserLogin().get();

        // Verify if already exists orders with PENDING Status
        Page<OrdersDTO> pageOrdersDTOPending = ordersService.getAllByLoginAndCustomerIdAndOrderStatusPending(login, customerId, PageRequest.of(0, 1));

        List<OrdersDTO> listOrdersDTOPending = pageOrdersDTOPending.getContent();

        for (Iterator<OrdersDTO> iterator = listOrdersDTOPending.iterator(); iterator.hasNext();) {
			OrdersDTO ordersDTOPending = iterator.next();

			// Delete all order details from PENDING order
			Page<OrderDetDTO> pageOrderDetDTOPending = orderDetService.getAllByOrderId(ordersDTOPending.getId(), PageRequest.of(0, 1000));

			List<OrderDetDTO> listOrderDetDTOPending = pageOrderDetDTOPending.getContent();

			for (Iterator<OrderDetDTO> iterator2 = listOrderDetDTOPending.iterator(); iterator2.hasNext();) {
				OrderDetDTO orderDetDTOPending = iterator2.next();

				orderDetService.delete(orderDetDTOPending.getId());
			}

			// Delete PENDING order
			ordersService.delete(ordersDTOPending.getId());
			break;
		}
	}
}
