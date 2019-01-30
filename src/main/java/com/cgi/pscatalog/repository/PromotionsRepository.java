package com.cgi.pscatalog.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.cgi.pscatalog.domain.Promotions;


/**
 * Spring Data  repository for the Promotions entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PromotionsRepository extends JpaRepository<Promotions, Long> {

	@Query("select pr from Promotions pr where pr.products.id =:productId and ((CURRENT_TIMESTAMP between pr.promotionStartDate and pr.promotionExpiryDate) or pr.promotionExpiryDate is null)")
	Page<Promotions> findAllPromotionsByProductId(@Param("productId") Long productId, Pageable pageable);

}
