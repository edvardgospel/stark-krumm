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
public final class RoadValidatorService {
    public static final String REQUEST_NULL_MESSAGE = "Request can't be null.";
    private static final String REQUEST_INVALID_MESSAGE = "Invalid request.";
    private static final String DRIVER_NAME_REGEX = "[a-zA-Z ]*";
    private static final String DEPARTURE_ARRIVAL_REGEX = "^(0[1-9]|[12][0-9]|3[01])\\.(0[0-9]|1[0-9]|2[0-3])\\.(0[0-9]|[1-5][0-9])$";
    private static final String DATE_REGEX = "^20[1-9][0-9]-(IAN|FEB|MAR|APR|MAI|IUN|IUL|AUG|SEP|OCT|NOV|DEC)$";

    public void validate(RoadRequest request) {
        if (isNull(request) ||
                isNull(request.getRoadNumber()) ||
                isNull(request.getCarNumber()) ||
                isEmpty(request.getDriverName()) ||
                isEmpty(request.getDeparture()) ||
                isEmpty(request.getArrival()) ||
                isEmpty(request.getDate()) ||
                isNull(request.getDistanceBig()) ||
                isNull(request.getDistanceSmall()) ||
                isNull(request.getConsumption1()) ||
                isNull(request.getConsumption2()) ||
                isNull(request.getConsumption3())) {
            log.error("RoadRequest is null.");
            throw new InvalidRoadException(REQUEST_NULL_MESSAGE);
        }
        validateRoadNumber(request.getRoadNumber());
        validateCarNumber(request.getCarNumber());
        validateDriverName(request.getDriverName());
        validateDepartureAndArrival(request.getDeparture(), request.getArrival());
        validateDate(request.getDate());
        validateDistances(request.getDistanceBig(), request.getDistanceSmall());
        validateConsumptions(request.getConsumption1(), request.getConsumption2(), request.getConsumption3());
    }

    public void validate(String date, Integer carNumber) {
        if (isEmpty(date) || isNull(carNumber)) {
            log.error("Month or car number is null.");
            throw new InvalidMonthException(REQUEST_NULL_MESSAGE);
        }
        validateDate(date);
        validateCarNumber(carNumber);
    }

    public void validate(String date, Integer carNumber, Integer roadNumber) {
        if (isEmpty(date) || isNull(carNumber) || isNull(roadNumber)) {
            log.error("Month, car number or road number is null.");
            throw new InvalidMonthException(REQUEST_NULL_MESSAGE);
        }
        validateDate(date);
        validateRoadNumber(roadNumber);
        validateCarNumber(carNumber);
    }

    private void validateRoadNumber(Integer roadNumber) {
        if (roadNumber < 1) {
            log.error("Road number is invalid.");
            throw new InvalidRoadException(REQUEST_INVALID_MESSAGE);
        }
    }

    private void validateCarNumber(Integer carNumber) {
        if (carNumber < 1 || carNumber > 99) {
            log.error("Car number is invalid.");
            throw new InvalidRoadException(REQUEST_INVALID_MESSAGE);
        }
    }

    private void validateDriverName(String driverName) {
        if (!driverName.matches(DRIVER_NAME_REGEX)) {
            log.error("Driver name do not match regex.");
            throw new InvalidRoadException(REQUEST_INVALID_MESSAGE);
        }
    }

    private void validateDepartureAndArrival(String departure, String arrival) {
        if (!departure.matches(DEPARTURE_ARRIVAL_REGEX) || !arrival.matches(DEPARTURE_ARRIVAL_REGEX)) {
            log.error("Departure or arrival do not match regex.");
            throw new InvalidRoadException(REQUEST_INVALID_MESSAGE);
        }
    }

    private void validateDate(String date) {
        if (!date.matches(DATE_REGEX)) {
            log.error("Date do not match regex.");
            throw new InvalidRoadException(REQUEST_INVALID_MESSAGE);
        }
    }

    private void validateDistances(Integer distanceBig, Integer distanceSmall) {
        if (distanceSmall >= distanceBig ||
                distanceBig < 1 ||
                distanceBig > 9999 ||
                distanceSmall < 1 ||
                distanceSmall > 9999) {
            log.error("Distance is invalid.");
            throw new InvalidRoadException(REQUEST_INVALID_MESSAGE);
        }
    }

    private void validateConsumptions(Double consumption1, Double consumption2, Double consumption3) {
        if (consumption1 < 1.0 || consumption1 >= 1000.0 ||
                consumption2 < 1.0 || consumption2 >= 1000.0 ||
                consumption3 < 1.0 || consumption3 >= 1000.0) {
            log.error("Consumption is invalid.");
            throw new InvalidRoadException(REQUEST_INVALID_MESSAGE);
        }
    }
}
