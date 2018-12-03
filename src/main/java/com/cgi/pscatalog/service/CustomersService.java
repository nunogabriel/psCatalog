package com.cgi.pscatalog.service;

import com.cgi.pscatalog.service.dto.CustomersDTO;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing Customers.
 */
public interface CustomersService {

    /**
     * Save a customers.
     *
     * @param customersDTO the entity to save
     * @return the persisted entity
     */
    CustomersDTO save(CustomersDTO customersDTO);

    /**
     * Get all the customers.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<CustomersDTO> findAll(Pageable pageable);

    /**
     * Get all the Customers with eager load of many-to-many relationships.
     *
     * @return the list of entities
     */
    Page<CustomersDTO> findAllWithEagerRelationships(Pageable pageable);
    
    /**
     * Get the "id" customers.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<CustomersDTO> findOne(Long id);

    /**
     * Delete the "id" customers.
     *
     * @param id the id of the entity
     */
    void delete(Long id);

    /**
     * Search for the customers corresponding to the query.
     *
     * @param query the query of the search
     * 
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<CustomersDTO> search(String query, Pageable pageable);
}
