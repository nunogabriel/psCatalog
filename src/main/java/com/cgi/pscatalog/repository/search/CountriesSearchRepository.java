package com.cgi.pscatalog.repository.search;

import com.cgi.pscatalog.domain.Countries;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Countries entity.
 */
public interface CountriesSearchRepository extends ElasticsearchRepository<Countries, Long> {
}
