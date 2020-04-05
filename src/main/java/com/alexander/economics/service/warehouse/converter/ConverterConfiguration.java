package com.alexander.economics.service.warehouse.converter;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

import static org.springframework.beans.factory.config.ConfigurableBeanFactory.SCOPE_SINGLETON;

/**
 * Spring configuration for converters
 */
@Configuration
public class ConverterConfiguration {
    @Bean("mapper")
    @Scope(SCOPE_SINGLETON)
    WarehouseConverter getConverter() {
        return new ModelMapperWarehouseConverter();
    }
}
