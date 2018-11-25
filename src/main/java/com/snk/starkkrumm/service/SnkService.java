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
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

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
            pstmt.setString(4, road.getDeparture().toString());
            pstmt.setString(5, road.getArrival().toString());
            pstmt.setInt(6, road.getDistance());
            pstmt.setDouble(7, road.getConsumption());
            pstmt.executeUpdate();
            log.info("UPDATED: road: {} ", road);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public List<RoadRequest> findRequestByDeparture(LocalDateTime departure) {
        List<RoadRequest> roadRequests = new ArrayList<>();
        String sql = "SELECT * FROM road WHERE departure LIKE(?)";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, departure.format(DateTimeFormatter.ofPattern("yyyy-MM")) + "%");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {

                roadRequests.add(RoadRequest
                        .builder()
                        .roadNumber(rs.getInt("roadNumber"))
                        .carNumber(rs.getInt("carNumber"))
                        .driverName(rs.getString("driverName"))
                        .departure(LocalDateTime.parse(rs.getString("departure")))
                        .arrival(LocalDateTime.parse(rs.getString("arrival")))
                        .distance(rs.getInt("distance"))
                        .consumption(rs.getDouble("consumption"))
                        .build());

            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        log.info("roadRequests returned: ", roadRequests);
        return roadRequests;
    }

    public Map<Integer, List<RoadRequest>> mapRoad(List<RoadRequest> roadRequests) {
        Map<Integer, List<RoadRequest>> roadRequestMap = new HashMap<>();
        for (RoadRequest roadRequest : roadRequests) {
            if (!roadRequestMap.containsKey(roadRequest.getCarNumber())) {
                roadRequestMap.put(roadRequest.getCarNumber(), new ArrayList<>(Arrays.asList(roadRequest)));
            } else {
                List<RoadRequest> rr = roadRequestMap.get(roadRequest.getCarNumber());
                rr.add(roadRequest);
                roadRequestMap.put(roadRequest.getCarNumber(), rr);
            }
        }
        log.info("MAP: " + roadRequestMap);
        return roadRequestMap;
    }
}
