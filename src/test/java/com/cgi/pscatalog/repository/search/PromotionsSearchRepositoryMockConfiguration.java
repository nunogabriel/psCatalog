package com.cgi.pscatalog.repository.search;

import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Configuration;

/**
 * Configure a Mock version of PromotionsSearchRepository to test the
 * application without starting Elasticsearch.
 */
@Configuration
public class PromotionsSearchRepositoryMockConfiguration {

    @MockBean
    private PromotionsSearchRepository mockPromotionsSearchRepository;

}
