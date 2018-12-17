package com.cgi.pscatalog.repository;

import com.cgi.pscatalog.domain.Addresses;
import com.cgi.pscatalog.service.dto.AddressesDTO;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the Addresses entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AddressesRepository extends JpaRepository<Addresses, Long> {

	@Query("select addresses from Addresses addresses where addresses.customer.id =:id")
	Page<Addresses> findAllByCustomerId(@Param("id") Long id, Pageable pageable);

}
