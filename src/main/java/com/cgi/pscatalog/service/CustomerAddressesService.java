package com.cgi.pscatalog.service;

import java.util.Map;
import java.util.Optional;

import org.springframework.data.domain.Pageable;

import com.cgi.pscatalog.service.dto.AddressesDTO;
import com.cgi.pscatalog.service.dto.CustomerAddressesDTO;
import com.cgi.pscatalog.service.util.PageDataUtil;

/**
 * Service Interface for managing Addresses.
 */
public interface CustomerAddressesService {
    PageDataUtil<AddressesDTO, CustomerAddressesDTO> searchCustomerAddresses(String query, Pageable pageable);

	PageDataUtil<AddressesDTO, CustomerAddressesDTO> getAllCustomerAddresses(Pageable pageable, String entityName);

	Optional<CustomerAddressesDTO> getCustomerAddresses(Long id);

	CustomerAddressesDTO createCustomerAddresses(CustomerAddressesDTO customerAddressesDTO, String entityName);

	Map<Long,Long> updateCustomerAddresses(Long oldCustomerId, Long newCustomerId);

	void updateCustomerAddresses(CustomerAddressesDTO customerAddressesDTO);

	void deleteCustomerAddresses(Long customerId, String entityName);

	void deleteCustomerAddresses(Long customerId);

}
