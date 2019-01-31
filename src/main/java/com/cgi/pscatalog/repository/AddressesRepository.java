package com.cgi.pscatalog.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.cgi.pscatalog.domain.Addresses;


/**
 * Spring Data  repository for the Addresses entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AddressesRepository extends JpaRepository<Addresses, Long> {

	@Query("select addresses from Addresses addresses where addresses.customer.login =:login and ((CURRENT_TIMESTAMP between addresses.addressBeginDate and addresses.addressEndDate) or addresses.addressEndDate is null)")
	Page<Addresses> findAllByLogin(@Param("login") String login, Pageable pageable);

	@Query("select addresses from Addresses addresses where addresses.customer.login =:login and addresses.customer.id =:customerId and ((CURRENT_TIMESTAMP between addresses.addressBeginDate and addresses.addressEndDate) or addresses.addressEndDate is null)")
	Page<Addresses> findAllByLoginAndCustomerId(@Param("login") String login, @Param("customerId") Long customerId, Pageable pageable);

}
