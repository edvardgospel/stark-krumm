package com.snk.starkkrumm.util;

import com.snk.starkkrumm.model.RoadRequest;

public class RoadUtil {
    public static String getYearFromDate(String date) {
        return date.split("-")[0];
    }

    public static String getMonthFromDate(String date) {
        return date.split("-")[1];
    }

    public static int getDistance(RoadRequest request) {
        return request.getDistanceBig() -
                request.getDistanceSmall();
    }

    public static double getConsumption(RoadRequest request) {
        return request.getConsumption1() +
                request.getConsumption2() +
                request.getConsumption3();
    }
}
