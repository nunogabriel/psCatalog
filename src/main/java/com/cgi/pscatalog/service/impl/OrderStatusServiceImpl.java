package com.cgi.pscatalog.service.impl;

import com.cgi.pscatalog.service.OrderStatusService;
import com.cgi.pscatalog.domain.OrderStatus;
import com.cgi.pscatalog.repository.OrderStatusRepository;
import com.cgi.pscatalog.repository.search.OrderStatusSearchRepository;
import com.cgi.pscatalog.service.dto.OrderStatusDTO;
import com.cgi.pscatalog.service.mapper.OrderStatusMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing OrderStatus.
 */
@Service
@Transactional
public class OrderStatusServiceImpl implements OrderStatusService {

    private final Logger log = LoggerFactory.getLogger(OrderStatusServiceImpl.class);

    private final OrderStatusRepository orderStatusRepository;

    private final OrderStatusMapper orderStatusMapper;

    private final OrderStatusSearchRepository orderStatusSearchRepository;

    public OrderStatusServiceImpl(OrderStatusRepository orderStatusRepository, OrderStatusMapper orderStatusMapper, OrderStatusSearchRepository orderStatusSearchRepository) {
        this.orderStatusRepository = orderStatusRepository;
        this.orderStatusMapper = orderStatusMapper;
        this.orderStatusSearchRepository = orderStatusSearchRepository;
    }

    /**
     * Save a orderStatus.
     *
     * @param orderStatusDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public OrderStatusDTO save(OrderStatusDTO orderStatusDTO) {
        log.debug("Request to save OrderStatus : {}", orderStatusDTO);

        OrderStatus orderStatus = orderStatusMapper.toEntity(orderStatusDTO);
        orderStatus = orderStatusRepository.save(orderStatus);
        OrderStatusDTO result = orderStatusMapper.toDto(orderStatus);
        orderStatusSearchRepository.save(orderStatus);
        return result;
    }

    /**
     * Get all the orderStatuses.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<OrderStatusDTO> findAll(Pageable pageable) {
        log.debug("Request to get all OrderStatuses");
        return orderStatusRepository.findAll(pageable)
            .map(orderStatusMapper::toDto);
    }


    /**
     * Get one orderStatus by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<OrderStatusDTO> findOne(Long id) {
        log.debug("Request to get OrderStatus : {}", id);
        return orderStatusRepository.findById(id)
            .map(orderStatusMapper::toDto);
    }

    /**
     * Delete the orderStatus by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete OrderStatus : {}", id);
        orderStatusRepository.deleteById(id);
        orderStatusSearchRepository.deleteById(id);
    }

    /**
     * Search for the orderStatus corresponding to the query.
     *
     * @param query the query of the search
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<OrderStatusDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of OrderStatuses for query {}", query);
        return orderStatusSearchRepository.search(queryStringQuery(query), pageable)
            .map(orderStatusMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
	public Optional<OrderStatusDTO> getOrderStatusByDescription(String orderStatusDescription) {
        log.debug("Request to get OrderStatus by description: {}", orderStatusDescription);
        return orderStatusRepository.findOneByOrderStatusDescription(orderStatusDescription)
            .map(orderStatusMapper::toDto);
	}
}
