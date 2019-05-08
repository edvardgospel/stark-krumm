package com.snk.starkkrumm.service;

import static java.util.Objects.isNull;
import static org.thymeleaf.util.StringUtils.isEmpty;

import org.springframework.stereotype.Service;

import com.snk.starkkrumm.exception.InvalidMonthException;
import com.snk.starkkrumm.exception.InvalidRoadException;
import com.snk.starkkrumm.model.RoadRequest;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class RoadValidatorService {
    public static final String REQUEST_NULL_MESSAGE = "Request can't be null.";
    private static final String REQUEST_INVALID_MESSAGE = "Invalid request.";
    private static final String REGEX = "^(0[1-9]|[12][0-9]|3[01])\\.(0[0-9]|1[0-9]|2[0-3])\\.(0[0-9]|[1-5][0-9])$";

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
        validateRoadNumber(roadRequest.getRoadNumber());
        validateCarNumber(roadRequest.getCarNumber());
        validateDriverName(roadRequest.getDriverName());
        validateDepartureAndArrival(roadRequest.getDeparture(), roadRequest.getArrival());
        validateDate(roadRequest.getDate());
    }

    private void validateRoadNumber(Integer roadNumber) {
        if (roadNumber < 1) {
            throw new InvalidRoadException(REQUEST_INVALID_MESSAGE);
        }
    }

    private void validateCarNumber(Integer carNumber) {
        if (carNumber <= 1 || carNumber > 99) {
            throw new InvalidRoadException(REQUEST_INVALID_MESSAGE);
        }
    }

    private void validateDriverName(String driverName) {
        if (!driverName.matches("[a-zA-Z ]*")) {
            log.info("drivername not matches regex");
            throw new InvalidRoadException(REQUEST_INVALID_MESSAGE);
        }
    }

    private void validateDepartureAndArrival(String departure, String arrival) {
        if (!departure.matches(REGEX) || !arrival.matches(REGEX)) {
            log.info("departure or arrival not matches regex");
            throw new InvalidRoadException(REQUEST_INVALID_MESSAGE);
        }
    }

    private void validateDate(String date) {
        if (!date.matches("^20[1-9][0-9]-(IAN|FEB|MAR|APR|MAI|IUN|IUL|AUG|SEP|OCT|NOV|DEC)$")) {
            log.info("date not matches regex");
            throw new InvalidRoadException(REQUEST_INVALID_MESSAGE);
        }
    }

    public void validate(String month, Integer carNumber) {
        if (isEmpty(month) || isNull(carNumber)) {
            throw new InvalidMonthException(REQUEST_NULL_MESSAGE);
        }
    }

    public void validate(String month, Integer carNumber, Integer roadNumber) {
        if (isEmpty(month) || isNull(carNumber) || isNull(roadNumber)) {
            throw new InvalidMonthException(REQUEST_NULL_MESSAGE);
        }
    }
}
