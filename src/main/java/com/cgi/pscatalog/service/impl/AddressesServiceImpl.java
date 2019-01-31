package com.cgi.pscatalog.service.impl;

import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cgi.pscatalog.domain.Addresses;
import com.cgi.pscatalog.repository.AddressesRepository;
import com.cgi.pscatalog.repository.search.AddressesSearchRepository;
import com.cgi.pscatalog.service.AddressesService;
import com.cgi.pscatalog.service.dto.AddressesDTO;
import com.cgi.pscatalog.service.mapper.AddressesMapper;

/**
 * Service Implementation for managing Addresses.
 */
@Service
@Transactional
public class AddressesServiceImpl implements AddressesService {

    private final Logger log = LoggerFactory.getLogger(AddressesServiceImpl.class);

    private final AddressesRepository addressesRepository;

    private final AddressesMapper addressesMapper;

    private final AddressesSearchRepository addressesSearchRepository;

    public AddressesServiceImpl(AddressesRepository addressesRepository, AddressesMapper addressesMapper, AddressesSearchRepository addressesSearchRepository) {
        this.addressesRepository = addressesRepository;
        this.addressesMapper = addressesMapper;
        this.addressesSearchRepository = addressesSearchRepository;
    }

    /**
     * Save a addresses.
     *
     * @param addressesDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public AddressesDTO save(AddressesDTO addressesDTO) {
        log.debug("Request to save Addresses : {}", addressesDTO);

        Addresses addresses = addressesMapper.toEntity(addressesDTO);
        addresses = addressesRepository.save(addresses);
        AddressesDTO result = addressesMapper.toDto(addresses);
        addressesSearchRepository.save(addresses);
        return result;
    }

    /**
     * Get all the addresses.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<AddressesDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Addresses");
        return addressesRepository.findAll(pageable)
            .map(addressesMapper::toDto);
    }


    /**
     * Get one addresses by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<AddressesDTO> findOne(Long id) {
        log.debug("Request to get Addresses : {}", id);
        return addressesRepository.findById(id)
            .map(addressesMapper::toDto);
    }

    /**
     * Delete the addresses by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Addresses : {}", id);
        addressesRepository.deleteById(id);
        addressesSearchRepository.deleteById(id);
    }

    /**
     * Search for the addresses corresponding to the query.
     *
     * @param query the query of the search
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<AddressesDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Addresses for query {}", query);
        return addressesSearchRepository.search(queryStringQuery(query), pageable)
            .map(addressesMapper::toDto);
    }

	@Override
	@Transactional(readOnly = true)
	public Page<AddressesDTO> getAddressesByLogin(String login, Pageable pageable) {
        log.debug("Request to get all Addresses by login {}", login);
        return addressesRepository.findAllByLogin(login, pageable)
            .map(addressesMapper::toDto);
	}

	@Override
	@Transactional(readOnly = true)
	public Page<AddressesDTO> getAddressesByLoginAndCustomerId(String login, Long customerId, Pageable pageable) {
        log.debug("Request to get all Addresses by login {} and customer id {}", login, customerId);
        return addressesRepository.findAllByLoginAndCustomerId(login, customerId, pageable)
            .map(addressesMapper::toDto);
	}
}
