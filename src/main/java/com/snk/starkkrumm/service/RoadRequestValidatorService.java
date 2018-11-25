package com.snk.starkkrumm.service;

import com.snk.starkkrumm.exception.InvalidRoadRequestException;
import com.snk.starkkrumm.model.RoadRequest;

import static java.util.Objects.isNull;
import static org.thymeleaf.util.StringUtils.isEmpty;

public class RoadRequestValidatorService {
    public void validate(RoadRequest request) {
        if (isNull(request) ||
                isNull(request.getRoadNumber()) ||
                isNull(request.getCarNumber()) ||
                isEmpty(request.getDriverName()) ||
                isEmpty(request.getDeparture().toString()) ||
                isEmpty(request.getArrival().toString()) ||
                isNull(request.getDistance()) ||
                isNull(request.getConsumption())) {
            throw new InvalidRoadRequestException("Request can't be null");
        }
    }
}
