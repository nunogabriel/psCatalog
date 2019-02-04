package com.cgi.pscatalog.web.rest;

import java.net.URI;
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
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.cgi.pscatalog.service.CustomerAddressesService;
import com.cgi.pscatalog.service.dto.AddressesDTO;
import com.cgi.pscatalog.service.dto.CustomerAddressesDTO;
import com.cgi.pscatalog.service.util.PageDataUtil;
import com.cgi.pscatalog.web.rest.errors.BadRequestAlertException;
import com.cgi.pscatalog.web.rest.util.HeaderUtil;
import com.cgi.pscatalog.web.rest.util.PaginationUtil;
import com.codahale.metrics.annotation.Timed;

import io.github.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing Addresses.
 */
@RestController
@RequestMapping("/api")
public class CustomerAddressesResource {

    private final Logger log = LoggerFactory.getLogger(CustomerAddressesResource.class);

    private static final String ENTITY_NAME = "customerAddresses";

    private final CustomerAddressesService customerAddressesService;

    public CustomerAddressesResource(CustomerAddressesService customerAddressesService) {
        this.customerAddressesService = customerAddressesService;
    }

    /**
     * POST  /customerAddresses : Create a new customer addresses.
     *
     * @param customerAddressesDTO the customerAddressesDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new customerAddressesDTO, or with status 400 (Bad Request) if the customerAddresses has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/customerAddresses")
    @Timed
    public ResponseEntity<CustomerAddressesDTO> createCustomerAddresses(@Valid @RequestBody CustomerAddressesDTO customerAddressesDTO) throws URISyntaxException {
        log.debug("REST request to createCustomerAddresses : {}", customerAddressesDTO);

        if (customerAddressesDTO.getId() != null) {
            throw new BadRequestAlertException("A new customer address cannot already have an ID", ENTITY_NAME, "idexists");
        }

        // Create customer address
        customerAddressesDTO = customerAddressesService.createCustomerAddresses(customerAddressesDTO, ENTITY_NAME);

        return ResponseEntity.created(new URI("/api/customerAddresses/" + customerAddressesDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, customerAddressesDTO.getAddressReference()))
            .body(customerAddressesDTO);
    }

    /**
     * PUT  /customerAddresses : Updates an existing customer addresses.
     *
     * @param customerAddressesDTO the customerAddressesDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated customerAddressesDTO,
     * or with status 400 (Bad Request) if the customerAddressesDTO is not valid,
     * or with status 500 (Internal Server Error) if the customerAddressesDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/customerAddresses")
    @Timed
    public ResponseEntity<CustomerAddressesDTO> updateCustomerAddresses(@Valid @RequestBody CustomerAddressesDTO customerAddressesDTO) throws URISyntaxException {
        log.debug("REST request to update Customer Addresses : {}", customerAddressesDTO);

        if (customerAddressesDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }

        // Update customer address
        customerAddressesService.updateCustomerAddresses(customerAddressesDTO);

        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, customerAddressesDTO.getAddressReference()))
            .body(customerAddressesDTO);
    }

    /**
     * GET  /customerAddresses : get all the customer addresses.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of customerAddresses in body
     */
    @GetMapping("/customerAddresses")
    @Timed
    public ResponseEntity<List<CustomerAddressesDTO>> getAllCustomerAddresses(Pageable pageable) {
        log.debug("REST request to get a page of Customer Addresses");

        // Get all customer addresses
        PageDataUtil<AddressesDTO, CustomerAddressesDTO> pageDataUtil = customerAddressesService.getAllCustomerAddresses(pageable, ENTITY_NAME);

        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(pageDataUtil.getPageInformation(), "/api/customerAddresses");

        return ResponseEntity.ok().headers(headers).body(pageDataUtil.getContent());
    }

    /**
     * GET  /customerAddresses/:id : get the "id" customer addresses.
     *
     * @param id the id of the customerAddressesDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the customerAddressesDTO, or with status 404 (Not Found)
     */
    @GetMapping("/customerAddresses/{id}")
    @Timed
    public ResponseEntity<CustomerAddressesDTO> getCustomerAddresses(@PathVariable Long id) {
        log.debug("REST request to get Customer Addresses : {}", id);

        // Get customer addresses by id
        Optional<CustomerAddressesDTO> customerAddressesDTOopt = customerAddressesService.getCustomerAddresses(id);

        return ResponseUtil.wrapOrNotFound(customerAddressesDTOopt);
    }

    /**
     * DELETE  /customerAddresses/:id : delete the "id" customerAddresses.
     *
     * @param id the id of the customerAddressesDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/customerAddresses/{id}")
    @Timed
    public ResponseEntity<Void> deleteCustomerAddresses(@PathVariable Long id) {
        log.debug("REST request to delete Customer Addresses : {}", id);

        // Delete addresses = Update address_end_date
        customerAddressesService.deleteCustomerAddresses(null, ENTITY_NAME);

        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, "")).build();
    }

    /**
     * SEARCH  /_search/customerAddresses?query=:query : search for the customer addresses corresponding
     * to the query.
     *
     * @param query the query of the customerAddresses search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/customerAddresses")
    @Timed
    public ResponseEntity<List<CustomerAddressesDTO>> searchCustomerAddresses(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of Customer Addresses for query {}", query);

        // Search customer addresses
        PageDataUtil<AddressesDTO, CustomerAddressesDTO> pageDataUtil = customerAddressesService.searchCustomerAddresses(query, pageable);

        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, pageDataUtil.getPageInformation(), "/api/_search/customerAddresses");

        return new ResponseEntity<>(pageDataUtil.getContent(), headers, HttpStatus.OK);
    }
}
