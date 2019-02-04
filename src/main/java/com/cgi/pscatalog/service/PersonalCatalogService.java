package com.cgi.pscatalog.service;

import java.util.Optional;

import org.springframework.data.domain.Pageable;

import com.cgi.pscatalog.service.dto.PersonalCatalogDTO;
import com.cgi.pscatalog.service.dto.ProductsDTO;
import com.cgi.pscatalog.service.util.PageDataUtil;

/**
 * Service Interface for managing Personal Catalog.
 */
public interface PersonalCatalogService {

	PageDataUtil<Object[], PersonalCatalogDTO> getAllPersonalCatalog(Pageable pageable, String entityName);

	Optional<PersonalCatalogDTO> getPersonalCatalog(Long id, String entityName);

	PageDataUtil<ProductsDTO, PersonalCatalogDTO> searchPersonalCatalog(String query, Pageable pageable);

	void addBasket(PersonalCatalogDTO personalCatalogDTO, String entityName);

	String deletePersonalCatalog(Long id, String entityName);

}
