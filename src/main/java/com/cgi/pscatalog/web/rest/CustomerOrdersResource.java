package com.cgi.pscatalog.web.rest;

import java.net.URISyntaxException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.cgi.pscatalog.security.SecurityUtils;
import com.cgi.pscatalog.service.OrdersService;
import com.cgi.pscatalog.service.dto.CustomerOrdersDTO;
import com.cgi.pscatalog.service.dto.OrdersDTO;
import com.cgi.pscatalog.web.rest.errors.BadRequestAlertException;
import com.cgi.pscatalog.web.rest.util.HeaderUtil;
import com.cgi.pscatalog.web.rest.util.PaginationUtil;
import com.codahale.metrics.annotation.Timed;

import io.github.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing Orders.
 */
@RestController
@RequestMapping("/api")
public class CustomerOrdersResource {

    private final Logger log = LoggerFactory.getLogger(CustomerOrdersResource.class);

    private static final String ENTITY_NAME = "customerOrders";

    private final OrdersService ordersService;

    public CustomerOrdersResource(OrdersService ordersService) {
        this.ordersService = ordersService;
    }

    /**
     * PUT  /customer-orders : Updates an existing customer orders.
     *
     * @param CustomerOrdersDTO the CustomerOrdersDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated ordersDTO,
     * or with status 400 (Bad Request) if the CustomerOrdersDTO is not valid,
     * or with status 500 (Internal Server Error) if the ordersDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/customer-orders")
    @Timed
    public ResponseEntity<CustomerOrdersDTO> updateCustomerOrders(@Valid @RequestBody CustomerOrdersDTO customerOrdersDTO) throws URISyntaxException {
        log.debug("REST request to update Customer Orders : {}", customerOrdersDTO);

        if (customerOrdersDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }

        Optional<OrdersDTO> ordersDTOOpt = ordersService.findOne(customerOrdersDTO.getId());

		if (ordersDTOOpt.isPresent()) {
			OrdersDTO ordersDTO = ordersDTOOpt.get();

			ordersDTO.setOrderStatusId(customerOrdersDTO.getOrderStatusId());
			ordersDTO.setDeliveryDate(customerOrdersDTO.getDeliveryDate());
			ordersDTO.setLastModifiedBy((SecurityUtils.getCurrentUserLogin().isPresent())?(SecurityUtils.getCurrentUserLogin().get()):"anonymousUser");
			ordersDTO.setLastModifiedDate(Instant.now());

			ordersService.save(ordersDTO);
		}

        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, customerOrdersDTO.getOrderReference()))
            .body(customerOrdersDTO);
    }

    /**
     * GET  /customer-orders : get all the customer orders.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of orders in body
     */
    @GetMapping("/customer-orders")
    @Timed
    public ResponseEntity<List<CustomerOrdersDTO>> getAllCustomerOrders(Pageable pageable) {
        log.debug("REST request to get a page of Customer Orders");

        Page<OrdersDTO> page = ordersService.getAllByLoginAndOrderStatus(SecurityUtils.getCurrentUserLogin().get(), pageable);

        List<OrdersDTO> listOrdersDTO = page.getContent();
		List<CustomerOrdersDTO> listCustomerOrdersDTO = new ArrayList<CustomerOrdersDTO>();

		for (Iterator<OrdersDTO> iterator = listOrdersDTO.iterator(); iterator.hasNext();) {
			OrdersDTO ordersDTO = iterator.next();

			CustomerOrdersDTO customerOrdersDTO = new CustomerOrdersDTO();
			customerOrdersDTO.setId(ordersDTO.getId());
			customerOrdersDTO.setAddressAddressReference(ordersDTO.getAddressAddressReference());
			customerOrdersDTO.setAddressId(ordersDTO.getAddressId());
			customerOrdersDTO.setCustomerCustomerName(ordersDTO.getCustomerCustomerName());
			customerOrdersDTO.setCustomerId(ordersDTO.getCustomerId());
			customerOrdersDTO.setDeliveryAddressAddressReference(ordersDTO.getDeliveryAddressAddressReference());
			customerOrdersDTO.setDeliveryAddressId(ordersDTO.getDeliveryAddressId());
			customerOrdersDTO.setCreatedDate(ordersDTO.getCreatedDate());
			customerOrdersDTO.setDeliveryDate(ordersDTO.getDeliveryDate());
			customerOrdersDTO.setOrderDate(ordersDTO.getOrderDate());
			customerOrdersDTO.setOrderReference(ordersDTO.getOrderReference());
			customerOrdersDTO.setOrderStatusId(ordersDTO.getOrderStatusId());
			customerOrdersDTO.setOrderStatusOrderStatusDescription(ordersDTO.getOrderStatusOrderStatusDescription());

			listCustomerOrdersDTO.add(customerOrdersDTO);
		}

		Page<CustomerOrdersDTO> pageAux = new PageImpl<CustomerOrdersDTO>(listCustomerOrdersDTO, pageable, listCustomerOrdersDTO.size());

        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(pageAux, "/api/customer-orders");

        return ResponseEntity.ok().headers(headers).body(pageAux.getContent());
    }

