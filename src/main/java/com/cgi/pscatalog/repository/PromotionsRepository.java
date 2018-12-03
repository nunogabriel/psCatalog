package com.cgi.pscatalog.repository;

import com.cgi.pscatalog.domain.Promotions;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the Promotions entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PromotionsRepository extends JpaRepository<Promotions, Long> {

}
