package com.cgi.pscatalog.repository;

import com.cgi.pscatalog.domain.OrdersHst;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the OrdersHst entity.
 */
@SuppressWarnings("unused")
@Repository
public interface OrdersHstRepository extends JpaRepository<OrdersHst, Long> {

}
