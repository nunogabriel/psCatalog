package com.cgi.pscatalog.service;

import com.cgi.pscatalog.service.dto.OrderDetDTO;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing OrderDet.
 */
public interface OrderDetService {

    /**
     * Save a orderDet.
     *
     * @param orderDetDTO the entity to save
     * @return the persisted entity
     */
    OrderDetDTO save(OrderDetDTO orderDetDTO);

    /**
     * Get all the orderDets.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<OrderDetDTO> findAll(Pageable pageable);


    /**
     * Get the "id" orderDet.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<OrderDetDTO> findOne(Long id);

    /**
     * Delete the "id" orderDet.
     *
     * @param id the id of the entity
     */
    void delete(Long id);

    /**
     * Search for the orderDet corresponding to the query.
     *
     * @param query the query of the search
     * 
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<OrderDetDTO> search(String query, Pageable pageable);
}
