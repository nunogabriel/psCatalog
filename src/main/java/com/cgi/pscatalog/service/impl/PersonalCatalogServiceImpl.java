package com.cgi.pscatalog.service.impl;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cgi.pscatalog.domain.enumeration.ProductTypeEnum;
import com.cgi.pscatalog.security.SecurityUtils;
import com.cgi.pscatalog.service.CatalogService;
import com.cgi.pscatalog.service.CustomersService;
import com.cgi.pscatalog.service.PersonalCatalogService;
import com.cgi.pscatalog.service.ProductsService;
import com.cgi.pscatalog.service.dto.CustomersDTO;
import com.cgi.pscatalog.service.dto.PersonalCatalogDTO;
import com.cgi.pscatalog.service.dto.ProductsDTO;
import com.cgi.pscatalog.service.util.PageDataUtil;
import com.cgi.pscatalog.web.rest.errors.FirstCreateCustomerException;

/**
 * Service Implementation for managing Personal Catalog.
 */
@Service
@Transactional(rollbackFor=Exception.class)
public class PersonalCatalogServiceImpl implements PersonalCatalogService {

    private final Logger log = LoggerFactory.getLogger(PersonalCatalogServiceImpl.class);

    @Autowired
    private ProductsService productsService;

    @Autowired
    private CatalogService catalogService;

    @Autowired
    private CustomersService customersService;

    @Override
    @Transactional(readOnly = true)
	public PageDataUtil<Object[], PersonalCatalogDTO> getAllPersonalCatalog(Pageable pageable, String entityName) {
        // Get customer identification by login
		Optional<CustomersDTO> optionalCustomerDTO = customersService.getCustomersByLogin(SecurityUtils.getCurrentUserLogin().get());

        if ( !optionalCustomerDTO.isPresent() ) {
            throw new FirstCreateCustomerException(entityName);
        }

    	Long customerId = optionalCustomerDTO.get().getId();

    	log.debug("REST request to getAllPersonalCatalog - customerId: {}", customerId);

        // Get all active products with or not with promotions by customer id
        Page<Object[]> page = productsService.getAllProductsWithPromotionsByCustomersId(customerId, pageable);

		List<PersonalCatalogDTO> listPersonalCatalogDTO = new ArrayList<PersonalCatalogDTO>();

		for (Iterator<Object[]> iterator = page.iterator(); iterator.hasNext();) {
			Object[] products = iterator.next();

			PersonalCatalogDTO personalCatalogDTO = new PersonalCatalogDTO();
			personalCatalogDTO.setId(((BigInteger)products[0]).longValue());
			personalCatalogDTO.setProductName((String)products[1]);
			personalCatalogDTO.setProductDescription((String)products[2]);
			personalCatalogDTO.setProductType(ProductTypeEnum.valueOf((String)products[3]));
			personalCatalogDTO.setProductImg((byte[])products[4]);
			personalCatalogDTO.setProductImgContentType((String)products[5]);
			personalCatalogDTO.setProductPrice((BigDecimal)products[6]);
			personalCatalogDTO.setOrderQuantity(new Integer(0));

			listPersonalCatalogDTO.add(personalCatalogDTO);
		}

		// Set page information
		PageDataUtil<Object[], PersonalCatalogDTO> pageDataUtil = new PageDataUtil<Object[], PersonalCatalogDTO>();
		pageDataUtil.setPageInformation(page);
		pageDataUtil.setContent(listPersonalCatalogDTO);

		return pageDataUtil;
	}

