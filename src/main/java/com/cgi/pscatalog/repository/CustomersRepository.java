package com.cgi.pscatalog.repository;

import com.cgi.pscatalog.domain.Customers;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Spring Data  repository for the Customers entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CustomersRepository extends JpaRepository<Customers, Long> {

    @Query(value = "select distinct customers from Customers customers left join fetch customers.products",
        countQuery = "select count(distinct customers) from Customers customers")
    Page<Customers> findAllWithEagerRelationships(Pageable pageable);

    @Query(value = "select distinct customers from Customers customers left join fetch customers.products")
    List<Customers> findAllWithEagerRelationships();

    @Query("select customers from Customers customers left join fetch customers.products where customers.id =:id")
    Optional<Customers> findOneWithEagerRelationships(@Param("id") Long id);

}
