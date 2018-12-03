package com.cgi.pscatalog.repository.search;

import com.cgi.pscatalog.domain.OrderStatus;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the OrderStatus entity.
 */
public interface OrderStatusSearchRepository extends ElasticsearchRepository<OrderStatus, Long> {
}
