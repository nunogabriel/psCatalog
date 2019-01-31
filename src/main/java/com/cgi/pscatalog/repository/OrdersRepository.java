package com.cgi.pscatalog.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.cgi.pscatalog.config.Constants;
import com.cgi.pscatalog.domain.Orders;


/**
 * Spring Data  repository for the Orders entity.
 */
@SuppressWarnings("unused")
@Repository
public interface OrdersRepository extends JpaRepository<Orders, Long> {

	@Query("select orders from Orders orders where orders.customer.login =:login and orders.orderStatus.orderStatusDescription = '" + Constants.ORDER_STATUS_PENDING + "'")
	Page<Orders> findAllByLoginAndOrderStatusPending(@Param("login") String login, Pageable pageable);

	@Query("select orders from Orders orders where orders.customer.login =:login and orders.orderStatus.orderStatusDescription != '" + Constants.ORDER_STATUS_PENDING + "'")
	Page<Orders> findAllByLoginAndOrderStatus(@Param("login") String login, Pageable pageable);

	@Query("select orders from Orders orders where orders.customer.login =:login and orders.customer.id =:customerId and orders.orderStatus.orderStatusDescription = '" + Constants.ORDER_STATUS_PENDING + "'")
	Page<Orders> findAllByLoginAndCustomerIdAndOrderStatusPending(@Param("login") String login, @Param("customerId") Long customerId, Pageable pageable);

	@Query("select orders from Orders orders where orders.customer.login =:login and orders.customer.id =:customerId and orders.orderStatus.orderStatusDescription != '" + Constants.ORDER_STATUS_PENDING + "'")
	Page<Orders> findAllByLoginAndCustomerIdAndOrderStatus(@Param("login") String login, @Param("customerId") Long customerId, Pageable pageable);

}
