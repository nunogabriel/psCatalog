package com.cgi.pscatalog.repository.search;

import com.cgi.pscatalog.domain.OrdersHst;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the OrdersHst entity.
 */
public interface OrdersHstSearchRepository extends ElasticsearchRepository<OrdersHst, Long> {
}
