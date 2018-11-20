package com.snk.starkkrumm.model;

import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Data
@Builder
public class Road {
    private String roadNumber;
    private int carNumber;
    private String name;
    private Date departureDate;
    private String departureTime;
    private Date arrivalDate;
    private String arrivalTime;
    private int kilometer;
    private double consumption;
}
