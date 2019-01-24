package com.cgi.pscatalog.service;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.cgi.pscatalog.service.dto.ProductsDTO;

/**
 * Service Interface for managing Products.
 */
public interface ProductsService {

    /**
     * Save a products.
     *
     * @param productsDTO the entity to save
     * @return the persisted entity
     */
    ProductsDTO save(ProductsDTO productsDTO);

    /**
     * Get all the products.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<ProductsDTO> findAll(Pageable pageable);


    /**
     * Get the "id" products.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<ProductsDTO> findOne(Long id);

    /**
     * Delete the "id" products.
     *
     * @param id the id of the entity
     */
    void delete(Long id);

    /**
     * Search for the products corresponding to the query.
     *
     * @param query the query of the search
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<ProductsDTO> search(String query, Pageable pageable);

    Page<Object[]> getAllProductsWithPromotions(Pageable pageable);

    Page<Object[]> getAllProductsWithPromotionsByProductId(Long productId, Pageable pageable);

	Page<Object[]> getAllProductsWithPromotionsByCustomersId(Long customerId, Pageable pageable);

	Page<Object[]> getAllProductsWithPromotionsByCustomersIdAndProductId(Long customerId, Long productId, Pageable pageable);

}
