package com.cgi.pscatalog.service;

import com.cgi.pscatalog.service.dto.OrderDetHstDTO;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing OrderDetHst.
 */
public interface OrderDetHstService {

    /**
     * Save a orderDetHst.
     *
     * @param orderDetHstDTO the entity to save
     * @return the persisted entity
     */
    OrderDetHstDTO save(OrderDetHstDTO orderDetHstDTO);

    /**
     * Get all the orderDetHsts.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<OrderDetHstDTO> findAll(Pageable pageable);


    /**
     * Get the "id" orderDetHst.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<OrderDetHstDTO> findOne(Long id);

    /**
     * Delete the "id" orderDetHst.
     *
     * @param id the id of the entity
     */
    void delete(Long id);

    /**
     * Search for the orderDetHst corresponding to the query.
     *
     * @param query the query of the search
     * 
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<OrderDetHstDTO> search(String query, Pageable pageable);
}
