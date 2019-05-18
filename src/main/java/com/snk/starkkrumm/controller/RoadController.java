package com.snk.starkkrumm.controller;

import com.snk.starkkrumm.exception.InvalidRoadException;
import com.snk.starkkrumm.model.Road;
import com.snk.starkkrumm.model.RoadRequest;
import com.snk.starkkrumm.service.RoadService;
import com.snk.starkkrumm.service.RoadValidatorService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.snk.starkkrumm.service.RoadValidatorService.REQUEST_NULL_MESSAGE;
import static com.snk.starkkrumm.service.transformer.RoadTransformer.transform;

@Slf4j
@RestController
@RequiredArgsConstructor
public class RoadController {

    private final RoadValidatorService validatorService;
    private final RoadService roadService;

    @PostMapping("/road")
    public void saveRoad(@RequestBody RoadRequest roadRequest, BindingResult bindingResult) {
        log.info("saveRoad endpoint called with RoadRequest: {}", roadRequest);
        if (bindingResult.hasErrors()) {
            throw new InvalidRoadException(REQUEST_NULL_MESSAGE);
        }
        validatorService.validate(roadRequest);
        roadService.save(transform(roadRequest));
    }

    @GetMapping("/road")
    public List<Road> getRoads(@RequestParam("date") String date,
                               @RequestParam("carNumber") Integer carNumber) {
        log.info("getRoad endpoint called with date: {}, carNumber: {}", date, carNumber);
        validatorService.validate(date, carNumber);
        return roadService.getRoads(date, carNumber);
    }

    @DeleteMapping("/road")
    public List<Road> deleteRoad(@RequestParam("date") String date,
                                 @RequestParam("carNumber") Integer carNumber,
                                 @RequestParam("roadNumber") Integer roadNumber) {
        log.info("deleteRoad endpoint called with date: {}, carNumber: {}, roadNumber: {}", date, carNumber, roadNumber);
        validatorService.validate(date, carNumber, roadNumber);
        return roadService.deleteRoad(date, carNumber, roadNumber);
    }

    @PostMapping("/drive/road")
    public List<Road> uploadRoad(@RequestParam("date") String date,
                                 @RequestParam("carNumber") Integer carNumber) {
        log.info("uploadRoad endpoint called with date: {}, carNumber: {}", date, carNumber);
        validatorService.validate(date, carNumber);
        return roadService.uploadRoad(date, carNumber);
    }

}
