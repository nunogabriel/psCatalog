package com.cgi.pscatalog.web.rest;

import java.math.BigDecimal;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.cgi.pscatalog.service.CustomerOrderDetService;
import com.cgi.pscatalog.service.dto.CustomerOrdersDetDTO;
import com.cgi.pscatalog.service.dto.OrderDetDTO;
import com.cgi.pscatalog.service.util.PageDataUtil;
import com.cgi.pscatalog.web.rest.util.PaginationUtil;
import com.codahale.metrics.annotation.Timed;

import io.github.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing OrderDet.
 */
@RestController
@RequestMapping("/api")
public class CustomerOrdersDetResource {

    private final Logger log = LoggerFactory.getLogger(CustomerOrdersDetResource.class);

    private final CustomerOrderDetService customerOrderDetService;

    public CustomerOrdersDetResource(CustomerOrderDetService customerOrderDetService) {
        this.customerOrderDetService = customerOrderDetService;
    }

    /**
     * GET  /customer-orders-det : get all the orderDets.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of orderDets in body
     */
    @GetMapping("/customer-orders-det")
    @Timed
    public ResponseEntity<List<CustomerOrdersDetDTO>> getAllCustomerOrderDets(Pageable pageable) throws URISyntaxException {
        log.debug("REST request to get a page of CustomerOrderDets");

        // Get all customer order details
        PageDataUtil<OrderDetDTO, CustomerOrdersDetDTO> pageDataUtil = customerOrderDetService.getAllCustomerOrderDets(pageable);

        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(pageDataUtil.getPageInformation(), "/api/customer-orders-det");

        return ResponseEntity.ok().headers(headers).body(pageDataUtil.getContent());
    }

    /**
     * GET  /customer-orders-det/by-order-id : get all the orderDets by order id.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of orderDets in body
     */
    @GetMapping("/customer-orders-det/by-order-id")
    @Timed
    public ResponseEntity<List<CustomerOrdersDetDTO>> getAllCustomerOrderDetsByOrderId(@RequestParam Long orderId, Pageable pageable) throws URISyntaxException {
        log.debug("REST request to get a page of getAllCustomerOrderDetsByOrderId by order id");

        // Get all customer order details by order id
        PageDataUtil<OrderDetDTO, CustomerOrdersDetDTO> pageDataUtil = customerOrderDetService.getAllCustomerOrderDetsByOrderId(orderId, pageable);

        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(pageDataUtil.getPageInformation(), "/api/customer-orders-det");

        return ResponseEntity.ok().headers(headers).body(pageDataUtil.getContent());
    }

    /**
     * GET  /customer-orders-det/:id : get the "id" orderDet.
     *
     * @param id the id of the customerOrdersDetDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the customerOrdersDetDTO, or with status 404 (Not Found)
     */
    @GetMapping("/customer-orders-det/{id}")
    @Timed
    public ResponseEntity<CustomerOrdersDetDTO> getCustomerOrderDet(@PathVariable Long id) {
        log.debug("REST request to get CustomerOrderDet : {}", id);

       // Get customer order detail by id
        Optional<CustomerOrdersDetDTO> customerOrdersDetDTOOpt = customerOrderDetService.getCustomerOrderDet(id);

        return ResponseUtil.wrapOrNotFound(customerOrdersDetDTOOpt);
    }

    /**
     * GET  /customer-orders-det/orderTotal : get the "id" orderDet.
     *
     * @param id the id of the customerOrdersDetDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the customerOrdersDetDTO, or with status 404 (Not Found)
     */
    @GetMapping("/customer-orders-det/orderTotal")
    @Timed
    public BigDecimal getPendingOrderTotal() {
        log.debug("REST request to get getOrderTotalWithPromotions");

        // Get Pending order total
        return customerOrderDetService.getPendingOrderTotal();
    }

    /**
     * GET  /customer-orders-det/orderTotalByOrderId/:orderId : get the "id" orderDet.
     *
     * @param id the id of the customerOrdersDetDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the customerOrdersDetDTO, or with status 404 (Not Found)
     */
    @GetMapping("/customer-orders-det/orderTotalByOrderId/{orderId}")
    @Timed
    public BigDecimal getOrderTotalByOrderId(@PathVariable Long orderId) {
        log.debug("REST request to get getOrderTotalByOrderId");

        // Get customer order details by order id
	    return customerOrderDetService.getOrderTotalByOrderId(orderId);
    }

    /**
     * SEARCH  /_search/customer-orders-det?query=:query : search for the orderDet corresponding
     * to the query.
     *
     * @param query the query of the orderDet search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/customer-orders-det")
    @Timed
    public ResponseEntity<List<CustomerOrdersDetDTO>> searchCustomerOrdersDet(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of searchCustomerOrdersDet for query {}", query);

        // Search customer order details
        PageDataUtil<OrderDetDTO, CustomerOrdersDetDTO> pageDataUtil = customerOrderDetService.searchCustomerOrdersDet(query, pageable);

        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, pageDataUtil.getPageInformation(), "/api/_search/cat-order-det");

        return new ResponseEntity<>(pageDataUtil.getContent(), headers, HttpStatus.OK);
    }
}