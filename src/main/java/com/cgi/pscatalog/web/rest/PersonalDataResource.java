package com.cgi.pscatalog.web.rest;

import java.net.URI;
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
import com.cgi.pscatalog.service.CustomersService;
import com.cgi.pscatalog.service.dto.CustomersDTO;
import com.cgi.pscatalog.service.dto.PersonalDataDTO;
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

    private final CustomersService customersService;

    public PersonalDataResource(CustomersService customersService) {
        this.customersService = customersService;
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

        CustomersDTO customersDTO = new CustomersDTO();
        customersDTO.setCustomerEmail(personalDataDTO.getCustomerEmail());
        customersDTO.setCustomerGender(personalDataDTO.getCustomerGender());
        customersDTO.setCustomerName(personalDataDTO.getCustomerName());
        customersDTO.setCustomerNif(personalDataDTO.getCustomerNif());
        customersDTO.setCustomerPhone(personalDataDTO.getCustomerPhone());
        customersDTO.setCustomerBeginDate(Instant.now());
        customersDTO.setCustomerEndDate(null);
        customersDTO.setLogin(SecurityUtils.getCurrentUserLogin().get());
        customersDTO.setCreatedBy((SecurityUtils.getCurrentUserLogin().isPresent())?(SecurityUtils.getCurrentUserLogin().get()):"anonymousUser");
        customersDTO.setCreatedDate(Instant.now());

        CustomersDTO customersDTOAux = customersService.save(customersDTO);

        personalDataDTO.setId(customersDTOAux.getId());

        return ResponseEntity.created(new URI("/api/personalData/" + personalDataDTO.getId()))
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

        Optional<CustomersDTO> customersDTOOpt = customersService.findOne(personalDataDTO.getId());

		if (customersDTOOpt.isPresent()) {
			CustomersDTO customersDTO = customersDTOOpt.get();

	        customersDTO.setCustomerEmail(personalDataDTO.getCustomerEmail());
	        customersDTO.setCustomerGender(personalDataDTO.getCustomerGender());
	        customersDTO.setCustomerName(personalDataDTO.getCustomerName());
	        customersDTO.setCustomerNif(personalDataDTO.getCustomerNif());
	        customersDTO.setCustomerPhone(personalDataDTO.getCustomerPhone());
	        customersDTO.setCustomerBeginDate(personalDataDTO.getCustomerBeginDate());
	        customersDTO.setLogin(SecurityUtils.getCurrentUserLogin().get());
	        customersDTO.setLastModifiedBy((SecurityUtils.getCurrentUserLogin().isPresent())?(SecurityUtils.getCurrentUserLogin().get()):"anonymousUser");
	        customersDTO.setLastModifiedDate(Instant.now());

	        customersService.save(customersDTO);
		}

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

        // Get customer identification by login
        CustomersResource customersResource = new CustomersResource(customersService);
        ResponseEntity<CustomersDTO> responseCustomersDTO = customersResource.getCustomersByLogin(SecurityUtils.getCurrentUserLogin().get());
        CustomersDTO customersDTO = responseCustomersDTO.getBody();

        List<PersonalDataDTO> listPersonalDataDTO = new ArrayList<PersonalDataDTO>();

        if (customersDTO == null) {

        } else {
    		PersonalDataDTO personalDataDTO = new PersonalDataDTO();
    		personalDataDTO.setId(customersDTO.getId());
    		personalDataDTO.setCustomerEmail(customersDTO.getCustomerEmail());
    		personalDataDTO.setCustomerGender(customersDTO.getCustomerGender());
    		personalDataDTO.setCustomerName(customersDTO.getCustomerName());
    		personalDataDTO.setCustomerNif(customersDTO.getCustomerNif());
    		personalDataDTO.setCustomerPhone(customersDTO.getCustomerPhone());
    		personalDataDTO.setCustomerBeginDate(customersDTO.getCustomerBeginDate());

    		listPersonalDataDTO.add(personalDataDTO);
        }

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

        Optional<CustomersDTO> customersDTOOpt = customersService.findOne(id);

        Optional<PersonalDataDTO> personalDataDTOOpt = Optional.empty();

		if (customersDTOOpt.isPresent()) {
			CustomersDTO customersDTO = customersDTOOpt.get();

			PersonalDataDTO personalDataDTO = new PersonalDataDTO();
			personalDataDTO.setId(customersDTO.getId());
			personalDataDTO.setCustomerEmail(customersDTO.getCustomerEmail());
			personalDataDTO.setCustomerGender(customersDTO.getCustomerGender());
			personalDataDTO.setCustomerName(customersDTO.getCustomerName());
			personalDataDTO.setCustomerNif(customersDTO.getCustomerNif());
			personalDataDTO.setCustomerPhone(customersDTO.getCustomerPhone());
			personalDataDTO.setCustomerBeginDate(customersDTO.getCustomerBeginDate());

			personalDataDTOOpt = Optional.of(personalDataDTO);
		}

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

        customersService.delete(id);

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

        Page<CustomersDTO> page = customersService.search(query, pageable);

		List<CustomersDTO> listCustomersDTO = page.getContent();
		List<PersonalDataDTO> listPersonalDataDTO = new ArrayList<PersonalDataDTO>();

		for (Iterator<CustomersDTO> iterator = listCustomersDTO.iterator(); iterator.hasNext();) {
			CustomersDTO customersDTO = iterator.next();

			PersonalDataDTO personalDataDTO = new PersonalDataDTO();
			personalDataDTO.setId(customersDTO.getId());
			personalDataDTO.setCustomerEmail(customersDTO.getCustomerEmail());
			personalDataDTO.setCustomerGender(customersDTO.getCustomerGender());
			personalDataDTO.setCustomerName(customersDTO.getCustomerName());
			personalDataDTO.setCustomerNif(customersDTO.getCustomerNif());
			personalDataDTO.setCustomerPhone(customersDTO.getCustomerPhone());
			personalDataDTO.setCustomerBeginDate(customersDTO.getCustomerBeginDate());

			listPersonalDataDTO.add(personalDataDTO);
		}

        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/personalData");

        return new ResponseEntity<>(listPersonalDataDTO, headers, HttpStatus.OK);
    }
}
