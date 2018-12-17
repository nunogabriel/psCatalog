package com.cgi.pscatalog.repository;

import com.cgi.pscatalog.domain.OrderDet;
import com.cgi.pscatalog.service.dto.OrderDetDTO;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the OrderDet entity.
 */
@SuppressWarnings("unused")
@Repository
public interface OrderDetRepository extends JpaRepository<OrderDet, Long> {

	@Query("select orderdet from OrderDet orderdet where orderdet.order.id =:orderId and orderdet.product.id =:productId")
	Optional<OrderDet> findByOrderIdAndProductId(@Param("orderId") Long orderId, @Param("productId") Long productId);

}
