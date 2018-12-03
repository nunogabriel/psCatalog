package com.cgi.pscatalog.repository;

import com.cgi.pscatalog.domain.OrderDetHst;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the OrderDetHst entity.
 */
@SuppressWarnings("unused")
@Repository
public interface OrderDetHstRepository extends JpaRepository<OrderDetHst, Long> {

}
