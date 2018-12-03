package com.cgi.pscatalog.repository.search;

import com.cgi.pscatalog.domain.OrderDet;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the OrderDet entity.
 */
public interface OrderDetSearchRepository extends ElasticsearchRepository<OrderDet, Long> {
}
