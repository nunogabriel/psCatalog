package com.cgi.pscatalog.service.impl;

import java.time.Instant;
import java.util.ArrayList;
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
import com.cgi.pscatalog.service.CustomerAddressesService;
import com.cgi.pscatalog.service.CustomerOrdersService;
import com.cgi.pscatalog.service.CustomersService;
import com.cgi.pscatalog.service.OrdersService;
import com.cgi.pscatalog.service.PersonalDataService;
import com.cgi.pscatalog.service.dto.CustomersDTO;
import com.cgi.pscatalog.service.dto.OrdersDTO;
import com.cgi.pscatalog.service.dto.PersonalDataDTO;
import com.cgi.pscatalog.service.util.PageDataUtil;

/**
 * Service Implementation for managing Customers.
 */
@Service
@Transactional(rollbackFor=Exception.class)
public class PersonalDataServiceImpl implements PersonalDataService {

    private final Logger log = LoggerFactory.getLogger(PersonalDataServiceImpl.class);

    @Autowired
    private OrdersService ordersService;

    @Autowired
    private CustomerOrdersService customerOrdersService;

    @Autowired
    private CustomersService customersService;

    @Autowired
    private CustomerAddressesService customerAddressesService;

	@Override
	@Transactional(readOnly = true)
	public List<PersonalDataDTO> getAllPersonalData(Pageable pageable) {
		// Get login
		String login = SecurityUtils.getCurrentUserLogin().get();

        log.debug("Request to get all personal data by login: {}", login);

        Optional<CustomersDTO> optionalCustomersDTO =  customersService.getCustomersByLogin(login);

		List<PersonalDataDTO> listPersonalDataDTO = new ArrayList<PersonalDataDTO>();

        if ( optionalCustomersDTO.isPresent() ) {
			CustomersDTO customersDTO = optionalCustomersDTO.get();

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

		return listPersonalDataDTO;
	}

	@Override
	@Transactional(readOnly = true)
	public Optional<PersonalDataDTO> getPersonalData(Long id) {
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

        return personalDataDTOOpt;
    }

    /**
     * Search for the customers corresponding to the query.
     *
     * @param query the query of the search
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public PageDataUtil<CustomersDTO, PersonalDataDTO> searchPersonalData(String query, Pageable pageable) {
        log.debug("Request to search for a page of Customers for query {}", query);

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

		// Set page information
		PageDataUtil<CustomersDTO, PersonalDataDTO> pageDataUtil = new PageDataUtil<CustomersDTO, PersonalDataDTO>();
		pageDataUtil.setPageInformation(page);
		pageDataUtil.setContent(listPersonalDataDTO);

		return pageDataUtil;
    }

	@Override
    public CustomersDTO createPersonalData(PersonalDataDTO personalDataDTO) {
        String login = (SecurityUtils.getCurrentUserLogin().isPresent())?(SecurityUtils.getCurrentUserLogin().get()):"anonymousUser";

        CustomersDTO customersDTO = new CustomersDTO();
        customersDTO.setCustomerEmail(personalDataDTO.getCustomerEmail());
        customersDTO.setCustomerGender(personalDataDTO.getCustomerGender());
        customersDTO.setCustomerName(personalDataDTO.getCustomerName());
        customersDTO.setCustomerNif(personalDataDTO.getCustomerNif());
        customersDTO.setCustomerPhone(personalDataDTO.getCustomerPhone());
        customersDTO.setCustomerBeginDate(Instant.now());
        customersDTO.setCustomerEndDate(null);
        customersDTO.setLogin(login);
        customersDTO.setCreatedBy(login);
        customersDTO.setCreatedDate(Instant.now());

        return customersService.save(customersDTO);
    }

	@Override
    public void updatePersonalData(PersonalDataDTO personalDataDTO) {
        // Get login
        String login = SecurityUtils.getCurrentUserLogin().get();

        // Get customer id
        Long customerId = personalDataDTO.getId();

        // Verify if already exists orders with PENDING Status
        Page<OrdersDTO> pageOrdersPending = ordersService.getAllByLoginAndCustomerIdAndOrderStatusPending(login, customerId, PageRequest.of(0, 1));

        List<OrdersDTO> listOrdersDTOPending = pageOrdersPending.getContent();

        // Verify if already exists orders with status different from PENDING
        Page<OrdersDTO> pageOrders = ordersService.getAllByLoginAndCustomerIdAndOrderStatus(login, customerId, PageRequest.of(0, 1000));

        List<OrdersDTO> listOrdersDTO = pageOrders.getContent();

        log.debug("REST request to update personal data : listOrdersDTOPending {}", listOrdersDTOPending.size());
        log.debug("REST request to update personal data : listOrdersDTO {}", listOrdersDTO.size());

        //
        if ( listOrdersDTOPending.size() == 0 && listOrdersDTO.size() == 0 ) {
        	// There are not orders
	        Optional<CustomersDTO> customersDTOOpt = customersService.findOne(customerId);

			if (customersDTOOpt.isPresent()) {
				CustomersDTO customersDTO = customersDTOOpt.get();

		        customersDTO.setCustomerEmail(personalDataDTO.getCustomerEmail());
		        customersDTO.setCustomerGender(personalDataDTO.getCustomerGender());
		        customersDTO.setCustomerName(personalDataDTO.getCustomerName());
		        customersDTO.setCustomerNif(personalDataDTO.getCustomerNif());
		        customersDTO.setCustomerPhone(personalDataDTO.getCustomerPhone());
		        customersDTO.setLastModifiedBy(login);
		        customersDTO.setLastModifiedDate(Instant.now());

		        customersService.save(customersDTO);
			}
        } else {
        	// There are orders for that customer
            Optional<CustomersDTO> customersDTOOptOld = customersService.findOne(customerId);

            CustomersDTO customersDTOOld = customersDTOOptOld.get();

            CustomersDTO customersDTONew = null;

    		if (customersDTOOptOld.isPresent()) {
	        	// Create new customer with customer products associated
	            customersDTONew = new CustomersDTO();
	            customersDTONew.setCustomerEmail(personalDataDTO.getCustomerEmail());
	            customersDTONew.setCustomerGender(personalDataDTO.getCustomerGender());
	            customersDTONew.setCustomerName(personalDataDTO.getCustomerName());
	            customersDTONew.setCustomerNif(personalDataDTO.getCustomerNif());
	            customersDTONew.setCustomerPhone(personalDataDTO.getCustomerPhone());
	            customersDTONew.setCustomerBeginDate(personalDataDTO.getCustomerBeginDate());
	            customersDTONew.setCustomerEndDate(null);
	            customersDTONew.setLogin(login);
	            customersDTONew.setCreatedBy(customersDTOOld.getCreatedBy());
	            customersDTONew.setCreatedDate(customersDTOOld.getCreatedDate());
	            customersDTONew.setLastModifiedBy(login);
	            customersDTONew.setLastModifiedDate(Instant.now());
	            customersDTONew.setProducts(customersDTOOld.getProducts());

	            customersDTONew = customersService.save(customersDTONew);

	            // Delete old customer and remove products from it (delete records from customer_products table)
    			customersDTOOld.setCustomerEndDate(Instant.now());
    			customersDTOOld.setLastModifiedBy(login);
    			customersDTOOld.setLastModifiedDate(Instant.now());

    			if ( listOrdersDTO.size() == 0 ) {
    				// If the are not orders with status different from PENDING
    				customersDTOOld.setProducts(null);

    				log.debug("REST request to update personal data : setProducts to null");
    			}

    			customersService.save(customersDTOOld);
    		}

        	// Add new addresses with new customer data
    		Map<Long,Long> mapOldNewAddresses = customerAddressesService.updateCustomerAddresses(customerId, customersDTONew.getId());

    		// Update PENDING orders with new customer data
    		customerOrdersService.updatePendingOrders(listOrdersDTOPending, mapOldNewAddresses, customersDTONew.getId());
        }
    }

	@Override
    public void deletePersonalData(Long id) {
        // Get login
        String login = SecurityUtils.getCurrentUserLogin().get();

        // Verify if already exists orders with status different from PENDING
        Page<OrdersDTO> pageOrders = ordersService.getAllByLoginAndCustomerIdAndOrderStatus(login, id, PageRequest.of(0, 1000));

        List<OrdersDTO> listOrdersDTO = pageOrders.getContent();

        // Delete old customer and remove products from it (delete records from customer_products table)
        Optional<CustomersDTO> customersDTOOpt = customersService.findOne(id);

		if (customersDTOOpt.isPresent()) {
			CustomersDTO customersDTOOld = customersDTOOpt.get();

			// Delete old customer and remove products from it (delete records from customer_products table)
			customersDTOOld.setCustomerEndDate(Instant.now());
			customersDTOOld.setLastModifiedBy(login);
			customersDTOOld.setLastModifiedDate(Instant.now());

	        if ( listOrdersDTO.size() == 0 ) {
				// If the are not orders with status different from PENDING
				customersDTOOld.setProducts(null);
			}

	        customersService.save(customersDTOOld);
		}

        // Delete addresses = Update address_end_date
        customerAddressesService.deleteCustomerAddresses(id);

        // Delete PENDING order and correspondent order details
        customerOrdersService.deletePendingOrders(id);
    }
}