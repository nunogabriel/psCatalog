package com.cgi.pscatalog.repository.search;

import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Configuration;

/**
 * Configure a Mock version of OrderStatusSearchRepository to test the
 * application without starting Elasticsearch.
 */
@Configuration
public class OrderStatusSearchRepositoryMockConfiguration {

    @MockBean
    private OrderStatusSearchRepository mockOrderStatusSearchRepository;

}
