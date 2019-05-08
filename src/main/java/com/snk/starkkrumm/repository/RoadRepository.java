package com.snk.starkkrumm.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.stereotype.Repository;

import com.snk.starkkrumm.model.Road;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

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

    private final DataSource dataSource;

    public void save(Road road) {
        log.info("save repo()");
        String sql = "INSERT INTO road(roadNumber,carNumber,driverName,departure,arrival,month,year,distance,consumption) VALUES(?,?,?,?,?,?,?,?,?)";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, road.getRoadNumber());
            preparedStatement.setInt(2, road.getCarNumber());
            preparedStatement.setString(3, road.getDriverName());
            preparedStatement.setString(4, road.getDeparture());
            preparedStatement.setString(5, road.getArrival());
            preparedStatement.setString(6, road.getMonth());
            preparedStatement.setString(7, road.getYear());
            preparedStatement.setInt(8, road.getDistance());
            preparedStatement.setDouble(9, road.getConsumption());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            log.warn(e.getMessage());
        }
    }

    public List<Road> findByYearMonthAndCarNumber(String year, String month, Integer carNumber) {
        List<Road> roads = new ArrayList<>();
        String sql = "SELECT * FROM road WHERE year = ? AND month = ? AND carNumber = ?";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, year);
            preparedStatement.setString(2, month);
            preparedStatement.setInt(3, carNumber);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                roads.add(Road.
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
            log.warn(e.getMessage());
        }
        return roads;
    }

    public Road findOne(String year, String month, Integer carNumber, Integer roadNumber) {
        String sql = "SELECT * FROM road WHERE year = ? AND month = ? AND carNumber = ? AND roadNumber = ?";
        Road road = null;
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, year);
            preparedStatement.setString(2, month);
            preparedStatement.setInt(3, carNumber);
            preparedStatement.setInt(4, roadNumber);
            ResultSet resultSet = preparedStatement.executeQuery();
            road = Road.builder()
                    .roadNumber(resultSet.getInt(ROAD_NUMBER))
                    .carNumber(resultSet.getInt(CAR_NUMBER))
                    .driverName(resultSet.getString(DRIVER_NAME))
                    .departure(resultSet.getString(DEPARTURE))
                    .arrival(resultSet.getString(ARRIVAL))
                    .month(resultSet.getString(MONTH))
                    .year(resultSet.getString(YEAR))
                    .distance(resultSet.getInt(DISTANCE))
                    .consumption(resultSet.getDouble(CONSUMPTION))
                    .build();

        } catch (SQLException e) {
            log.warn(e.getMessage());
        }
        return road;
    }

    public void delete(Road road) {
        String sql = "DELETE FROM road WHERE year = ? AND month = ? AND carNumber = ? AND roadNumber = ?";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, road.getYear());
            preparedStatement.setString(2, road.getMonth());
            preparedStatement.setInt(3, road.getCarNumber());
            preparedStatement.setInt(4, road.getRoadNumber());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            log.warn(e.getMessage());
        }
    }
}
