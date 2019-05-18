package com.snk.starkkrumm.model;

import lombok.Builder;
import lombok.Data;

import javax.annotation.Nullable;

@Data
@Builder
public class Road implements Comparable<Road> {
    private int roadNumber;
    private int carNumber;
    private String driverName;
    private String departure;
    private String arrival;
    private String month;
    private String year;
    private int distance;
    private double consumption;

    @Override
    public int compareTo(@Nullable Road road) {
        assert road != null;
        return Integer.valueOf(this.getArrival().substring(0, 2))
                .compareTo(Integer.valueOf(road.getArrival().substring(0, 2)));
    }
}
