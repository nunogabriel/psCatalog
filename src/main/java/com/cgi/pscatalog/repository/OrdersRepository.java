package com.cgi.pscatalog.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.cgi.pscatalog.domain.Orders;


/**
 * Spring Data  repository for the Orders entity.
 */
@SuppressWarnings("unused")
@Repository
public interface OrdersRepository extends JpaRepository<Orders, Long> {

	@Query("select orders from Orders orders where orders.customer.id =:customerId and orders.orderStatus.id =:orderStatusId")
	Optional<Orders> findByCustomerIdAndOrderStatusId(@Param("customerId") Long customerId, @Param("orderStatusId") Long orderStatusId);

	@Query("select orders from Orders orders where orders.customer.id =:customerId")
	Page<Orders> findAllByCustomerId(@Param("customerId") Long customerId, Pageable pageable);

}
