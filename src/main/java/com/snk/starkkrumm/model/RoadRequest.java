package com.snk.starkkrumm.model;


import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RoadRequest {
    private Integer roadNumber;
    private Integer carNumber;
    private String driverName;
    private String departure;
    private String arrival;
    private String date;
    private Integer distanceBig;
    private Integer distanceSmall;
    private Integer consumption1;
    private Integer consumption2;
    private Integer consumption3;
}
