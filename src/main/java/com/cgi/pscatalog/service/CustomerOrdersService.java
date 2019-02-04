package com.cgi.pscatalog.service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.data.domain.Pageable;

import com.cgi.pscatalog.service.dto.CustomerOrdersDTO;
import com.cgi.pscatalog.service.dto.OrdersDTO;
import com.cgi.pscatalog.service.util.PageDataUtil;

/**
 * Service Interface for managing Orders.
 */
public interface CustomerOrdersService {

	PageDataUtil<OrdersDTO, CustomerOrdersDTO> getAllCustomerOrders(Pageable pageable, String entityName);

	Optional<CustomerOrdersDTO> getCustomerOrders(Long id);

	PageDataUtil<OrdersDTO, CustomerOrdersDTO> searchCustomerOrders(String query, Pageable pageable);

	void updateCustomerOrders(CustomerOrdersDTO customerOrdersDTO);

	void updatePendingOrders(List<OrdersDTO> listOrdersDTOPending, Map<Long,Long> mapOldNewAddresses, Long customerId);

	void deletePendingOrders(Long customerId);

}
