package com.snk.starkkrumm.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RoadResponse {
    private Integer roadNumber;
    private String driverName;
    private String departure;
    private String arrival;
    private Integer distance;
    private Double consumption;
}
