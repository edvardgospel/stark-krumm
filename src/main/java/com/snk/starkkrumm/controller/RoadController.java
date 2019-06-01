package com.snk.starkkrumm.controller;

import com.snk.starkkrumm.model.RoadRequest;
import com.snk.starkkrumm.model.RoadResponse;
import com.snk.starkkrumm.service.RoadService;
import com.snk.starkkrumm.service.RoadValidatorService;
import com.snk.starkkrumm.service.transformer.RoadTransformer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

import static com.snk.starkkrumm.service.transformer.RoadTransformer.transform;

@Slf4j
@RestController
@RequiredArgsConstructor
public class RoadController {

    private final RoadValidatorService validatorService;
    private final RoadService roadService;

    @PostMapping("/road")
    public List<RoadResponse> saveRoad(@RequestBody RoadRequest roadRequest) {
        log.info("saveRoad endpoint called with RoadRequest: {}", roadRequest);
        validatorService.validate(roadRequest);
        return roadService.save(transform(roadRequest)).stream()
                .map(RoadTransformer::transform)
                .collect(Collectors.toList());
    }

    @GetMapping("/road")
    public List<RoadResponse> getRoads(@RequestParam("date") String date,
                                       @RequestParam("carNumber") Integer carNumber) {
        log.info("getRoad endpoint called with date: {}, carNumber: {}", date, carNumber);
        validatorService.validate(date, carNumber);
        return roadService.getRoads(date, carNumber).stream()
                .map(RoadTransformer::transform)
                .collect(Collectors.toList());
    }

    @DeleteMapping("/road")
    public List<RoadResponse> deleteRoad(@RequestParam("date") String date,
                                         @RequestParam("carNumber") Integer carNumber,
                                         @RequestParam("roadNumber") Integer roadNumber) {
        log.info("deleteRoad endpoint called with date: {}, carNumber: {}, roadNumber: {}", date, carNumber, roadNumber);
        validatorService.validate(date, carNumber, roadNumber);
        return roadService.deleteRoad(date, carNumber, roadNumber).stream()
                .map(RoadTransformer::transform)
                .collect(Collectors.toList());
    }

    @PostMapping("/drive/road")
    public List<RoadResponse> uploadRoad(@RequestParam("date") String date,
                                         @RequestParam("carNumber") Integer carNumber) {
        log.info("uploadRoad endpoint called with date: {}, carNumber: {}", date, carNumber);
        validatorService.validate(date, carNumber);
        return roadService.uploadRoad(date, carNumber).stream()
                .map(RoadTransformer::transform)
                .collect(Collectors.toList());
    }

}
