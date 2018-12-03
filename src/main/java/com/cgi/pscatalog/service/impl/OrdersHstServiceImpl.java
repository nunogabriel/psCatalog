package com.cgi.pscatalog.service.impl;

import com.cgi.pscatalog.service.OrdersHstService;
import com.cgi.pscatalog.domain.OrdersHst;
import com.cgi.pscatalog.repository.OrdersHstRepository;
import com.cgi.pscatalog.repository.search.OrdersHstSearchRepository;
import com.cgi.pscatalog.service.dto.OrdersHstDTO;
import com.cgi.pscatalog.service.mapper.OrdersHstMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing OrdersHst.
 */
@Service
@Transactional
public class OrdersHstServiceImpl implements OrdersHstService {

    private final Logger log = LoggerFactory.getLogger(OrdersHstServiceImpl.class);

    private final OrdersHstRepository ordersHstRepository;

    private final OrdersHstMapper ordersHstMapper;

    private final OrdersHstSearchRepository ordersHstSearchRepository;

    public OrdersHstServiceImpl(OrdersHstRepository ordersHstRepository, OrdersHstMapper ordersHstMapper, OrdersHstSearchRepository ordersHstSearchRepository) {
        this.ordersHstRepository = ordersHstRepository;
        this.ordersHstMapper = ordersHstMapper;
        this.ordersHstSearchRepository = ordersHstSearchRepository;
    }

    /**
     * Save a ordersHst.
     *
     * @param ordersHstDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public OrdersHstDTO save(OrdersHstDTO ordersHstDTO) {
        log.debug("Request to save OrdersHst : {}", ordersHstDTO);

        OrdersHst ordersHst = ordersHstMapper.toEntity(ordersHstDTO);
        ordersHst = ordersHstRepository.save(ordersHst);
        OrdersHstDTO result = ordersHstMapper.toDto(ordersHst);
        ordersHstSearchRepository.save(ordersHst);
        return result;
    }

    /**
     * Get all the ordersHsts.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<OrdersHstDTO> findAll(Pageable pageable) {
        log.debug("Request to get all OrdersHsts");
        return ordersHstRepository.findAll(pageable)
            .map(ordersHstMapper::toDto);
    }


    /**
     * Get one ordersHst by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<OrdersHstDTO> findOne(Long id) {
        log.debug("Request to get OrdersHst : {}", id);
        return ordersHstRepository.findById(id)
            .map(ordersHstMapper::toDto);
    }

    /**
     * Delete the ordersHst by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete OrdersHst : {}", id);
        ordersHstRepository.deleteById(id);
        ordersHstSearchRepository.deleteById(id);
    }

    /**
     * Search for the ordersHst corresponding to the query.
     *
     * @param query the query of the search
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<OrdersHstDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of OrdersHsts for query {}", query);
        return ordersHstSearchRepository.search(queryStringQuery(query), pageable)
            .map(ordersHstMapper::toDto);
    }
}
