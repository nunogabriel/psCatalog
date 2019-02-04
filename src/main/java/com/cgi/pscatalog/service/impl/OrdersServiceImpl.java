package com.cgi.pscatalog.service.impl;

import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cgi.pscatalog.domain.Orders;
import com.cgi.pscatalog.repository.OrdersRepository;
import com.cgi.pscatalog.repository.search.OrdersSearchRepository;
import com.cgi.pscatalog.service.OrdersService;
import com.cgi.pscatalog.service.dto.OrdersDTO;
import com.cgi.pscatalog.service.mapper.OrdersMapper;

/**
 * Service Implementation for managing Orders.
 */
@Service
@Transactional
public class OrdersServiceImpl implements OrdersService {

    private final Logger log = LoggerFactory.getLogger(OrdersServiceImpl.class);

    private final OrdersRepository ordersRepository;

    private final OrdersMapper ordersMapper;

    private final OrdersSearchRepository ordersSearchRepository;

    public OrdersServiceImpl(OrdersRepository ordersRepository, OrdersMapper ordersMapper, OrdersSearchRepository ordersSearchRepository) {
        this.ordersRepository = ordersRepository;
        this.ordersMapper = ordersMapper;
        this.ordersSearchRepository = ordersSearchRepository;
    }

    /**
     * Save a orders.
     *
     * @param ordersDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public OrdersDTO save(OrdersDTO ordersDTO) {
        log.debug("Request to save Orders : {}", ordersDTO);

        Orders orders = ordersMapper.toEntity(ordersDTO);
        orders = ordersRepository.save(orders);
        OrdersDTO result = ordersMapper.toDto(orders);
        ordersSearchRepository.save(orders);
        return result;
    }

    /**
     * Get all the orders.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<OrdersDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Orders");
        return ordersRepository.findAll(pageable)
            .map(ordersMapper::toDto);
    }


    /**
     * Get one orders by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<OrdersDTO> findOne(Long id) {
        log.debug("Request to get Orders : {}", id);
        return ordersRepository.findById(id)
            .map(ordersMapper::toDto);
    }

    /**
     * Delete the orders by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Orders : {}", id);
        ordersRepository.deleteById(id);
        ordersSearchRepository.deleteById(id);
    }

    /**
     * Search for the orders corresponding to the query.
     *
     * @param query the query of the search
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<OrdersDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Orders for query {}", query);
        return ordersSearchRepository.search(queryStringQuery(query), pageable)
            .map(ordersMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
	public Page<OrdersDTO> getAllByLoginAndOrderStatusPending(String login, Pageable pageable) {
        log.debug("Request to get all Pending Orders By Login: {}", login);
        return ordersRepository.findAllByLoginAndOrderStatusPending(login, pageable)
            .map(ordersMapper::toDto);
	}

	@Override
	@Transactional(readOnly = true)
	public Page<OrdersDTO> getAllByLoginAndOrderStatus(String login, Pageable pageable) {
        log.debug("Request to get all Orders by Login: {} and Order Status", login);
        return ordersRepository.findAllByLoginAndOrderStatus(login, pageable)
                .map(ordersMapper::toDto);
	}

    @Override
    @Transactional(readOnly = true)
	public Page<OrdersDTO> getAllByLoginAndCustomerIdAndOrderStatusPending(String login, Long customerId, Pageable pageable) {
        log.debug("Request to get all Pending Orders By Login: {} and CustomerId: {}", login, customerId);
        return ordersRepository.findAllByLoginAndCustomerIdAndOrderStatusPending(login, customerId, pageable)
            .map(ordersMapper::toDto);
	}

	@Override
	@Transactional(readOnly = true)
	public Page<OrdersDTO> getAllByLoginAndCustomerIdAndOrderStatus(String login, Long customerId, Pageable pageable) {
        log.debug("Request to get all Orders by Login: {} and CustomerId: {} and Order Status", login, customerId);
        return ordersRepository.findAllByLoginAndCustomerIdAndOrderStatus(login, customerId, pageable)
                .map(ordersMapper::toDto);
	}

}
