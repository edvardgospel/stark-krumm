package com.snk.starkkrumm.service.transformer;

import com.snk.starkkrumm.model.Road;
import com.snk.starkkrumm.model.RoadRequest;
import com.snk.starkkrumm.model.RoadResponse;

import static com.snk.starkkrumm.util.RoadUtil.*;

public class RoadTransformer {
    public static Road transform(RoadRequest request) {
        return Road.builder()
                .roadNumber(request.getRoadNumber())
                .carNumber(request.getCarNumber())
                .driverName(request.getDriverName())
                .departure(request.getDeparture())
                .arrival(request.getArrival())
                .month(getMonth(request.getDate()))
                .year(getYear(request.getDate()))
                .distance(getDistance(request))
                .consumption(getConsumption(request))
                .build();
    }

    public static RoadResponse transform(Road road) {
        return RoadResponse.builder()
                .roadNumber(road.getRoadNumber())
                .driverName(road.getDriverName())
                .departure(road.getDeparture())
                .arrival(road.getArrival())
                .distance(road.getDistance())
                .consumption(road.getConsumption())
                .build();
    }
}