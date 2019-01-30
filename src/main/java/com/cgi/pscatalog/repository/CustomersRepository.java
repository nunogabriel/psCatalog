package com.cgi.pscatalog.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.cgi.pscatalog.domain.Customers;

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

    @Query("select customers from Customers customers where customers.login =:login and ((CURRENT_TIMESTAMP between customers.customerBeginDate and customers.customerEndDate) or customers.customerEndDate is null)")
	Optional<Customers> findOneByLogin(@Param("login") String login);

}
