package com.cgi.pscatalog.repository.search;

import com.cgi.pscatalog.domain.Addresses;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Addresses entity.
 */
public interface AddressesSearchRepository extends ElasticsearchRepository<Addresses, Long> {
}
