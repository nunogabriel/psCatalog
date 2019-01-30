package com.cgi.pscatalog.service;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.cgi.pscatalog.service.dto.PromotionsDTO;

/**
 * Service Interface for managing Promotions.
 */
public interface PromotionsService {

    /**
     * Save a promotions.
     *
     * @param promotionsDTO the entity to save
     * @return the persisted entity
     */
    PromotionsDTO save(PromotionsDTO promotionsDTO);

    /**
     * Get all the promotions.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<PromotionsDTO> findAll(Pageable pageable);


    /**
     * Get the "id" promotions.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<PromotionsDTO> findOne(Long id);

    /**
     * Delete the "id" promotions.
     *
     * @param id the id of the entity
     */
    void delete(Long id);

    /**
     * Search for the promotions corresponding to the query.
     *
     * @param query the query of the search
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<PromotionsDTO> search(String query, Pageable pageable);
}
