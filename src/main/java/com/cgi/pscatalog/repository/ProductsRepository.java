package com.cgi.pscatalog.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.cgi.pscatalog.domain.Products;


/**
 * Spring Data  repository for the Products entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ProductsRepository extends JpaRepository<Products, Long> {

	@Query(
			value = "select a.id, a.product_name, a.product_description, a.product_type, a.product_img, a.product_img_content_type, CASE WHEN b.new_product_price is null THEN a.product_price ELSE b.new_product_price END as product_price from products a left join promotions b on a.id = b.products_id and ((now() between a.product_start_date and a.product_end_date) or a.product_end_date is null) and ((now() between b.promotion_start_date and b.promotion_expiry_date) or b.promotion_expiry_date is null)",
		    countQuery = "select count(*) from products a left join promotions b on a.id = b.products_id and ((now() between a.product_start_date and a.product_end_date) or a.product_end_date is null) and ((now() between b.promotion_start_date and b.promotion_expiry_date) or b.promotion_expiry_date is null)",
			nativeQuery = true)
	Page<Object[]> findAllProductsWithPromotions(Pageable pageable);

	@Query(
			value = "select a.id, a.product_name, a.product_description, a.product_type, a.product_img, a.product_img_content_type, CASE WHEN b.new_product_price is null THEN a.product_price ELSE b.new_product_price END as product_price from products a left join promotions b on a.id = b.products_id and ((now() between b.promotion_Start_Date and b.promotion_Expiry_Date) or b.promotion_Expiry_Date is null) where a.id = ?1",
			countQuery = "select count(*) from products a left join promotions b on a.id = b.products_id and ((now() between b.promotion_Start_Date and b.promotion_Expiry_Date) or b.promotion_Expiry_Date is null) where a.id = ?1",
			nativeQuery = true)
	Page<Object[]> findAllProductsWithPromotionsByProductId(Long productId, Pageable pageable);

	@Query(
			value = "select a.id, a.product_name, a.product_description, a.product_type, a.product_img, a.product_img_content_type, CASE WHEN b.new_product_price is null THEN a.product_price ELSE b.new_product_price END as product_price from products a INNER JOIN customers_products AS cp on a.id = cp.products_id and cp.customers_id = ?1 left join promotions b on a.id = b.products_id and ((now() between a.product_start_date and a.product_end_date) or a.product_end_date is null) and ((now() between b.promotion_start_date and b.promotion_expiry_date) or b.promotion_expiry_date is null)",
		    countQuery = "select count(*) from products a INNER JOIN customers_products AS cp on a.id = cp.products_id and cp.customers_id = ?1 left join promotions b on a.id = b.products_id and ((now() between a.product_start_date and a.product_end_date) or a.product_end_date is null) and ((now() between b.promotion_start_date and b.promotion_expiry_date) or b.promotion_expiry_date is null)",
			nativeQuery = true)
	Page<Object[]> findAllProductsWithPromotionsByCustomersId(Long customerId, Pageable pageable);

	@Query(
			value = "select a.id, a.product_name, a.product_description, a.product_type, a.product_img, a.product_img_content_type, CASE WHEN b.new_product_price is null THEN a.product_price ELSE b.new_product_price END as product_price from products a INNER JOIN customers_products AS cp on a.id = cp.products_id and cp.customers_id = ?1 left join promotions b on a.id = b.products_id and ((now() between b.promotion_Start_Date and b.promotion_Expiry_Date) or b.promotion_Expiry_Date is null) where a.id = ?2",
			countQuery = "select count(*) from products a INNER JOIN customers_products AS cp on a.id = cp.products_id and cp.customers_id = ?1 left join promotions b on a.id = b.products_id and ((now() between b.promotion_Start_Date and b.promotion_Expiry_Date) or b.promotion_Expiry_Date is null) where a.id = ?2",
			nativeQuery = true)
	Page<Object[]> findAllProductsWithPromotionsByCustomersIdAndProductId(Long customerId, Long productId, Pageable pageable);

}
