package com.cgi.pscatalog.repository;

import com.cgi.pscatalog.domain.OrderDet;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the OrderDet entity.
 */
@SuppressWarnings("unused")
@Repository
public interface OrderDetRepository extends JpaRepository<OrderDet, Long> {

}
