package com.cgi.pscatalog.service;

import com.cgi.pscatalog.service.dto.CountriesDTO;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing Countries.
 */
public interface CountriesService {

    /**
     * Save a countries.
     *
     * @param countriesDTO the entity to save
     * @return the persisted entity
     */
    CountriesDTO save(CountriesDTO countriesDTO);

    /**
     * Get all the countries.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<CountriesDTO> findAll(Pageable pageable);


    /**
     * Get the "id" countries.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<CountriesDTO> findOne(Long id);

    /**
     * Delete the "id" countries.
     *
     * @param id the id of the entity
     */
    void delete(Long id);

    /**
     * Search for the countries corresponding to the query.
     *
     * @param query the query of the search
     * 
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<CountriesDTO> search(String query, Pageable pageable);
}
