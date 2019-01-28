package com.snk.starkkrumm.service.transformer;

import com.snk.starkkrumm.model.Road;
import com.snk.starkkrumm.model.RoadRequest;

import static com.snk.starkkrumm.util.RoadUtil.*;

public class RoadTransformer {
    public static Road transform(RoadRequest request) {
        return Road.builder()
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
}
