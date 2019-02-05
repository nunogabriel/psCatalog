package com.cgi.pscatalog.service;

import java.util.Optional;

import org.springframework.data.domain.Pageable;

import com.cgi.pscatalog.service.dto.CartOrderDetDTO;
import com.cgi.pscatalog.service.dto.OrderDetDTO;
import com.cgi.pscatalog.service.dto.OrdersDTO;
import com.cgi.pscatalog.service.util.PageDataUtil;

/**
 * Service Interface for managing OrderDet.
 */
public interface CartOrderDetService {

	PageDataUtil<OrderDetDTO, CartOrderDetDTO> searchCatOrderDets(String query, Pageable pageable);

	OrdersDTO createCartOrderDet(Long addressId, Long deliveryAddressId, String entityName);

	void updateCartOrderDet(CartOrderDetDTO cartOrderDetDTO, String entityName);

	PageDataUtil<OrderDetDTO, CartOrderDetDTO> getAllCartOrderDets(Pageable pageable, String entityName);

	Optional<CartOrderDetDTO> getCartOrderDet(Long id);

	void deleteCartOrderDet(Long id);
}
