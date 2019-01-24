package com.cgi.pscatalog.repository;

import java.math.BigDecimal;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.cgi.pscatalog.config.Constants;
import com.cgi.pscatalog.domain.OrderDet;


/**
 * Spring Data  repository for the OrderDet entity.
 */
@SuppressWarnings("unused")
@Repository
public interface OrderDetRepository extends JpaRepository<OrderDet, Long> {

	@Query("select orderdet from OrderDet orderdet where orderdet.order.id =:orderId and orderdet.product.id =:productId")
	Optional<OrderDet> findByOrderIdAndProductId(@Param("orderId") Long orderId, @Param("productId") Long productId);

	@Query("select orderdet from OrderDet orderdet where orderdet.order.id =:orderId")
	Page<OrderDet> findAllByOrderId(@Param("orderId") Long orderId, Pageable pageable);

	@Query("select orderdet from OrderDet orderdet where orderdet.order.customer.login =:login and orderdet.order.orderStatus.orderStatusDescription = '" + Constants.ORDER_STATUS_PENDING + "'")
	Page<OrderDet> findAllByLoginAndOrderStatusPending(@Param("login") String login, Pageable pageable);

	@Query("select orderdet from OrderDet orderdet where orderdet.order.customer.login =:login and orderdet.order.orderStatus.orderStatusDescription != '" + Constants.ORDER_STATUS_PENDING + "'")
	Page<OrderDet> findAllByLoginAndOrderStatus(@Param("login") String login, Pageable pageable);

	@Query("select orderdet from OrderDet orderdet where orderdet.order.customer.login =:login and orderdet.order.id =:orderId and orderdet.order.orderStatus.orderStatusDescription != '" + Constants.ORDER_STATUS_PENDING + "'")
	Page<OrderDet> findAllByLoginAndOrderIdAndOrderStatus(@Param("login") String login, @Param("orderId") Long orderId, Pageable pageable);

	@Query("select sum(orderdet.unitPrice * orderdet.orderQuantity) from OrderDet orderdet where orderdet.order.id =:orderId")
	BigDecimal findOrderTotal(@Param("orderId") Long orderId);

	@Query(
			value = "select sum((CASE WHEN p.new_product_price is null THEN od.unit_price ELSE p.new_product_price END) * od.order_Quantity) from Order_Det od left join promotions p on od.product_id = p.products_id and ((now() between p.promotion_Start_Date and p.promotion_Expiry_Date) or p.promotion_Expiry_Date is null) where od.order_id = ?1",
			nativeQuery = true)
	BigDecimal findOrderTotalWithPromotions(@Param("orderId") Long orderId);
}
