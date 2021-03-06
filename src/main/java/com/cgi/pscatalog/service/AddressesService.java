package com.cgi.pscatalog.service;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.cgi.pscatalog.service.dto.AddressesDTO;

/**
 * Service Interface for managing Addresses.
 */
public interface AddressesService {

    /**
     * Save a addresses.
     *
     * @param addressesDTO the entity to save
     * @return the persisted entity
     */
    AddressesDTO save(AddressesDTO addressesDTO);

    /**
     * Get all the addresses.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<AddressesDTO> findAll(Pageable pageable);


    /**
     * Get the "id" addresses.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<AddressesDTO> findOne(Long id);

    /**
     * Delete the "id" addresses.
     *
     * @param id the id of the entity
     */
    void delete(Long id);

    /**
     * Search for the addresses corresponding to the query.
     *
     * @param query the query of the search
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<AddressesDTO> search(String query, Pageable pageable);

	Page<AddressesDTO> getAddressesByLogin(String login, Pageable pageable);

	Page<AddressesDTO> getAddressesByLoginAndCustomerId(String login, Long customerId, Pageable pageable);

}
