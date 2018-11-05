package com.snk.starkkrumm.config;

import com.snk.starkkrumm.service.SnkService;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;

import javax.sql.DataSource;

public class AppConfig {
    @Bean
    public SnkService createSnkService() {
        return new SnkService();
    }

    @Bean
    public DataSource dataSource() {
        return DataSourceBuilder
                .create()
                .driverClassName("org.sqlite.JDBC")
                .url("jdbc:sqlite:snk.db")
                .build();
    }
}
