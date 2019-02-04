package com.cgi.pscatalog.service.impl;

import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cgi.pscatalog.security.SecurityUtils;
import com.cgi.pscatalog.service.AddressesService;
import com.cgi.pscatalog.service.CustomerAddressesService;
import com.cgi.pscatalog.service.CustomerOrdersService;
import com.cgi.pscatalog.service.CustomersService;
import com.cgi.pscatalog.service.OrdersService;
import com.cgi.pscatalog.service.dto.AddressesDTO;
import com.cgi.pscatalog.service.dto.CustomerAddressesDTO;
import com.cgi.pscatalog.service.dto.CustomersDTO;
import com.cgi.pscatalog.service.dto.OrdersDTO;
import com.cgi.pscatalog.service.util.PageDataUtil;
import com.cgi.pscatalog.web.rest.errors.BadRequestAlertException;
import com.cgi.pscatalog.web.rest.errors.FirstCreateCustomerException;

/**
 * Service Implementation for managing Addresses.
 */
@Service
@Transactional(rollbackFor=Exception.class)
public class CustomerAddressesServiceImpl implements CustomerAddressesService {

    private final Logger log = LoggerFactory.getLogger(CustomerAddressesServiceImpl.class);

    @Autowired
	private CustomersService customersService;

    @Autowired
    private OrdersService ordersService;

    @Autowired
    private CustomerOrdersService customerOrdersService;

    @Autowired
    private AddressesService addressesService;

