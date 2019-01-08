package com.snk.starkkrumm.service.transformer;

import com.snk.starkkrumm.model.RoadRequest;
import com.snk.starkkrumm.model.RoadV2;

public class RoadTransformer {
    public static RoadV2 transform(RoadRequest request) {
        return RoadV2.builder()
                .roadNumber(request.getRoadNumber())
                .carNumber(request.getCarNumber())
                .driverName(request.getDriverName())
                .departure(request.getDeparture())
                .arrival(request.getArrival())
                .month(getMonthFromDate(request.getDate()))
                .year(getYearFromDate(request.getDate()))
                .distance(getDistance(request))
                .consumption(getConsumption(request))
                .build();
    }

    private static String getMonthFromDate(String date) {
        return date.split("-")[1];
    }

    private static String getYearFromDate(String date) {
        return date.split("-")[0];
    }

    private static int getDistance(RoadRequest request) {
        return request.getDistanceBig() -
                request.getDistanceSmall();
    }

    private static double getConsumption(RoadRequest request) {
        return request.getConsumption1() +
                request.getConsumption2() +
                request.getConsumption3();
    }
}
