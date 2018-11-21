package com.snk.starkkrumm.service;

import com.snk.starkkrumm.model.RoadRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;

@Slf4j
@Service
public class SnkService {
    HSSFWorkbook sheets = new HSSFWorkbook();
    HSSFSheet sheet = sheets.createSheet("title of sheet");

    //

    public void instertData(RoadRequest road) throws IOException {
        // dataValidationService.validate(data);
        Row row = sheet.createRow(0);
        row.createCell(0).setCellValue("ezaz");
        //row.createCell(1).setCellValue(data);
        saveDataToExcel();
    }

    public void saveDataToExcel() throws IOException {
        FileOutputStream outputStream = new FileOutputStream("D:\\testout.xls");
        sheets.write(outputStream);
    }

    DataSource dataSource = DataSourceBuilder
            .create()
            .driverClassName("org.sqlite.JDBC")
            .url("jdbc:sqlite:stark-krumm.db")
            .build();

    public void insert(RoadRequest road) {
        String sql = "INSERT INTO road(roadNumber,carNumber,driverName,departure,arrival,distance,consumption) VALUES(?,?,?,?,?,?,?)";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, road.getRoadNumber());
            pstmt.setInt(2, road.getCarNumber());
            pstmt.setString(3, road.getDriverName());
            pstmt.setTimestamp(4, Timestamp.valueOf(road.getDeparture()));
            pstmt.setTimestamp(5, Timestamp.valueOf(road.getArrival()));
            pstmt.setInt(6, road.getDistance());
            pstmt.setDouble(7, road.getConsumption());
            pstmt.executeUpdate();
            log.info("UPDATED: road: {} ", road);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
}
