package com.cgi.pscatalog.repository.search;

import com.cgi.pscatalog.domain.Customers;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Customers entity.
 */
public interface CustomersSearchRepository extends ElasticsearchRepository<Customers, Long> {
}
