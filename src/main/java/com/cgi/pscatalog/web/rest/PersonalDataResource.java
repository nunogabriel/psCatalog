package com.cgi.pscatalog.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
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
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.cgi.pscatalog.service.PersonalDataService;
import com.cgi.pscatalog.service.dto.CustomersDTO;
import com.cgi.pscatalog.service.dto.PersonalDataDTO;
import com.cgi.pscatalog.service.util.PageDataUtil;
import com.cgi.pscatalog.web.rest.errors.BadRequestAlertException;
import com.cgi.pscatalog.web.rest.util.HeaderUtil;
import com.cgi.pscatalog.web.rest.util.PaginationUtil;
import com.codahale.metrics.annotation.Timed;

import io.github.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing Personal Data.
 */
@RestController
@RequestMapping("/api")
public class PersonalDataResource {

    private final Logger log = LoggerFactory.getLogger(PersonalDataResource.class);

    private static final String ENTITY_NAME = "personalData";

    private final PersonalDataService personalDataService;

    public PersonalDataResource(PersonalDataService personalDataService) {
        this.personalDataService = personalDataService;
    }

    /**
     * POST  /personalData : Create a new personal data.
     *
     * @param personalDataDTO the personalDataDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new personalDataDTO, or with status 400 (Bad Request) if the customers has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/personalData")
    @Timed
    public ResponseEntity<PersonalDataDTO> createPersonalData(@Valid @RequestBody PersonalDataDTO personalDataDTO) throws URISyntaxException {
        log.debug("REST request to save personal data : {}", personalDataDTO);

        if (personalDataDTO.getId() != null) {
            throw new BadRequestAlertException("A new personal data cannot already have an ID", ENTITY_NAME, "idexists");
        }

        // Create personal data
        personalDataService.createPersonalData(personalDataDTO);

        return ResponseEntity.created(new URI("/api/personalData/"))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, ""))
            .body(personalDataDTO);
    }

    /**
     * PUT  /personalData : Updates an existing personal data.
     *
     * @param personalDataDTO the personalDataDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated personalDataDTO,
     * or with status 400 (Bad Request) if the personalDataDTO is not valid,
     * or with status 500 (Internal Server Error) if the personalDataDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/personalData")
    @Timed
    public ResponseEntity<PersonalDataDTO> updatePersonalData(@Valid @RequestBody PersonalDataDTO personalDataDTO) throws URISyntaxException {
        log.debug("REST request to update personal data : {}", personalDataDTO);

        if (personalDataDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }

        // Update personal data
        personalDataService.updatePersonalData(personalDataDTO);

        return ResponseEntity.ok()
	            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, ""))
	            .body(personalDataDTO);
    }

    /**
     * GET  /personalData : get all the personal data.
     *
     * @param pageable the pagination information
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many)
     * @return the ResponseEntity with status 200 (OK) and the list of customers in body
     */
    @GetMapping("/personalData")
    @Timed
    public ResponseEntity<List<PersonalDataDTO>> getAllPersonalData(Pageable pageable, @RequestParam(required = false, defaultValue = "false") boolean eagerload) {
        log.debug("REST request to get a page of personal data");

        // Get all personal data by login
        List<PersonalDataDTO> listPersonalDataDTO = personalDataService.getAllPersonalData(pageable);

		Page<PersonalDataDTO> page = new PageImpl<PersonalDataDTO>(listPersonalDataDTO, pageable, listPersonalDataDTO.size());

        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, String.format("/api/personalData?eagerload=%b", eagerload));

        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * GET  /personalData/:id : get the "id" personal data.
     *
     * @param id the id of the personalDataDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the personalDataDTO, or with status 404 (Not Found)
     */
    @GetMapping("/personalData/{id}")
    @Timed
    public ResponseEntity<PersonalDataDTO> getPersonalData(@PathVariable Long id) {
        log.debug("REST request to get personal data : {}", id);

        // Get personal data by id
        Optional<PersonalDataDTO> personalDataDTOOpt = personalDataService.getPersonalData(id);

        return ResponseUtil.wrapOrNotFound(personalDataDTOOpt);
    }

    /**
     * DELETE  /personalData/:id : delete the "id" personal data.
     *
     * @param id the id of the personalDataDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/personalData/{id}")
    @Timed
    public ResponseEntity<Void> deletePersonalData(@PathVariable Long id) {
        log.debug("REST request to delete Customers : {}", id);

        if ( id == null ) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }

        // Delete personal data
        personalDataService.deletePersonalData(id);

        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, "")).build();
    }

    /**
     * SEARCH  /_search/personalData?query=:query : search for the personal data corresponding
     * to the query.
     *
     * @param query the query of the personal data search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/personalData")
    @Timed
    public ResponseEntity<List<PersonalDataDTO>> searchPersonalData(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of personal data for query {}", query);

        // Search
        PageDataUtil<CustomersDTO, PersonalDataDTO> pageInformation = personalDataService.searchPersonalData(query, pageable);

        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, pageInformation.getPageInformation(), "/api/_search/personalData");

        return new ResponseEntity<>(pageInformation.getContent(), headers, HttpStatus.OK);
    }
}