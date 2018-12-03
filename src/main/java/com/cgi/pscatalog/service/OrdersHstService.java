package com.cgi.pscatalog.service;

import com.cgi.pscatalog.service.dto.OrdersHstDTO;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing OrdersHst.
 */
public interface OrdersHstService {

    /**
     * Save a ordersHst.
     *
     * @param ordersHstDTO the entity to save
     * @return the persisted entity
     */
    OrdersHstDTO save(OrdersHstDTO ordersHstDTO);

    /**
     * Get all the ordersHsts.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<OrdersHstDTO> findAll(Pageable pageable);


    /**
     * Get the "id" ordersHst.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<OrdersHstDTO> findOne(Long id);

    /**
     * Delete the "id" ordersHst.
     *
     * @param id the id of the entity
     */
    void delete(Long id);

    /**
     * Search for the ordersHst corresponding to the query.
     *
     * @param query the query of the search
     * 
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<OrdersHstDTO> search(String query, Pageable pageable);
}
