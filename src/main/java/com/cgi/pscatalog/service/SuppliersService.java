package com.cgi.pscatalog.service;

import com.cgi.pscatalog.service.dto.SuppliersDTO;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing Suppliers.
 */
public interface SuppliersService {

    /**
     * Save a suppliers.
     *
     * @param suppliersDTO the entity to save
     * @return the persisted entity
     */
    SuppliersDTO save(SuppliersDTO suppliersDTO);

    /**
     * Get all the suppliers.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<SuppliersDTO> findAll(Pageable pageable);


    /**
     * Get the "id" suppliers.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<SuppliersDTO> findOne(Long id);

    /**
     * Delete the "id" suppliers.
     *
     * @param id the id of the entity
     */
    void delete(Long id);

    /**
     * Search for the suppliers corresponding to the query.
     *
     * @param query the query of the search
     * 
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<SuppliersDTO> search(String query, Pageable pageable);
}