    /**
     * GET  /customer-orders/:id : get the "id" customer orders.
     *
     * @param id the id of the CustomerOrdersDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the CustomerOrdersDTO, or with status 404 (Not Found)
     */
    @GetMapping("/customer-orders/{id}")
    @Timed
    public ResponseEntity<CustomerOrdersDTO> getCustomerOrders(@PathVariable Long id) {
        log.debug("REST request to get Orders : {}", id);

        Optional<OrdersDTO> ordersDTOOpt = ordersService.findOne(id);

        Optional<CustomerOrdersDTO> customerOrdersDTOopt = Optional.empty();

		if (ordersDTOOpt.isPresent()) {
			OrdersDTO ordersDTO = ordersDTOOpt.get();

			CustomerOrdersDTO customerOrdersDTO = new CustomerOrdersDTO();
			customerOrdersDTO.setId(ordersDTO.getId());
			customerOrdersDTO.setAddressAddressReference(ordersDTO.getAddressAddressReference());
			customerOrdersDTO.setAddressId(ordersDTO.getAddressId());
			customerOrdersDTO.setCustomerCustomerName(ordersDTO.getCustomerCustomerName());
			customerOrdersDTO.setCustomerId(ordersDTO.getCustomerId());
			customerOrdersDTO.setDeliveryAddressAddressReference(ordersDTO.getDeliveryAddressAddressReference());
			customerOrdersDTO.setDeliveryAddressId(ordersDTO.getDeliveryAddressId());
			customerOrdersDTO.setCreatedDate(ordersDTO.getCreatedDate());
			customerOrdersDTO.setDeliveryDate(ordersDTO.getDeliveryDate());
			customerOrdersDTO.setOrderDate(ordersDTO.getOrderDate());
			customerOrdersDTO.setOrderReference(ordersDTO.getOrderReference());
			customerOrdersDTO.setOrderStatusId(ordersDTO.getOrderStatusId());
			customerOrdersDTO.setOrderStatusOrderStatusDescription(ordersDTO.getOrderStatusOrderStatusDescription());

			customerOrdersDTOopt = Optional.of(customerOrdersDTO);
		}

        return ResponseUtil.wrapOrNotFound(customerOrdersDTOopt);
    }

    /**
     * SEARCH  /_search/customer-orders?query=:query : search for the customer orders corresponding
     * to the query.
     *
     * @param query the query of the orders search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/customer-orders")
    @Timed
    public ResponseEntity<List<CustomerOrdersDTO>> searchCustomerOrders(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of Customer Orders for query {}", query);

        Page<OrdersDTO> page = ordersService.search(query, pageable);

        List<OrdersDTO> listOrdersDTO = page.getContent();
		List<CustomerOrdersDTO> listCustomerOrdersDTO = new ArrayList<CustomerOrdersDTO>();

		for (Iterator<OrdersDTO> iterator = listOrdersDTO.iterator(); iterator.hasNext();) {
			OrdersDTO ordersDTO = iterator.next();

			CustomerOrdersDTO customerOrdersDTO = new CustomerOrdersDTO();
			customerOrdersDTO.setId(ordersDTO.getId());
			customerOrdersDTO.setAddressAddressReference(ordersDTO.getAddressAddressReference());
			customerOrdersDTO.setAddressId(ordersDTO.getAddressId());
			customerOrdersDTO.setCustomerCustomerName(ordersDTO.getCustomerCustomerName());
			customerOrdersDTO.setCustomerId(ordersDTO.getCustomerId());
			customerOrdersDTO.setDeliveryAddressAddressReference(ordersDTO.getDeliveryAddressAddressReference());
			customerOrdersDTO.setDeliveryAddressId(ordersDTO.getDeliveryAddressId());
			customerOrdersDTO.setCreatedDate(ordersDTO.getCreatedDate());
			customerOrdersDTO.setDeliveryDate(ordersDTO.getDeliveryDate());
			customerOrdersDTO.setOrderDate(ordersDTO.getOrderDate());
			customerOrdersDTO.setOrderReference(ordersDTO.getOrderReference());
			customerOrdersDTO.setOrderStatusId(ordersDTO.getOrderStatusId());
			customerOrdersDTO.setOrderStatusOrderStatusDescription(ordersDTO.getOrderStatusOrderStatusDescription());

			listCustomerOrdersDTO.add(customerOrdersDTO);
		}

        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/customer-orders");

        return new ResponseEntity<>(listCustomerOrdersDTO, headers, HttpStatus.OK);
    }

}