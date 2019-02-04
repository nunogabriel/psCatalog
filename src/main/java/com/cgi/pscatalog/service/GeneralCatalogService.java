package com.cgi.pscatalog.service;

import java.util.Optional;

import org.springframework.data.domain.Pageable;

import com.cgi.pscatalog.service.dto.GeneralCatalogDTO;
import com.cgi.pscatalog.service.dto.ProductsDTO;
import com.cgi.pscatalog.service.util.PageDataUtil;
import com.cgi.pscatalog.web.rest.errors.ProductAlreadyInPersonalCatalogException;

/**
 * Service Interface for managing General Catalog.
 */
public interface GeneralCatalogService {

	PageDataUtil<Object[], GeneralCatalogDTO> getAllGeneralCatalog(Pageable pageable, String entityName);

	Optional<GeneralCatalogDTO> getGeneralCatalog(Long id);

	PageDataUtil<ProductsDTO, GeneralCatalogDTO> searchGeneralCatalog(String query, Pageable pageable);

	void addBasket(GeneralCatalogDTO generalCatalogDTO, String entityName);

	String addPersonalCatalog(Long id, String entityName) throws ProductAlreadyInPersonalCatalogException;

}
