package com.cgi.pscatalog.repository.search;

import com.cgi.pscatalog.domain.Promotions;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Promotions entity.
 */
public interface PromotionsSearchRepository extends ElasticsearchRepository<Promotions, Long> {
}
