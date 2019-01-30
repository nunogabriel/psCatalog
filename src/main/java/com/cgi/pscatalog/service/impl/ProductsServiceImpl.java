package com.cgi.pscatalog.service.impl;

import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cgi.pscatalog.domain.Products;
import com.cgi.pscatalog.repository.ProductsRepository;
import com.cgi.pscatalog.repository.search.ProductsSearchRepository;
import com.cgi.pscatalog.service.ProductsService;
import com.cgi.pscatalog.service.dto.ProductsDTO;
import com.cgi.pscatalog.service.mapper.ProductsMapper;

/**
 * Service Implementation for managing Products.
 */
@Service
@Transactional
public class ProductsServiceImpl implements ProductsService {

    private final Logger log = LoggerFactory.getLogger(ProductsServiceImpl.class);

    private final ProductsRepository productsRepository;

    private final ProductsMapper productsMapper;

    private final ProductsSearchRepository productsSearchRepository;

    public ProductsServiceImpl(ProductsRepository productsRepository, ProductsMapper productsMapper, ProductsSearchRepository productsSearchRepository) {
        this.productsRepository = productsRepository;
        this.productsMapper = productsMapper;
        this.productsSearchRepository = productsSearchRepository;
    }

    /**
     * Save a products.
     *
     * @param productsDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public ProductsDTO save(ProductsDTO productsDTO) {
        log.debug("Request to save Products : {}", productsDTO);

        Products products = productsMapper.toEntity(productsDTO);
        products = productsRepository.save(products);
        ProductsDTO result = productsMapper.toDto(products);
        productsSearchRepository.save(products);
        return result;
    }

    /**
     * Get all the products.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<ProductsDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Products");
        return productsRepository.findAll(pageable)
            .map(productsMapper::toDto);
    }


    /**
     * Get one products by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<ProductsDTO> findOne(Long id) {
        log.debug("Request to get Products : {}", id);
        return productsRepository.findById(id)
            .map(productsMapper::toDto);
    }

    /**
     * Delete the products by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Products : {}", id);
        productsRepository.deleteById(id);
        productsSearchRepository.deleteById(id);
    }

    /**
     * Search for the products corresponding to the query.
     *
     * @param query the query of the search
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<ProductsDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Products for query {}", query);
        return productsSearchRepository.search(queryStringQuery(query), pageable)
            .map(productsMapper::toDto);
    }

	@Override
	@Transactional(readOnly = true)
	public Page<Object[]> getAllProductsWithPromotions(Pageable pageable) {
        log.debug("Request to get all products promotions");
        return productsRepository.findAllProductsWithPromotions(pageable);
	}

	@Override
	@Transactional(readOnly = true)
	public Page<Object[]> getAllProductsWithPromotionsByProductId(Long productId, Pageable pageable) {
        log.debug("Request to get all products promotions by product id {}", productId);
        return productsRepository.findAllProductsWithPromotionsByProductId(productId, pageable);
	}

	@Override
	@Transactional(readOnly = true)
	public Page<Object[]> getAllProductsWithPromotionsByCustomersId(Long customerId, Pageable pageable) {
        log.debug("Request to get all products promotions by customer id {}", customerId);
        return productsRepository.findAllProductsWithPromotionsByCustomersId(customerId, pageable);
	}

	@Override
	@Transactional(readOnly = true)
	public Page<Object[]> getAllProductsWithPromotionsByCustomersIdAndProductId(Long customerId, Long productId, Pageable pageable) {
        log.debug("Request to get all products promotions by customer id {} and product id {}", customerId, productId);
        return productsRepository.findAllProductsWithPromotionsByCustomersIdAndProductId(customerId, productId, pageable);
	}

}
