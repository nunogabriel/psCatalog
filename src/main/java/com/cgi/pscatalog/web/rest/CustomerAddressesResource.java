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
import org.springframework.beans.factory.annotation.Autowired;
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
import com.cgi.pscatalog.service.AddressesService;
import com.cgi.pscatalog.service.CustomersService;
import com.cgi.pscatalog.service.dto.AddressesDTO;
import com.cgi.pscatalog.service.dto.CustomerAddressesDTO;
import com.cgi.pscatalog.service.dto.CustomersDTO;
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

    private final AddressesService addressesService;

    @Autowired
	private CustomersService customersService;

    public CustomerAddressesResource(AddressesService addressesService) {
        this.addressesService = addressesService;
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
        log.debug("REST request to save Customer Addresses : {}", customerAddressesDTO);

        if (customerAddressesDTO.getId() != null) {
            throw new BadRequestAlertException("A new customer address cannot already have an ID", ENTITY_NAME, "idexists");
        }

        // Get customer identification by login
        CustomersResource customersResource = new CustomersResource(customersService);
        ResponseEntity<CustomersDTO> responseCustomersDTO = customersResource.getCustomersByLogin(SecurityUtils.getCurrentUserLogin().get());
        CustomersDTO customersDTO = responseCustomersDTO.getBody();
		Long customerId = customersDTO.getId();

        if (customerId.longValue() == 0) {
            throw new BadRequestAlertException("You must create a customer first", ENTITY_NAME, "idnull");
        }

        log.debug("REST request to getAllCustomerAddresses - customerId: {}", customerId);

        AddressesDTO addressesDTO = new AddressesDTO();
        addressesDTO.setAddressName(customerAddressesDTO.getAddressName());
        addressesDTO.setAddressReference(customerAddressesDTO.getAddressReference());
        addressesDTO.setCity(customerAddressesDTO.getCity());
        addressesDTO.setCountryCountryName(customerAddressesDTO.getCountryCountryName());
        addressesDTO.setCountryId(customerAddressesDTO.getCountryId());
        addressesDTO.setCustomerId(customerId);
        addressesDTO.setPhoneNumber(customerAddressesDTO.getPhoneNumber());
        addressesDTO.setState(customerAddressesDTO.getState());
        addressesDTO.setStreetAddress(customerAddressesDTO.getStreetAddress());
        addressesDTO.setZipCode(customerAddressesDTO.getZipCode());
        addressesDTO.setCreatedBy((SecurityUtils.getCurrentUserLogin().isPresent())?(SecurityUtils.getCurrentUserLogin().get()):"anonymousUser");
        addressesDTO.setCreatedDate(Instant.now());

        AddressesDTO addressesDTOAux = addressesService.save(addressesDTO);

        customerAddressesDTO.setId(addressesDTOAux.getId());

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

        Optional<AddressesDTO> addressesDTOOpt = addressesService.findOne(customerAddressesDTO.getId());

		if (addressesDTOOpt.isPresent()) {
			AddressesDTO addressesDTO = addressesDTOOpt.get();

			addressesDTO.setAddressName(customerAddressesDTO.getAddressName());
	        addressesDTO.setAddressReference(customerAddressesDTO.getAddressReference());
	        addressesDTO.setCity(customerAddressesDTO.getCity());
	        addressesDTO.setCountryCountryName(customerAddressesDTO.getCountryCountryName());
	        addressesDTO.setCountryId(customerAddressesDTO.getCountryId());
	        addressesDTO.setPhoneNumber(customerAddressesDTO.getPhoneNumber());
	        addressesDTO.setState(customerAddressesDTO.getState());
	        addressesDTO.setStreetAddress(customerAddressesDTO.getStreetAddress());
	        addressesDTO.setZipCode(customerAddressesDTO.getZipCode());
			addressesDTO.setLastModifiedBy((SecurityUtils.getCurrentUserLogin().isPresent())?(SecurityUtils.getCurrentUserLogin().get()):"anonymousUser");
			addressesDTO.setLastModifiedDate(Instant.now());

	        addressesService.save(addressesDTO);
		}

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

        // Get customer identification by login
        CustomersResource customersResource = new CustomersResource(customersService);
        ResponseEntity<CustomersDTO> responseCustomersDTO = customersResource.getCustomersByLogin(SecurityUtils.getCurrentUserLogin().get());
        CustomersDTO customersDTO = responseCustomersDTO.getBody();
		Long customerId = customersDTO.getId();

        if (customerId.longValue() == 0) {
            throw new BadRequestAlertException("You must create a customer first", ENTITY_NAME, "idnull");
        }

        log.debug("REST request to getAllCustomerAddresses - customerId: {}", customerId);

        // Get address identification by customer identification
        AddressesResource addressesResource = new AddressesResource(addressesService);
        ResponseEntity<List<AddressesDTO>> responseListAddressesDTO = addressesResource.getAddressesByCustomerId(customerId, pageable);
        List<AddressesDTO> listAddressesDTO = responseListAddressesDTO.getBody();

        List<CustomerAddressesDTO> listCustomerAddressesDTO = new ArrayList<CustomerAddressesDTO>();

        for (Iterator<AddressesDTO> iterator = listAddressesDTO.iterator(); iterator.hasNext();) {
			AddressesDTO addressesDTO = iterator.next();

			CustomerAddressesDTO customerAddressesDTO = new CustomerAddressesDTO();
			customerAddressesDTO.setId(addressesDTO.getId());
			customerAddressesDTO.setAddressName(addressesDTO.getAddressName());
			customerAddressesDTO.setAddressReference(addressesDTO.getAddressReference());
			customerAddressesDTO.setCity(addressesDTO.getCity());
			customerAddressesDTO.setCountryCountryName(addressesDTO.getCountryCountryName());
			customerAddressesDTO.setCountryId(addressesDTO.getCountryId());
			customerAddressesDTO.setCustomerId(addressesDTO.getCustomerId());
			customerAddressesDTO.setPhoneNumber(addressesDTO.getPhoneNumber());
			customerAddressesDTO.setState(addressesDTO.getState());
			customerAddressesDTO.setStreetAddress(addressesDTO.getStreetAddress());
			customerAddressesDTO.setZipCode(addressesDTO.getZipCode());

			listCustomerAddressesDTO.add(customerAddressesDTO);
		}

        Page<CustomerAddressesDTO> page = new PageImpl<CustomerAddressesDTO>(listCustomerAddressesDTO, pageable, listCustomerAddressesDTO.size());

        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/customerAddresses");

        return ResponseEntity.ok().headers(headers).body(page.getContent());
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

        Optional<AddressesDTO> addressesDTOOpt = addressesService.findOne(id);

        Optional<CustomerAddressesDTO> customerAddressesDTOopt = Optional.empty();

		if (addressesDTOOpt.isPresent()) {
			AddressesDTO addressesDTO = addressesDTOOpt.get();

			CustomerAddressesDTO customerAddressesDTO = new CustomerAddressesDTO();
			customerAddressesDTO.setId(addressesDTO.getId());
			customerAddressesDTO.setAddressName(addressesDTO.getAddressName());
			customerAddressesDTO.setAddressReference(addressesDTO.getAddressReference());
			customerAddressesDTO.setCity(addressesDTO.getCity());
			customerAddressesDTO.setCountryCountryName(addressesDTO.getCountryCountryName());
			customerAddressesDTO.setCountryId(addressesDTO.getCountryId());
			customerAddressesDTO.setCustomerId(addressesDTO.getCustomerId());
			customerAddressesDTO.setPhoneNumber(addressesDTO.getPhoneNumber());
			customerAddressesDTO.setState(addressesDTO.getState());
			customerAddressesDTO.setStreetAddress(addressesDTO.getStreetAddress());
			customerAddressesDTO.setZipCode(addressesDTO.getZipCode());

			customerAddressesDTOopt = Optional.of(customerAddressesDTO);
		}

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

        addressesService.delete(id);

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

        Page<AddressesDTO> page = addressesService.search(query, pageable);

        List<AddressesDTO> listAddressesDTO = page.getContent();
		List<CustomerAddressesDTO> listCustomerAddressesDTO = new ArrayList<CustomerAddressesDTO>();

		for (Iterator<AddressesDTO> iterator = listAddressesDTO.iterator(); iterator.hasNext();) {
			AddressesDTO addressesDTO = iterator.next();

			CustomerAddressesDTO customerAddressesDTO = new CustomerAddressesDTO();
			customerAddressesDTO.setId(addressesDTO.getId());
			customerAddressesDTO.setAddressName(addressesDTO.getAddressName());
			customerAddressesDTO.setAddressReference(addressesDTO.getAddressReference());
			customerAddressesDTO.setCity(addressesDTO.getCity());
			customerAddressesDTO.setCountryCountryName(addressesDTO.getCountryCountryName());
			customerAddressesDTO.setCountryId(addressesDTO.getCountryId());
			customerAddressesDTO.setCustomerId(addressesDTO.getCustomerId());
			customerAddressesDTO.setPhoneNumber(addressesDTO.getPhoneNumber());
			customerAddressesDTO.setState(addressesDTO.getState());
			customerAddressesDTO.setStreetAddress(addressesDTO.getStreetAddress());
			customerAddressesDTO.setZipCode(addressesDTO.getZipCode());

			listCustomerAddressesDTO.add(customerAddressesDTO);
		}

        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/customerAddresses");

        return new ResponseEntity<>(listCustomerAddressesDTO, headers, HttpStatus.OK);
    }
}
