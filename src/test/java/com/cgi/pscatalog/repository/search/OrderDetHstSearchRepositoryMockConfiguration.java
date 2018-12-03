package com.cgi.pscatalog.repository.search;

import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Configuration;

/**
 * Configure a Mock version of OrderDetHstSearchRepository to test the
 * application without starting Elasticsearch.
 */
@Configuration
public class OrderDetHstSearchRepositoryMockConfiguration {

    @MockBean
    private OrderDetHstSearchRepository mockOrderDetHstSearchRepository;

}
