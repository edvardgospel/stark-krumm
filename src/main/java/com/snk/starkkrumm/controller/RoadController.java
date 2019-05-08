package com.snk.starkkrumm.controller;

import static com.snk.starkkrumm.service.RoadValidatorService.REQUEST_NULL_MESSAGE;
import static com.snk.starkkrumm.service.transformer.RoadTransformer.transform;
import static org.apache.logging.log4j.util.Strings.isBlank;

import java.util.Collections;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.snk.starkkrumm.exception.InvalidRoadException;
import com.snk.starkkrumm.model.Road;
import com.snk.starkkrumm.model.RoadRequest;
import com.snk.starkkrumm.service.RoadService;
import com.snk.starkkrumm.service.RoadValidatorService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequiredArgsConstructor
public class RoadController {

    private final RoadValidatorService validatorService;
    private final RoadService roadService;

    @GetMapping("/")
    public String home() {
        return "admin";
    }

    @ResponseBody
    @PostMapping("/road")
    public void saveRoad(@RequestBody RoadRequest roadRequest, BindingResult bindingResult) {
        log.info("roadRequest: {}" + roadRequest);
        if (bindingResult.hasErrors()) {
            throw new InvalidRoadException(REQUEST_NULL_MESSAGE);
        }
        validatorService.validate(roadRequest);
        roadService.save(transform(roadRequest));
    }

    @ResponseBody
    @GetMapping("/road")
    public List<Road> getRoads(@RequestParam("date") String date, @RequestParam("carNumber") Integer carNumber) {
        validatorService.validate(date, carNumber);
        if (isBlank(date)) {
            return Collections.emptyList();
        }
        return roadService.getRoads(date, carNumber);
    }

    @ResponseBody
    @DeleteMapping("road")
    public List<Road> deleteRoad(@RequestParam("date") String date,
                                 @RequestParam("carNumber") Integer carNumber,
                                 @RequestParam("roadNumber") Integer roadNumber) {
        validatorService.validate(date, carNumber, roadNumber);
        return roadService.deleteRoad(date, carNumber, roadNumber);
    }

    @ResponseBody
    @PostMapping("/drive/road")
    public void uploadRoad(@RequestParam("date") String date, @RequestParam("carNumber") Integer carNumber) {
        validatorService.validate(date, carNumber);
        roadService.uploadRoad(date, carNumber);
    }

}
