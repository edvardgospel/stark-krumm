package com.snk.starkkrumm.controller;

import com.snk.starkkrumm.model.Road;
import com.snk.starkkrumm.model.RoadRequest;
import com.snk.starkkrumm.model.RoadV2;
import com.snk.starkkrumm.service.RoadService;
import com.snk.starkkrumm.service.RoadValidatorService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

import static com.snk.starkkrumm.service.transformer.RoadTransformer.transform;
import static org.apache.logging.log4j.util.Strings.isBlank;

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
    @RequestMapping(value = "/starkkrumm/send")
    public void saveRoad(Road road, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new RuntimeException();
        }
        validatorService.validate(road);
        roadService.save(road);
    }

    @ResponseBody
    @PostMapping("/starkkrumm/sendV2")
    public void saveRoadV2(RoadRequest roadRequest) {
        log.info("saveRoadV2: " + roadRequest);
        validatorService.validate(roadRequest);
        roadService.save(transform(roadRequest));
    }

    @ResponseBody
    @RequestMapping(value = "/starkkrumm/print")
    public void uploadRoad(@RequestParam("date") String date, @RequestParam("carNumber") Integer carNumber) {
        validatorService.validate(date);
        roadService.uploadRoad(date,carNumber);
    }

    @ResponseBody
    @GetMapping("/starkkrumm/month")
    public List<RoadV2> getRoads(@RequestParam("date") String date, @RequestParam("carNumber") Integer carNumber) {
        log.info("getRoads: month: {}", date);
        if (isBlank(date)) {
            return Collections.emptyList();
        }
        return roadService.getRoadsByDateAndCarNumber(date, carNumber);
    }

}