    @Override
    @Transactional(readOnly = true)
	public Optional<PersonalCatalogDTO> getPersonalCatalog(Long id, String entityName) {
		// Get customer identification by login
        Optional<CustomersDTO> optionalCustomersDTO = customersService.getCustomersByLogin(SecurityUtils.getCurrentUserLogin().get());

        if ( !optionalCustomersDTO.isPresent() ) {
        	throw new FirstCreateCustomerException(entityName);
        }

    	Long customerId = optionalCustomersDTO.get().getId();

    	log.debug("REST request to getAllPersonalCatalog - customerId: {}", customerId);

        // Get all active products with or not with promotions by customer id and product id
        Page<Object[]> page = productsService.getAllProductsWithPromotionsByCustomersIdAndProductId(customerId, id, PageRequest.of(0, 1));

		Optional<PersonalCatalogDTO> personalCatalogDTOOpt = Optional.empty();

		for (Iterator<Object[]> iterator = page.iterator(); iterator.hasNext();) {
			Object[] products = iterator.next();

			PersonalCatalogDTO personalCatalogDTO = new PersonalCatalogDTO();
			personalCatalogDTO.setId(((BigInteger)products[0]).longValue());
			personalCatalogDTO.setProductName((String)products[1]);
			personalCatalogDTO.setProductDescription((String)products[2]);
			personalCatalogDTO.setProductType(ProductTypeEnum.valueOf((String)products[3]));
			personalCatalogDTO.setProductImg((byte[])products[4]);
			personalCatalogDTO.setProductImgContentType((String)products[5]);
			personalCatalogDTO.setProductPrice((BigDecimal)products[6]);
			personalCatalogDTO.setOrderQuantity(new Integer(0));

			personalCatalogDTOOpt = Optional.of(personalCatalogDTO);
			break;
		}

		return personalCatalogDTOOpt;
	}

    @Override
    @Transactional(readOnly = true)
	public PageDataUtil<ProductsDTO, PersonalCatalogDTO> searchPersonalCatalog(String query, Pageable pageable) {
		Page<ProductsDTO> page = productsService.search(query, pageable);

		List<ProductsDTO> listProductsDTO = page.getContent();
		List<PersonalCatalogDTO> listPersonalCatalogDTO = new ArrayList<PersonalCatalogDTO>();

		for (Iterator<ProductsDTO> iterator = listProductsDTO.iterator(); iterator.hasNext();) {
			ProductsDTO productsDTO = iterator.next();

			PersonalCatalogDTO personalCatalogDTO = new PersonalCatalogDTO();
			personalCatalogDTO.setId(productsDTO.getId());
			personalCatalogDTO.setProductName(productsDTO.getProductName());
			personalCatalogDTO.setProductDescription(productsDTO.getProductDescription());
			personalCatalogDTO.setProductType(productsDTO.getProductType());
			personalCatalogDTO.setProductImg(productsDTO.getProductImg());
			personalCatalogDTO.setProductImgContentType(productsDTO.getProductImgContentType());
			personalCatalogDTO.setProductPrice(productsDTO.getProductPrice());
			personalCatalogDTO.setOrderQuantity(new Integer(0));

			listPersonalCatalogDTO.add(personalCatalogDTO);
		}

		// Set page information
		PageDataUtil<ProductsDTO, PersonalCatalogDTO> pageDataUtil = new PageDataUtil<ProductsDTO, PersonalCatalogDTO>();
		pageDataUtil.setPageInformation(page);
		pageDataUtil.setContent(listPersonalCatalogDTO);

		return pageDataUtil;
	}

    @Override
	public void addBasket(PersonalCatalogDTO personalCatalogDTO, String entityName) {
    	// Add product to basket
    	catalogService.addBasket(personalCatalogDTO.getId(), personalCatalogDTO.getOrderQuantity(), personalCatalogDTO.getProductPrice(), entityName);
    }

    @Override
    public String deletePersonalCatalog(Long id, String entityName) {
    	// Get login
    	String login = SecurityUtils.getCurrentUserLogin().get();

        // Get customer identification by login
        Optional<CustomersDTO> optionalCustomersDTO = customersService.getCustomersByLogin(login);

        if ( optionalCustomersDTO.isPresent() ) {
        	CustomersDTO customersDTO = optionalCustomersDTO.get();

            Set<ProductsDTO> oldSetProductsDTO = customersDTO.getProducts();

            // Find product
            Optional<ProductsDTO> productsDTOOpt = productsService.findOne(id);
            ProductsDTO productsDTO = null;

            // Add new product
    		if ( productsDTOOpt.isPresent() ) {
    			productsDTO = productsDTOOpt.get();

    			oldSetProductsDTO.remove(productsDTO);

    			customersDTO.setProducts(oldSetProductsDTO);
        		customersDTO.setLastModifiedBy(login);
                customersDTO.setLastModifiedDate(Instant.now());

                customersService.save(customersDTO);

    	        log.debug("REST request to delete personal catalog updateCustomers: {}", productsDTO.getId());

    	        return productsDTO.getProductName();
    		} else {
    			return "-1";
    		}
        } else {
        	throw new FirstCreateCustomerException(entityName);
        }
    }
}
