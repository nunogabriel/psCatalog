package com.cgi.pscatalog.service;

import java.math.BigDecimal;

/**
 * Service Interface for managing OrderDet.
 */
public interface CatalogService {

	void addBasket(Long productId, Integer orderQuantity, BigDecimal productPrice, String entityName);

}
