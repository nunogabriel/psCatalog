package com.cgi.pscatalog.service.impl;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashSet;
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
import com.cgi.pscatalog.service.GeneralCatalogService;
import com.cgi.pscatalog.service.ProductsService;
import com.cgi.pscatalog.service.dto.CustomersDTO;
import com.cgi.pscatalog.service.dto.GeneralCatalogDTO;
import com.cgi.pscatalog.service.dto.ProductsDTO;
import com.cgi.pscatalog.service.util.PageDataUtil;
import com.cgi.pscatalog.web.rest.errors.FirstCreateCustomerException;
import com.cgi.pscatalog.web.rest.errors.ProductAlreadyInPersonalCatalogException;

/**
 * Service Implementation for managing General Catalog.
 */
@Service
@Transactional(rollbackFor=Exception.class)
public class GeneralCatalogServiceImpl implements GeneralCatalogService {

    private final Logger log = LoggerFactory.getLogger(GeneralCatalogServiceImpl.class);

    @Autowired
    private CustomersService customersService;

    @Autowired
    private ProductsService productsService;

    @Autowired
    private CatalogService catalogService;

    @Override
    @Transactional(readOnly = true)
	public PageDataUtil<Object[], GeneralCatalogDTO> getAllGeneralCatalog(Pageable pageable, String entityName) {
        // Get customer identification by login
        Optional<CustomersDTO> optionalCustomerDTO = customersService.getCustomersByLogin(SecurityUtils.getCurrentUserLogin().get());

        if ( !optionalCustomerDTO.isPresent() ) {
            throw new FirstCreateCustomerException(entityName);
        }

        // Get all active products with or not with promotions
        Page<Object[]> page = productsService.getAllProductsWithPromotions(pageable);

		List<GeneralCatalogDTO> listGeneralCatalogDTO = new ArrayList<GeneralCatalogDTO>();

		for (Iterator<Object[]> iterator = page.iterator(); iterator.hasNext();) {
			Object[] products = iterator.next();

			GeneralCatalogDTO generalCatalogDTO = new GeneralCatalogDTO();
			generalCatalogDTO.setId(((BigInteger)products[0]).longValue());
			generalCatalogDTO.setProductName((String)products[1]);
			generalCatalogDTO.setProductDescription((String)products[2]);
			generalCatalogDTO.setProductType(ProductTypeEnum.valueOf((String)products[3]));
			generalCatalogDTO.setProductImg((byte[])products[4]);
			generalCatalogDTO.setProductImgContentType((String)products[5]);
			generalCatalogDTO.setProductPrice((BigDecimal)products[6]);
			generalCatalogDTO.setOrderQuantity(new Integer(0));

			listGeneralCatalogDTO.add(generalCatalogDTO);
		}

		// Set page information
		PageDataUtil<Object[], GeneralCatalogDTO> pageDataUtil = new PageDataUtil<Object[], GeneralCatalogDTO>();
		pageDataUtil.setPageInformation(page);
		pageDataUtil.setContent(listGeneralCatalogDTO);

		return pageDataUtil;
	}

    @Override
    @Transactional(readOnly = true)
	public Optional<GeneralCatalogDTO> getGeneralCatalog(Long id) {
		// Get all active products with or not with promotions by product id
		Page<Object[]> page = productsService.getAllProductsWithPromotionsByProductId(id, PageRequest.of(0, 1));

		Optional<GeneralCatalogDTO> generalCatalogDTOOpt = Optional.empty();

		for (Iterator<Object[]> iterator = page.iterator(); iterator.hasNext();) {
			Object[] products = iterator.next();

			GeneralCatalogDTO generalCatalogDTO = new GeneralCatalogDTO();
			generalCatalogDTO.setId(((BigInteger)products[0]).longValue());
			generalCatalogDTO.setProductName((String)products[1]);
			generalCatalogDTO.setProductDescription((String)products[2]);
			generalCatalogDTO.setProductType(ProductTypeEnum.valueOf((String)products[3]));
			generalCatalogDTO.setProductImg((byte[])products[4]);
			generalCatalogDTO.setProductImgContentType((String)products[5]);
			generalCatalogDTO.setProductPrice((BigDecimal)products[6]);
			generalCatalogDTO.setOrderQuantity(new Integer(0));

			generalCatalogDTOOpt = Optional.of(generalCatalogDTO);
			break;
		}

		return generalCatalogDTOOpt;
	}