	@Override
	@Transactional(readOnly = true)
	public PageDataUtil<AddressesDTO, CustomerAddressesDTO> getAllCustomerAddresses(Pageable pageable, String entityName) {
		// Get login
        String login = SecurityUtils.getCurrentUserLogin().get();

        // Get customer identification by login
        Optional<CustomersDTO> optionalCustomersDTO = customersService.getCustomersByLogin(login);

        if ( !optionalCustomersDTO.isPresent() ) {
            throw new FirstCreateCustomerException(entityName);
        }

        // Get address identification by customer identification
        Page<AddressesDTO> page = addressesService.getAddressesByLogin(login, pageable);

        List<AddressesDTO> listAddressesDTO = page.getContent();

        List<CustomerAddressesDTO> listCustomerAddressesDTO = new ArrayList<CustomerAddressesDTO>();

        for (Iterator<AddressesDTO> iterator = listAddressesDTO.iterator(); iterator.hasNext();) {
			AddressesDTO addressesDTO = iterator.next();

			CustomerAddressesDTO customerAddressesDTO = new CustomerAddressesDTO();
			customerAddressesDTO.setId(addressesDTO.getId());
			customerAddressesDTO.setAddressName(addressesDTO.getAddressName());
			customerAddressesDTO.setAddressNif(addressesDTO.getAddressNif());
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

		// Set page information
		PageDataUtil<AddressesDTO, CustomerAddressesDTO> pageDataUtil = new PageDataUtil<AddressesDTO, CustomerAddressesDTO>();
		pageDataUtil.setPageInformation(page);
		pageDataUtil.setContent(listCustomerAddressesDTO);

		return pageDataUtil;
    }

	@Override
	@Transactional(readOnly = true)
    public Optional<CustomerAddressesDTO> getCustomerAddresses(Long id) {
        Optional<AddressesDTO> addressesDTOOpt = addressesService.findOne(id);

        Optional<CustomerAddressesDTO> customerAddressesDTOopt = Optional.empty();

		if (addressesDTOOpt.isPresent()) {
			AddressesDTO addressesDTO = addressesDTOOpt.get();

			CustomerAddressesDTO customerAddressesDTO = new CustomerAddressesDTO();
			customerAddressesDTO.setId(addressesDTO.getId());
			customerAddressesDTO.setAddressName(addressesDTO.getAddressName());
			customerAddressesDTO.setAddressNif(addressesDTO.getAddressNif());
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

		return customerAddressesDTOopt;
    }

    /**
     * Search for the addresses corresponding to the query.
     *
     * @param query the query of the search
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public PageDataUtil<AddressesDTO, CustomerAddressesDTO> searchCustomerAddresses(String query, Pageable pageable) {
        Page<AddressesDTO> page =  addressesService.search(query, pageable);

        List<AddressesDTO> listAddressesDTO = page.getContent();
		List<CustomerAddressesDTO> listCustomerAddressesDTO = new ArrayList<CustomerAddressesDTO>();

		for (Iterator<AddressesDTO> iterator = listAddressesDTO.iterator(); iterator.hasNext();) {
			AddressesDTO addressesDTO = iterator.next();

			CustomerAddressesDTO customerAddressesDTO = new CustomerAddressesDTO();
			customerAddressesDTO.setId(addressesDTO.getId());
			customerAddressesDTO.setAddressName(addressesDTO.getAddressName());
			customerAddressesDTO.setAddressNif(addressesDTO.getAddressNif());
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

		// Set page information
		PageDataUtil<AddressesDTO, CustomerAddressesDTO> pageDataUtil = new PageDataUtil<AddressesDTO, CustomerAddressesDTO>();
		pageDataUtil.setPageInformation(page);
		pageDataUtil.setContent(listCustomerAddressesDTO);

		return pageDataUtil;
    }

	@Override
	public CustomerAddressesDTO createCustomerAddresses(CustomerAddressesDTO customerAddressesDTO, String entityName) {
        // Get customer identification by login
        Optional<CustomersDTO> optionalCustomersDTO = customersService.getCustomersByLogin(SecurityUtils.getCurrentUserLogin().get());

        Long customerId = new Long(0);

        if ( optionalCustomersDTO.isPresent() ) {
        	CustomersDTO customersDTO = optionalCustomersDTO.get();

        	customerId = customersDTO.getId();

        	log.debug("REST request to createCustomerAddresses - customerId: {}", customerId);
        } else {
        	throw new BadRequestAlertException("You must create a customer first", entityName, "idnull");
        }

        //
        AddressesDTO addressesDTO = new AddressesDTO();
        addressesDTO.setAddressName(customerAddressesDTO.getAddressName());
        addressesDTO.setAddressNif(customerAddressesDTO.getAddressNif());
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
        addressesDTO.setAddressBeginDate(Instant.now());

        AddressesDTO addressesDTOAux = addressesService.save(addressesDTO);

        customerAddressesDTO.setId(addressesDTOAux.getId());

        return customerAddressesDTO;
    }

	@Override
	public Map<Long,Long> updateCustomerAddresses(Long oldCustomerId, Long newCustomerId) {
		 // Get login
        String login = SecurityUtils.getCurrentUserLogin().get();

		// Add new addresses with new customer data
		Page<AddressesDTO> pageAddressesDTOOld = addressesService.getAddressesByLoginAndCustomerId(login, oldCustomerId, PageRequest.of(0, 1000));
		List<AddressesDTO> listAddressesDTOOld = pageAddressesDTOOld.getContent();

		Map<Long,Long> mapOldNewAddresses = new HashMap<Long,Long>();

		for (Iterator<AddressesDTO> iteratorAddressesDTOOld = listAddressesDTOOld.iterator(); iteratorAddressesDTOOld.hasNext();) {
			AddressesDTO addressesDTOOld = iteratorAddressesDTOOld.next();

	        // Add new addresses
	        AddressesDTO addressesDTONew = new AddressesDTO();
	        addressesDTONew.setAddressName(addressesDTOOld.getAddressName());
	        addressesDTONew.setAddressReference(addressesDTOOld.getAddressReference());
	        addressesDTONew.setCity(addressesDTOOld.getCity());
	        addressesDTONew.setCountryCountryName(addressesDTOOld.getCountryCountryName());
	        addressesDTONew.setCountryId(addressesDTOOld.getCountryId());
	        addressesDTONew.setPhoneNumber(addressesDTOOld.getPhoneNumber());
	        addressesDTONew.setState(addressesDTOOld.getState());
	        addressesDTONew.setStreetAddress(addressesDTOOld.getStreetAddress());
	        addressesDTONew.setZipCode(addressesDTOOld.getZipCode());
	        addressesDTONew.setCreatedBy(addressesDTOOld.getCreatedBy());
	        addressesDTONew.setCreatedDate(addressesDTOOld.getCreatedDate());
	        addressesDTONew.setLastModifiedBy(login);
	        addressesDTONew.setLastModifiedDate(Instant.now());
	        addressesDTONew.setAddressBeginDate(addressesDTOOld.getAddressBeginDate());
	        addressesDTONew.setAddressNif(addressesDTOOld.getAddressNif());
	        addressesDTONew.setCustomerId(newCustomerId);

	        addressesDTONew = addressesService.save(addressesDTONew);

	        mapOldNewAddresses.put(addressesDTOOld.getId(), addressesDTONew.getId());

	        log.debug("REST request to update customer addresses : addressesDTOOld {}", addressesDTOOld.getId());
	        log.debug("REST request to update customer addresses : addressesDTONew {}", addressesDTONew.getId());

			// Delete old addresses = Update address_end_date
	        addressesDTOOld.setAddressEndDate(Instant.now());
	        addressesDTOOld.setLastModifiedBy(login);
	        addressesDTOOld.setLastModifiedDate(Instant.now());

	        addressesService.save(addressesDTOOld);
		}

		return mapOldNewAddresses;
	}

	@Override
	public void updateCustomerAddresses(CustomerAddressesDTO customerAddressesDTO) {
        // Get login
        String login = SecurityUtils.getCurrentUserLogin().get();

        // Get customer id
        Long customerId = customerAddressesDTO.getCustomerId();

        // Verify if already exists orders with PENDING Status
        Page<OrdersDTO> pageOrdersPending = ordersService.getAllByLoginAndCustomerIdAndOrderStatusPending(login, customerId, PageRequest.of(0, 1));

        List<OrdersDTO> listOrdersDTOPending = pageOrdersPending.getContent();

        // Verify if already exists orders with status different from PENDING
        Page<OrdersDTO> pageOrders = ordersService.getAllByLoginAndCustomerIdAndOrderStatus(login, customerId, PageRequest.of(0, 1000));

        List<OrdersDTO> listOrdersDTO = pageOrders.getContent();

        log.debug("REST request to update customer addresses : listOrdersDTOPending {}", listOrdersDTOPending.size());
        log.debug("REST request to update customer addresses : listOrdersDTO {}", listOrdersDTO.size());

        //
        if ( listOrdersDTOPending.size() == 0 && listOrdersDTO.size() == 0 ) {
        	// There are not orders
	        Optional<AddressesDTO> addressesDTOOpt = addressesService.findOne(customerAddressesDTO.getId());

			if (addressesDTOOpt.isPresent()) {
				AddressesDTO addressesDTO = addressesDTOOpt.get();

				addressesDTO.setAddressName(customerAddressesDTO.getAddressName());
				addressesDTO.setAddressNif(customerAddressesDTO.getAddressNif());
		        addressesDTO.setAddressReference(customerAddressesDTO.getAddressReference());
		        addressesDTO.setCity(customerAddressesDTO.getCity());
		        addressesDTO.setCountryCountryName(customerAddressesDTO.getCountryCountryName());
		        addressesDTO.setCountryId(customerAddressesDTO.getCountryId());
		        addressesDTO.setPhoneNumber(customerAddressesDTO.getPhoneNumber());
		        addressesDTO.setState(customerAddressesDTO.getState());
		        addressesDTO.setStreetAddress(customerAddressesDTO.getStreetAddress());
		        addressesDTO.setZipCode(customerAddressesDTO.getZipCode());
				addressesDTO.setLastModifiedBy(login);
				addressesDTO.setLastModifiedDate(Instant.now());

				addressesService.save(addressesDTO);
			}
        } else {
        	// There are orders for that customer

        	// Add new addresses with customer data
    		Map<Long,Long> mapOldNewAddresses = updateCustomerAddresses(customerId, customerId);

    		// Update PENDING orders with new customer data
    		customerOrdersService.updatePendingOrders(listOrdersDTOPending, mapOldNewAddresses, customerId);
        }
    }

	@Override
	public void deleteCustomerAddresses(Long customerId) {
		// Get login
        String login = SecurityUtils.getCurrentUserLogin().get();

		// Delete addresses = Update address_end_date
		Page<AddressesDTO> pageAddressesDTO = addressesService.getAddressesByLoginAndCustomerId(login, customerId, PageRequest.of(0, 1000));
		List<AddressesDTO> listAddressesDTO = pageAddressesDTO.getContent();

		for (Iterator<AddressesDTO> iteratorAddressesDTO = listAddressesDTO.iterator(); iteratorAddressesDTO.hasNext();) {
			AddressesDTO addressesDTO = iteratorAddressesDTO.next();

	        addressesDTO.setAddressEndDate(Instant.now());
	        addressesDTO.setLastModifiedBy(login);
	        addressesDTO.setLastModifiedDate(Instant.now());

	        addressesService.save(addressesDTO);
		}
	}

	@Override
	public void deleteCustomerAddresses(Long customerId, String entityName) {
		// Get login
        String login = SecurityUtils.getCurrentUserLogin().get();

        if ( customerId == null ) {
            // Get customer identification by login
            Optional<CustomersDTO> optionalCustomersDTO = customersService.getCustomersByLogin(login);

            customerId = new Long(0);

            if ( optionalCustomersDTO.isPresent() ) {
            	CustomersDTO customersDTO = optionalCustomersDTO.get();

            	customerId = customersDTO.getId();

            	log.debug("REST request to deleteCustomerAddresses - customerId: {}", customerId);
            } else {
            	throw new BadRequestAlertException("You must create a customer first", entityName, "idnull");
            }
        }

		// Delete addresses = Update address_end_date
        deleteCustomerAddresses(customerId);

        // Delete PENDING order and correspondent order details
        customerOrdersService.deletePendingOrders(customerId);
	}
}