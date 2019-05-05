package com.snk.starkkrumm.service;

import com.snk.starkkrumm.exception.InvalidMonthException;
import com.snk.starkkrumm.exception.InvalidRoadException;
import com.snk.starkkrumm.model.RoadRequest;
import org.springframework.stereotype.Service;

import static java.util.Objects.isNull;
import static org.thymeleaf.util.StringUtils.isEmpty;

@Service
public class RoadValidatorService {
    public static final String REQUEST_NULL_MESSAGE = "Request can't be null";

    public void validate(RoadRequest roadRequest) {
        if (isNull(roadRequest) ||
                isNull(roadRequest.getRoadNumber()) ||
                isNull(roadRequest.getCarNumber()) ||
                isEmpty(roadRequest.getDriverName()) ||
                isEmpty(roadRequest.getDeparture()) ||
                isEmpty(roadRequest.getArrival()) ||
                isEmpty(roadRequest.getDate()) ||
                isNull(roadRequest.getDistanceBig()) ||
                isNull(roadRequest.getDistanceSmall()) ||
                isNull(roadRequest.getConsumption1()) ||
                isNull(roadRequest.getConsumption2()) ||
                isNull(roadRequest.getConsumption3())) {
            throw new InvalidRoadException(REQUEST_NULL_MESSAGE);
        }
    }

    public void validate(String month, Integer carNumber) {
        if (isEmpty(month) || isNull(carNumber)) {
            throw new InvalidMonthException(REQUEST_NULL_MESSAGE);
        }
    }
}
