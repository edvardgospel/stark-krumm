package com.snk.starkkrumm.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RoadV2 {
    private int roadNumber;
    private int carNumber;
    private String driverName;
    private String departure;
    private String arrival;
    private String month;
    private String year;
    private int distance;
    private double consumption;
}
