package com.cgi.pscatalog.web.rest;

import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

import com.cgi.pscatalog.service.CustomerOrdersService;
import com.cgi.pscatalog.service.dto.CustomerOrdersDTO;
import com.cgi.pscatalog.service.dto.OrdersDTO;
import com.cgi.pscatalog.service.util.PageDataUtil;
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

    private final CustomerOrdersService customerOrdersService;

    public CustomerOrdersResource(CustomerOrdersService customerOrdersService) {
        this.customerOrdersService = customerOrdersService;
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

        // Update customer order
        customerOrdersService.updateCustomerOrders(customerOrdersDTO);

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

        // Get all customer orders
        PageDataUtil<OrdersDTO, CustomerOrdersDTO> pageDataUtil = customerOrdersService.getAllCustomerOrders(pageable, ENTITY_NAME);

        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(pageDataUtil.getPageInformation(), "/api/customer-orders");

        return ResponseEntity.ok().headers(headers).body(pageDataUtil.getContent());
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

        // Get customer orders by id
        Optional<CustomerOrdersDTO> customerOrdersDTOopt = customerOrdersService.getCustomerOrders(id);

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

        // Search customer orders
        PageDataUtil<OrdersDTO, CustomerOrdersDTO> pageDataUtil = customerOrdersService.searchCustomerOrders(query, pageable);

        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, pageDataUtil.getPageInformation(), "/api/_search/customer-orders");

        return new ResponseEntity<>(pageDataUtil.getContent(), headers, HttpStatus.OK);
    }

}