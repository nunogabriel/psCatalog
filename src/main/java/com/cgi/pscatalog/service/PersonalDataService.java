package com.cgi.pscatalog.service;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Pageable;

import com.cgi.pscatalog.service.dto.CustomersDTO;
import com.cgi.pscatalog.service.dto.PersonalDataDTO;
import com.cgi.pscatalog.service.util.PageDataUtil;

/**
 * Service Interface for managing Customers.
 */
public interface PersonalDataService {

    List<PersonalDataDTO> getAllPersonalData(Pageable pageable);

	Optional<PersonalDataDTO> getPersonalData(Long id);

    PageDataUtil<CustomersDTO, PersonalDataDTO> searchPersonalData(String query, Pageable pageable);

	CustomersDTO createPersonalData(PersonalDataDTO personalDataDTO);

	void updatePersonalData(PersonalDataDTO personalDataDTO);

	void deletePersonalData(Long id);
}
