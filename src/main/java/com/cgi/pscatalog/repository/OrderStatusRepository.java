package com.cgi.pscatalog.repository;

import com.cgi.pscatalog.domain.OrderStatus;
import com.cgi.pscatalog.service.dto.OrderStatusDTO;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the OrderStatus entity.
 */
@SuppressWarnings("unused")
@Repository
public interface OrderStatusRepository extends JpaRepository<OrderStatus, Long> {

	@Query("select orderstatus from OrderStatus orderstatus where orderstatus.orderStatusDescription =:orderStatusDescription")
	Optional<OrderStatus> findOneByOrderStatusDescription(@Param("orderStatusDescription") String orderStatusDescription);

}
