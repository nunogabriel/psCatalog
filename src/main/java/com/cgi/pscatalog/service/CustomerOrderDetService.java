package com.cgi.pscatalog.service;

import java.math.BigDecimal;
import java.util.Optional;

import org.springframework.data.domain.Pageable;

import com.cgi.pscatalog.service.dto.CustomerOrdersDetDTO;
import com.cgi.pscatalog.service.dto.OrderDetDTO;
import com.cgi.pscatalog.service.util.PageDataUtil;

/**
 * Service Interface for managing OrderDet.
 */
public interface CustomerOrderDetService {

	PageDataUtil<OrderDetDTO, CustomerOrdersDetDTO> getAllCustomerOrderDets(Pageable pageable);

	PageDataUtil<OrderDetDTO, CustomerOrdersDetDTO> getAllCustomerOrderDetsByOrderId(Long orderId, Pageable pageable);

	PageDataUtil<OrderDetDTO, CustomerOrdersDetDTO> searchCustomerOrdersDet(String query, Pageable pageable);

	Optional<CustomerOrdersDetDTO> getCustomerOrderDet(Long id);

	BigDecimal getPendingOrderTotal();

	BigDecimal getOrderTotalByOrderId(Long orderId);

}
