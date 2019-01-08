package com.snk.starkkrumm.config;

import com.snk.starkkrumm.repository.RoadRepository;
import com.snk.starkkrumm.service.ExcelCreationService;
import com.snk.starkkrumm.service.RoadService;
import com.snk.starkkrumm.service.RoadValidatorService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Slf4j
@Configuration
public class AppConfig {

    @Value("${driver.name}")
    private String driverClassName;

    @Value("${db.url}")
    private String dbUrl;


    @Bean
    public DataSource dataSource() {
        return DataSourceBuilder
                .create()
                .driverClassName(driverClassName)
                .url("jdbc:sqlite:" + System.getProperty("user.home") + dbUrl)
                .build();
    }

    @Bean
    public RoadService roadService(ExcelCreationService excelService, RoadRepository roadRepository) {
        return new RoadService(excelService, roadRepository);
    }

    @Bean
    public RoadValidatorService roadValidatorService() {
        return new RoadValidatorService();
    }

    @Bean
    public RoadRepository roadRepository(DataSource dataSource) {
        return new RoadRepository(dataSource);
    }
}
