package com.snk.starkkrumm.config;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.google.api.services.drive.Drive;
import com.snk.starkkrumm.repository.RoadRepository;
import com.snk.starkkrumm.service.ExcelCreationService;
import com.snk.starkkrumm.service.GoogleDriveRequestSenderService;
import com.snk.starkkrumm.service.RoadService;
import com.snk.starkkrumm.service.RoadValidatorService;

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
                .url("jdbc:sqlite:" /*+ System.getProperty("user.home")*/ + dbUrl)
                .build();
    }

    @Bean
    public ExcelCreationService excelCreationService(@Value("${input.xls}") String inputXls,
                                                     @Value("${output.path}") String outputPath) {
        return new ExcelCreationService(inputXls, outputPath);
    }

    @Bean
    public GoogleDriveRequestSenderService googleDriveRequestSenderService(Drive drive) {
        return new GoogleDriveRequestSenderService(drive);
    }

    @Bean
    public RoadService roadService(ExcelCreationService excelService, RoadRepository roadRepository,
                                   GoogleDriveRequestSenderService googleDriveRequestSenderService) {
        return new RoadService(excelService, roadRepository, googleDriveRequestSenderService);
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
