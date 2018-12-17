package com.cgi.pscatalog.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.cgi.pscatalog.security.SecurityUtils;
import com.cgi.pscatalog.service.AddressesService;
import com.cgi.pscatalog.web.rest.errors.BadRequestAlertException;
import com.cgi.pscatalog.web.rest.util.HeaderUtil;
import com.cgi.pscatalog.web.rest.util.PaginationUtil;
import com.cgi.pscatalog.service.dto.AddressesDTO;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing Addresses.
 */
@RestController
@RequestMapping("/api")
public class AddressesResource {

    private final Logger log = LoggerFactory.getLogger(AddressesResource.class);

    private static final String ENTITY_NAME = "addresses";

    private final AddressesService addressesService;

    public AddressesResource(AddressesService addressesService) {
        this.addressesService = addressesService;
    }

    /**
     * POST  /addresses : Create a new addresses.
     *
     * @param addressesDTO the addressesDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new addressesDTO, or with status 400 (Bad Request) if the addresses has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/addresses")
    @Timed
    public ResponseEntity<AddressesDTO> createAddresses(@Valid @RequestBody AddressesDTO addressesDTO) throws URISyntaxException {
        log.debug("REST request to save Addresses : {}", addressesDTO);
        
        if (addressesDTO.getId() != null) {
            throw new BadRequestAlertException("A new addresses cannot already have an ID", ENTITY_NAME, "idexists");
        }
        
        addressesDTO.setCreatedBy((SecurityUtils.getCurrentUserLogin().isPresent())?(SecurityUtils.getCurrentUserLogin().get()):"anonymousUser");
        addressesDTO.setCreatedDate(Instant.now());
        
        AddressesDTO result = addressesService.save(addressesDTO);
        
        return ResponseEntity.created(new URI("/api/addresses/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /addresses : Updates an existing addresses.
     *
     * @param addressesDTO the addressesDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated addressesDTO,
     * or with status 400 (Bad Request) if the addressesDTO is not valid,
     * or with status 500 (Internal Server Error) if the addressesDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/addresses")
    @Timed
    public ResponseEntity<AddressesDTO> updateAddresses(@Valid @RequestBody AddressesDTO addressesDTO) throws URISyntaxException {
        log.debug("REST request to update Addresses : {}", addressesDTO);
        
        if (addressesDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        
        addressesDTO.setLastModifiedBy((SecurityUtils.getCurrentUserLogin().isPresent())?(SecurityUtils.getCurrentUserLogin().get()):"anonymousUser");
        addressesDTO.setLastModifiedDate(Instant.now());
        
        AddressesDTO result = addressesService.save(addressesDTO);
        
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, addressesDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /addresses : get all the addresses.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of addresses in body
     */
    @GetMapping("/addresses")
    @Timed
    public ResponseEntity<List<AddressesDTO>> getAllAddresses(Pageable pageable) {
        log.debug("REST request to get a page of Addresses");
        Page<AddressesDTO> page = addressesService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/addresses");
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }
    
    /**
     * GET  /addresses/:id : get the "id" addresses.
     *
     * @param id the id of the addressesDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the addressesDTO, or with status 404 (Not Found)
     */
    @GetMapping("/addresses/{id}")
    @Timed
    public ResponseEntity<AddressesDTO> getAddresses(@PathVariable Long id) {
        log.debug("REST request to get Addresses : {}", id);
        Optional<AddressesDTO> addressesDTO = addressesService.findOne(id);
        return ResponseUtil.wrapOrNotFound(addressesDTO);
    }

    /**
     * DELETE  /addresses/:id : delete the "id" addresses.
     *
     * @param id the id of the addressesDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/addresses/{id}")
    @Timed
    public ResponseEntity<Void> deleteAddresses(@PathVariable Long id) {
        log.debug("REST request to delete Addresses : {}", id);
        addressesService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/addresses?query=:query : search for the addresses corresponding
     * to the query.
     *
     * @param query the query of the addresses search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/addresses")
    @Timed
    public ResponseEntity<List<AddressesDTO>> searchAddresses(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of Addresses for query {}", query);
        Page<AddressesDTO> page = addressesService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/addresses");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /addresses/customer/:id : get all the addresses.
     *
     * @param id the id of the customer to retrieve
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and with body the addressesDTO, or with status 404 (Not Found)
     */
    @GetMapping("/addresses/customer/{id}")
    @Timed
    public ResponseEntity<List<AddressesDTO>> getAddressesByCustomerId(@PathVariable Long customerId, Pageable pageable) {
        log.debug("REST request to get a page of Addresses by Customer Id {}", customerId);
        Page<AddressesDTO> page = addressesService.getAddressesByCustomerId(customerId, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/addresses");
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

}
