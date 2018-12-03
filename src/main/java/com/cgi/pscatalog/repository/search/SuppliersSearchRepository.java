package com.cgi.pscatalog.repository.search;

import com.cgi.pscatalog.domain.Suppliers;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Suppliers entity.
 */
public interface SuppliersSearchRepository extends ElasticsearchRepository<Suppliers, Long> {
}
