package com.snk.starkkrumm.repository;

import com.snk.starkkrumm.model.Road;
import com.snk.starkkrumm.model.RoadV2;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static java.time.format.DateTimeFormatter.ofPattern;

@Slf4j
@Repository
@RequiredArgsConstructor
public class RoadRepository {

    private static final String ROAD_NUMBER = "roadNumber";
    private static final String CAR_NUMBER = "carNumber";
    private static final String DRIVER_NAME = "driverName";
    private static final String DEPARTURE = "departure";
    private static final String ARRIVAL = "arrival";
    private static final String MONTH = "month";
    private static final String YEAR = "year";
    private static final String DISTANCE = "distance";
    private static final String CONSUMPTION = "consumption";
    private static final String YYYY_MM = "yyyy-MM";
    private final DataSource dataSource;

    public void save(Road road) {
        String sql = "INSERT INTO road(roadNumber,carNumber,driverName,departure,arrival,distance,consumption) VALUES(?,?,?,?,?,?,?)";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, road.getRoadNumber());
            preparedStatement.setInt(2, road.getCarNumber());
            preparedStatement.setString(3, road.getDriverName());
            preparedStatement.setString(4, road.getDeparture().toString());
            preparedStatement.setString(5, road.getArrival().toString());
            preparedStatement.setInt(6, road.getDistance());
            preparedStatement.setDouble(7, road.getConsumption());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public List<Road> findByDeparture(LocalDateTime departure) {
        List<Road> roads = new ArrayList<>();
        String sql = "SELECT * FROM road WHERE departure LIKE(?)";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, departure.format(ofPattern(YYYY_MM)) + "%");
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                roads.add(Road.
                        builder()
                        .roadNumber(resultSet.getInt(ROAD_NUMBER))
                        .carNumber(resultSet.getInt(CAR_NUMBER))
                        .driverName(resultSet.getString(DRIVER_NAME))
                        .departure(LocalDateTime.parse(resultSet.getString(DEPARTURE)))
                        .arrival(LocalDateTime.parse(resultSet.getString(ARRIVAL)))
                        .distance(resultSet.getInt(DISTANCE))
                        .consumption(resultSet.getDouble(CONSUMPTION))
                        .build());

            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return roads;
    }

    public void save(RoadV2 roadV2) {
        log.info("save repo()");
        String sql = "INSERT INTO road_v2(roadNumber,carNumber,driverName,departure,arrival,month,year,distance,consumption) VALUES(?,?,?,?,?,?,?,?,?)";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, roadV2.getRoadNumber());
            preparedStatement.setInt(2, roadV2.getCarNumber());
            preparedStatement.setString(3, roadV2.getDriverName());
            preparedStatement.setString(4, roadV2.getDeparture());
            preparedStatement.setString(5, roadV2.getArrival());
            preparedStatement.setString(6, roadV2.getMonth());
            preparedStatement.setString(7, roadV2.getYear());
            preparedStatement.setInt(8, roadV2.getDistance());
            preparedStatement.setDouble(9, roadV2.getConsumption());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public List<RoadV2> findByMonthAndCarNumber(String month, Integer carNumber) {
        List<RoadV2> roads = new ArrayList<>();
        String sql = "SELECT * FROM road_v2 WHERE month = ? AND carNumber = ?";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, month);
            preparedStatement.setInt(2, carNumber);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                roads.add(RoadV2.
                        builder()
                        .roadNumber(resultSet.getInt(ROAD_NUMBER))
                        .carNumber(resultSet.getInt(CAR_NUMBER))
                        .driverName(resultSet.getString(DRIVER_NAME))
                        .departure(resultSet.getString(DEPARTURE))
                        .arrival(resultSet.getString(ARRIVAL))
                        .month(resultSet.getString(MONTH))
                        .year(resultSet.getString(YEAR))
                        .distance(resultSet.getInt(DISTANCE))
                        .consumption(resultSet.getDouble(CONSUMPTION))
                        .build());

            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return roads;
    }
}
