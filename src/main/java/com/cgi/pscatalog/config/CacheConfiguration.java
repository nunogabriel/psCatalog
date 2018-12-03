package com.cgi.pscatalog.config;

import java.time.Duration;

import org.ehcache.config.builders.*;
import org.ehcache.jsr107.Eh107Configuration;

import io.github.jhipster.config.jcache.BeanClassLoaderAwareJCacheRegionFactory;
import io.github.jhipster.config.JHipsterProperties;

import org.springframework.boot.autoconfigure.cache.JCacheManagerCustomizer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.*;

@Configuration
@EnableCaching
public class CacheConfiguration {

    private final javax.cache.configuration.Configuration<Object, Object> jcacheConfiguration;

    public CacheConfiguration(JHipsterProperties jHipsterProperties) {
        BeanClassLoaderAwareJCacheRegionFactory.setBeanClassLoader(this.getClass().getClassLoader());
        JHipsterProperties.Cache.Ehcache ehcache =
            jHipsterProperties.getCache().getEhcache();

        jcacheConfiguration = Eh107Configuration.fromEhcacheCacheConfiguration(
            CacheConfigurationBuilder.newCacheConfigurationBuilder(Object.class, Object.class,
                ResourcePoolsBuilder.heap(ehcache.getMaxEntries()))
                .withExpiry(ExpiryPolicyBuilder.timeToLiveExpiration(Duration.ofSeconds(ehcache.getTimeToLiveSeconds())))
                .build());
    }

    @Bean
    public JCacheManagerCustomizer cacheManagerCustomizer() {
        return cm -> {
            cm.createCache(com.cgi.pscatalog.repository.UserRepository.USERS_BY_LOGIN_CACHE, jcacheConfiguration);
            cm.createCache(com.cgi.pscatalog.repository.UserRepository.USERS_BY_EMAIL_CACHE, jcacheConfiguration);
            cm.createCache(com.cgi.pscatalog.domain.User.class.getName(), jcacheConfiguration);
            cm.createCache(com.cgi.pscatalog.domain.Authority.class.getName(), jcacheConfiguration);
            cm.createCache(com.cgi.pscatalog.domain.User.class.getName() + ".authorities", jcacheConfiguration);
            cm.createCache(com.cgi.pscatalog.domain.Suppliers.class.getName(), jcacheConfiguration);
            cm.createCache(com.cgi.pscatalog.domain.Suppliers.class.getName() + ".addresses", jcacheConfiguration);
            cm.createCache(com.cgi.pscatalog.domain.Suppliers.class.getName() + ".products", jcacheConfiguration);
            cm.createCache(com.cgi.pscatalog.domain.Customers.class.getName(), jcacheConfiguration);
            cm.createCache(com.cgi.pscatalog.domain.Customers.class.getName() + ".orders", jcacheConfiguration);
            cm.createCache(com.cgi.pscatalog.domain.Customers.class.getName() + ".addresses", jcacheConfiguration);
            cm.createCache(com.cgi.pscatalog.domain.Customers.class.getName() + ".products", jcacheConfiguration);
            cm.createCache(com.cgi.pscatalog.domain.Products.class.getName(), jcacheConfiguration);
            cm.createCache(com.cgi.pscatalog.domain.Products.class.getName() + ".orderDets", jcacheConfiguration);
            cm.createCache(com.cgi.pscatalog.domain.Products.class.getName() + ".customers", jcacheConfiguration);
            cm.createCache(com.cgi.pscatalog.domain.Promotions.class.getName(), jcacheConfiguration);
            cm.createCache(com.cgi.pscatalog.domain.Addresses.class.getName(), jcacheConfiguration);
            cm.createCache(com.cgi.pscatalog.domain.Addresses.class.getName() + ".orders", jcacheConfiguration);
            cm.createCache(com.cgi.pscatalog.domain.Countries.class.getName(), jcacheConfiguration);
            cm.createCache(com.cgi.pscatalog.domain.Countries.class.getName() + ".addresses", jcacheConfiguration);
            cm.createCache(com.cgi.pscatalog.domain.Orders.class.getName(), jcacheConfiguration);
            cm.createCache(com.cgi.pscatalog.domain.Orders.class.getName() + ".orderDets", jcacheConfiguration);
            cm.createCache(com.cgi.pscatalog.domain.OrdersHst.class.getName(), jcacheConfiguration);
            cm.createCache(com.cgi.pscatalog.domain.OrderStatus.class.getName(), jcacheConfiguration);
            cm.createCache(com.cgi.pscatalog.domain.OrderStatus.class.getName() + ".orders", jcacheConfiguration);
            cm.createCache(com.cgi.pscatalog.domain.OrderDet.class.getName(), jcacheConfiguration);
            cm.createCache(com.cgi.pscatalog.domain.OrderDetHst.class.getName(), jcacheConfiguration);
            // jhipster-needle-ehcache-add-entry
        };
    }
}
