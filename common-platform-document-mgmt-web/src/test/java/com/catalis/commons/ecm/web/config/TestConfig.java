package com.catalis.commons.ecm.web.config;

import com.catalis.common.web.error.converter.ExceptionConverterService;
import com.catalis.common.web.idempotency.cache.IdempotencyCache;
import org.mockito.Mockito;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

/**
 * Test configuration for web tests.
 * Provides mock beans for required dependencies.
 */
@TestConfiguration
public class TestConfig {

    /**
     * Creates a mock IdempotencyCache bean for tests.
     * This is required by the IdempotencyWebFilter.
     */
    @Bean
    public IdempotencyCache idempotencyCache() {
        return Mockito.mock(IdempotencyCache.class);
    }

    /**
     * Creates a mock ExceptionConverterService bean for tests.
     * This is required by the GlobalExceptionHandler.
     */
    @Bean
    public ExceptionConverterService exceptionConverterService() {
        return Mockito.mock(ExceptionConverterService.class);
    }
}
