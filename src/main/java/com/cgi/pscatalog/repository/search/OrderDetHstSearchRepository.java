package com.cgi.pscatalog.repository.search;

import com.cgi.pscatalog.domain.OrderDetHst;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the OrderDetHst entity.
 */
public interface OrderDetHstSearchRepository extends ElasticsearchRepository<OrderDetHst, Long> {
}
