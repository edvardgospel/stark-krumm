package com.snk.starkkrumm.repository;

import com.snk.starkkrumm.model.Road;
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

    public static final String ROAD_NUMBER = "roadNumber";
    public static final String CAR_NUMBER = "carNumber";
    public static final String DRIVER_NAME = "driverName";
    public static final String DEPARTURE = "departure";
    public static final String ARRIVAL = "arrival";
    public static final String DISTANCE = "distance";
    public static final String CONSUMPTION = "consumption";
    public static final String YYYY_MM = "yyyy-MM";
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
}
