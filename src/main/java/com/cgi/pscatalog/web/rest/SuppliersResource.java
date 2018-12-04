package com.cgi.pscatalog.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
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

import com.cgi.pscatalog.security.SecurityUtils;
import com.cgi.pscatalog.service.SuppliersService;
import com.cgi.pscatalog.service.dto.SuppliersDTO;
import com.cgi.pscatalog.web.rest.errors.BadRequestAlertException;
import com.cgi.pscatalog.web.rest.util.HeaderUtil;
import com.cgi.pscatalog.web.rest.util.PaginationUtil;
import com.codahale.metrics.annotation.Timed;

import io.github.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing Suppliers.
 */
@RestController
@RequestMapping("/api")
public class SuppliersResource {

    private final Logger log = LoggerFactory.getLogger(SuppliersResource.class);

    private static final String ENTITY_NAME = "suppliers";

    private final SuppliersService suppliersService;

    public SuppliersResource(SuppliersService suppliersService) {
        this.suppliersService = suppliersService;
    }

    /**
     * POST  /suppliers : Create a new suppliers.
     *
     * @param suppliersDTO the suppliersDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new suppliersDTO, or with status 400 (Bad Request) if the suppliers has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/suppliers")
    @Timed
    public ResponseEntity<SuppliersDTO> createSuppliers(@Valid @RequestBody SuppliersDTO suppliersDTO) throws URISyntaxException {
        log.debug("REST request to save Suppliers : {}", suppliersDTO);
        
        if (suppliersDTO.getId() != null) {
            throw new BadRequestAlertException("A new suppliers cannot already have an ID", ENTITY_NAME, "idexists");
        }
        
        suppliersDTO.setCreatedBy(SecurityUtils.getCurrentUserLogin().toString());
        suppliersDTO.setCreatedDate(Instant.now());
        
        SuppliersDTO result = suppliersService.save(suppliersDTO);
        
        return ResponseEntity.created(new URI("/api/suppliers/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /suppliers : Updates an existing suppliers.
     *
     * @param suppliersDTO the suppliersDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated suppliersDTO,
     * or with status 400 (Bad Request) if the suppliersDTO is not valid,
     * or with status 500 (Internal Server Error) if the suppliersDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/suppliers")
    @Timed
    public ResponseEntity<SuppliersDTO> updateSuppliers(@Valid @RequestBody SuppliersDTO suppliersDTO) throws URISyntaxException {
        log.debug("REST request to update Suppliers : {}", suppliersDTO);
        
        if (suppliersDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        
        suppliersDTO.setLastModifiedBy(SecurityUtils.getCurrentUserLogin().toString());
        suppliersDTO.setLastModifiedDate(Instant.now());
        
        SuppliersDTO result = suppliersService.save(suppliersDTO);
        
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, suppliersDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /suppliers : get all the suppliers.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of suppliers in body
     */
    @GetMapping("/suppliers")
    @Timed
    public ResponseEntity<List<SuppliersDTO>> getAllSuppliers(Pageable pageable) {
        log.debug("REST request to get a page of Suppliers");
        Page<SuppliersDTO> page = suppliersService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/suppliers");
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * GET  /suppliers/:id : get the "id" suppliers.
     *
     * @param id the id of the suppliersDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the suppliersDTO, or with status 404 (Not Found)
     */
    @GetMapping("/suppliers/{id}")
    @Timed
    public ResponseEntity<SuppliersDTO> getSuppliers(@PathVariable Long id) {
        log.debug("REST request to get Suppliers : {}", id);
        Optional<SuppliersDTO> suppliersDTO = suppliersService.findOne(id);
        return ResponseUtil.wrapOrNotFound(suppliersDTO);
    }

    /**
     * DELETE  /suppliers/:id : delete the "id" suppliers.
     *
     * @param id the id of the suppliersDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/suppliers/{id}")
    @Timed
    public ResponseEntity<Void> deleteSuppliers(@PathVariable Long id) {
        log.debug("REST request to delete Suppliers : {}", id);
        suppliersService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/suppliers?query=:query : search for the suppliers corresponding
     * to the query.
     *
     * @param query the query of the suppliers search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/suppliers")
    @Timed
    public ResponseEntity<List<SuppliersDTO>> searchSuppliers(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of Suppliers for query {}", query);
        Page<SuppliersDTO> page = suppliersService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/suppliers");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

}
