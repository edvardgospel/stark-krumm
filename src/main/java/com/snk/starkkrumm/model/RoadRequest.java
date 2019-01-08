package com.snk.starkkrumm.model;


import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RoadRequest {
    private int roadNumber;
    private int carNumber;
    private String driverName;
    private String departure;
    private String arrival;
    private String date;
    private int distanceBig;
    private int distanceSmall;
    private double consumption1;
    private double consumption2;
    private double consumption3;
}
