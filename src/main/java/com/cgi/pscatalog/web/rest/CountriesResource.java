package com.cgi.pscatalog.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.cgi.pscatalog.service.CountriesService;
import com.cgi.pscatalog.web.rest.errors.BadRequestAlertException;
import com.cgi.pscatalog.web.rest.util.HeaderUtil;
import com.cgi.pscatalog.web.rest.util.PaginationUtil;
import com.cgi.pscatalog.service.dto.CountriesDTO;
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

import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing Countries.
 */
@RestController
@RequestMapping("/api")
public class CountriesResource {

    private final Logger log = LoggerFactory.getLogger(CountriesResource.class);

    private static final String ENTITY_NAME = "countries";

    private final CountriesService countriesService;

    public CountriesResource(CountriesService countriesService) {
        this.countriesService = countriesService;
    }

    /**
     * POST  /countries : Create a new countries.
     *
     * @param countriesDTO the countriesDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new countriesDTO, or with status 400 (Bad Request) if the countries has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/countries")
    @Timed
    public ResponseEntity<CountriesDTO> createCountries(@Valid @RequestBody CountriesDTO countriesDTO) throws URISyntaxException {
        log.debug("REST request to save Countries : {}", countriesDTO);
        if (countriesDTO.getId() != null) {
            throw new BadRequestAlertException("A new countries cannot already have an ID", ENTITY_NAME, "idexists");
        }
        CountriesDTO result = countriesService.save(countriesDTO);
        return ResponseEntity.created(new URI("/api/countries/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /countries : Updates an existing countries.
     *
     * @param countriesDTO the countriesDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated countriesDTO,
     * or with status 400 (Bad Request) if the countriesDTO is not valid,
     * or with status 500 (Internal Server Error) if the countriesDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/countries")
    @Timed
    public ResponseEntity<CountriesDTO> updateCountries(@Valid @RequestBody CountriesDTO countriesDTO) throws URISyntaxException {
        log.debug("REST request to update Countries : {}", countriesDTO);
        if (countriesDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        CountriesDTO result = countriesService.save(countriesDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, countriesDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /countries : get all the countries.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of countries in body
     */
    @GetMapping("/countries")
    @Timed
    public ResponseEntity<List<CountriesDTO>> getAllCountries(Pageable pageable) {
        log.debug("REST request to get a page of Countries");
        Page<CountriesDTO> page = countriesService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/countries");
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * GET  /countries/:id : get the "id" countries.
     *
     * @param id the id of the countriesDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the countriesDTO, or with status 404 (Not Found)
     */
    @GetMapping("/countries/{id}")
    @Timed
    public ResponseEntity<CountriesDTO> getCountries(@PathVariable Long id) {
        log.debug("REST request to get Countries : {}", id);
        Optional<CountriesDTO> countriesDTO = countriesService.findOne(id);
        return ResponseUtil.wrapOrNotFound(countriesDTO);
    }

    /**
     * DELETE  /countries/:id : delete the "id" countries.
     *
     * @param id the id of the countriesDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/countries/{id}")
    @Timed
    public ResponseEntity<Void> deleteCountries(@PathVariable Long id) {
        log.debug("REST request to delete Countries : {}", id);
        countriesService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/countries?query=:query : search for the countries corresponding
     * to the query.
     *
     * @param query the query of the countries search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/countries")
    @Timed
    public ResponseEntity<List<CountriesDTO>> searchCountries(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of Countries for query {}", query);
        Page<CountriesDTO> page = countriesService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/countries");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

}
