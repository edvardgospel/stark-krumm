package com.snk.starkkrumm.config;

import com.google.api.services.drive.Drive;
import com.snk.starkkrumm.service.ExcelService;
import com.snk.starkkrumm.service.GoogleDriveRequestSenderService;
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

    @Value("${db.name}")
    private String dbName;

    @Value("${input.xls}")
    private String inputXls;

    @Value("${output.path}")
    public String outputPath;

    @Bean
    public DataSource dataSource() {
        return DataSourceBuilder
                .create()
                .driverClassName(driverClassName)
                .url("jdbc:sqlite:" + dbPath + "/" + dbName)
                .build();
    }

    @Bean
    public ExcelService excelService() {
        return new ExcelService(inputXls, outputPath);
    }

    @Bean
    public GoogleDriveRequestSenderService googleDriveRequestSenderService(Drive drive) {
        return new GoogleDriveRequestSenderService(drive, outputPath);

    }

}
