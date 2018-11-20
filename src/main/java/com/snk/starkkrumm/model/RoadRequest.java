package com.snk.starkkrumm.model;

import lombok.Builder;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

import static org.springframework.format.annotation.DateTimeFormat.ISO.DATE_TIME;

@Data
@Builder
public class RoadRequest {
    private int roadNumber;
    private int carNumber;
    private String driverName;
    @DateTimeFormat(iso = DATE_TIME)
    private LocalDateTime departure;
    @DateTimeFormat(iso = DATE_TIME)
    private LocalDateTime arrival;
    private int distance;
    private double consumption;
}
