package com.cgi.pscatalog.service.impl;

import com.cgi.pscatalog.service.OrderDetHstService;
import com.cgi.pscatalog.domain.OrderDetHst;
import com.cgi.pscatalog.repository.OrderDetHstRepository;
import com.cgi.pscatalog.repository.search.OrderDetHstSearchRepository;
import com.cgi.pscatalog.service.dto.OrderDetHstDTO;
import com.cgi.pscatalog.service.mapper.OrderDetHstMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing OrderDetHst.
 */
@Service
@Transactional
public class OrderDetHstServiceImpl implements OrderDetHstService {

    private final Logger log = LoggerFactory.getLogger(OrderDetHstServiceImpl.class);

    private final OrderDetHstRepository orderDetHstRepository;

    private final OrderDetHstMapper orderDetHstMapper;

    private final OrderDetHstSearchRepository orderDetHstSearchRepository;

    public OrderDetHstServiceImpl(OrderDetHstRepository orderDetHstRepository, OrderDetHstMapper orderDetHstMapper, OrderDetHstSearchRepository orderDetHstSearchRepository) {
        this.orderDetHstRepository = orderDetHstRepository;
        this.orderDetHstMapper = orderDetHstMapper;
        this.orderDetHstSearchRepository = orderDetHstSearchRepository;
    }

    /**
     * Save a orderDetHst.
     *
     * @param orderDetHstDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public OrderDetHstDTO save(OrderDetHstDTO orderDetHstDTO) {
        log.debug("Request to save OrderDetHst : {}", orderDetHstDTO);

        OrderDetHst orderDetHst = orderDetHstMapper.toEntity(orderDetHstDTO);
        orderDetHst = orderDetHstRepository.save(orderDetHst);
        OrderDetHstDTO result = orderDetHstMapper.toDto(orderDetHst);
        orderDetHstSearchRepository.save(orderDetHst);
        return result;
    }

    /**
     * Get all the orderDetHsts.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<OrderDetHstDTO> findAll(Pageable pageable) {
        log.debug("Request to get all OrderDetHsts");
        return orderDetHstRepository.findAll(pageable)
            .map(orderDetHstMapper::toDto);
    }


    /**
     * Get one orderDetHst by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<OrderDetHstDTO> findOne(Long id) {
        log.debug("Request to get OrderDetHst : {}", id);
        return orderDetHstRepository.findById(id)
            .map(orderDetHstMapper::toDto);
    }

    /**
     * Delete the orderDetHst by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete OrderDetHst : {}", id);
        orderDetHstRepository.deleteById(id);
        orderDetHstSearchRepository.deleteById(id);
    }

    /**
     * Search for the orderDetHst corresponding to the query.
     *
     * @param query the query of the search
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<OrderDetHstDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of OrderDetHsts for query {}", query);
        return orderDetHstSearchRepository.search(queryStringQuery(query), pageable)
            .map(orderDetHstMapper::toDto);
    }
}