    @Override
    @Transactional(readOnly = true)
	public PageDataUtil<ProductsDTO, GeneralCatalogDTO> searchGeneralCatalog(String query, Pageable pageable) {
		Page<ProductsDTO> page = productsService.search(query, pageable);

		List<ProductsDTO> listProductsDTO = page.getContent();
		List<GeneralCatalogDTO> listGeneralCatalogDTO = new ArrayList<GeneralCatalogDTO>();

		for (Iterator<ProductsDTO> iterator = listProductsDTO.iterator(); iterator.hasNext();) {
			ProductsDTO productsDTO = iterator.next();

			GeneralCatalogDTO generalCatalogDTO = new GeneralCatalogDTO();
			generalCatalogDTO.setId(productsDTO.getId());
			generalCatalogDTO.setProductName(productsDTO.getProductName());
			generalCatalogDTO.setProductDescription(productsDTO.getProductDescription());
			generalCatalogDTO.setProductType(productsDTO.getProductType());
			generalCatalogDTO.setProductImg(productsDTO.getProductImg());
			generalCatalogDTO.setProductImgContentType(productsDTO.getProductImgContentType());
			generalCatalogDTO.setProductPrice(productsDTO.getProductPrice());
			generalCatalogDTO.setOrderQuantity(new Integer(0));

			listGeneralCatalogDTO.add(generalCatalogDTO);
		}

		// Set page information
		PageDataUtil<ProductsDTO, GeneralCatalogDTO> pageDataUtil = new PageDataUtil<ProductsDTO, GeneralCatalogDTO>();
		pageDataUtil.setPageInformation(page);
		pageDataUtil.setContent(listGeneralCatalogDTO);

		return pageDataUtil;
    }

    @Override
	public void addBasket(GeneralCatalogDTO generalCatalogDTO, String entityName) {
    	// Add product to basket
    	catalogService.addBasket(generalCatalogDTO.getId(), generalCatalogDTO.getOrderQuantity(), generalCatalogDTO.getProductPrice(), entityName);
    }

    @Override
    public String addPersonalCatalog(Long id, String entityName) throws ProductAlreadyInPersonalCatalogException {
    	// Get login
    	String login = SecurityUtils.getCurrentUserLogin().get();

        // Get customer identification by login
    	Optional<CustomersDTO> optionalCustomerDTO = customersService.getCustomersByLogin(login);

        if ( optionalCustomerDTO.isPresent() ) {
        	CustomersDTO customersDTO = optionalCustomerDTO.get();

            Set<ProductsDTO> oldSetProductsDTO = customersDTO.getProducts();

            boolean productAlreadyInPersonalCatalog = false;
            String productName = null;

            for (Iterator<ProductsDTO> iterator = oldSetProductsDTO.iterator(); iterator.hasNext();) {
    			ProductsDTO productsDTO = iterator.next();

    			if (productsDTO.getId().longValue() == id.longValue()) {
    				productName = productsDTO.getProductName();
    				productAlreadyInPersonalCatalog = true;
    				break;
    			}
    		}

            log.debug("REST request to add personal catalog productAlreadyInPersonalCatalog: {}", productName);

            if ( productAlreadyInPersonalCatalog ) {
            	throw new ProductAlreadyInPersonalCatalogException(productName);
            }

            // Find product
            Optional<ProductsDTO> productsDTOOpt = productsService.findOne(id);
            Set<ProductsDTO> setProductsDTO = new HashSet<ProductsDTO>();

            // Add new product
    		if (productsDTOOpt.isPresent()) {
    			ProductsDTO productsDTO = productsDTOOpt.get();
    			productName = productsDTO.getProductName();
    			setProductsDTO.add( productsDTO );
    		}

    		// Add old products
    		for (Iterator<ProductsDTO> iterator = oldSetProductsDTO.iterator(); iterator.hasNext();) {
    			ProductsDTO productsDTO = iterator.next();

    			setProductsDTO.add( productsDTO );
    		}

    		customersDTO.setProducts(setProductsDTO);
    		customersDTO.setLastModifiedBy(login);
            customersDTO.setLastModifiedDate(Instant.now());

            customersService.save(customersDTO);

            return productName;
        } else {
        	throw new FirstCreateCustomerException(entityName);
        }
    }
}
