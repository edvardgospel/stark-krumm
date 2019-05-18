package com.snk.starkkrumm.config;

import com.snk.starkkrumm.service.ExcelCreationService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
public class AppConfig {

    @Value("${driver.name}")
    private String driverClassName;

    @Value("${db.path}")
    private String dbPath;

    @Value("${db.url}")
    private String dbUrl;

    @Value("${input.xls}")
    private String inputXls;

    @Value("${output.path}")
    String outputPath;

    @Bean
    public DataSource dataSource() {
        return DataSourceBuilder
                .create()
                .driverClassName(driverClassName)
                .url("jdbc:sqlite:" + dbPath + dbUrl)
                .build();
    }

    @Bean
    public ExcelCreationService excelCreationService() {
        return new ExcelCreationService(inputXls, outputPath);
    }


}
