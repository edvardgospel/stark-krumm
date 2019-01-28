package com.snk.starkkrumm.controller;

import com.snk.starkkrumm.model.Road;
import com.snk.starkkrumm.model.RoadRequest;
import com.snk.starkkrumm.service.RoadService;
import com.snk.starkkrumm.service.RoadValidatorService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

import static com.snk.starkkrumm.service.transformer.RoadTransformer.transform;
import static org.apache.logging.log4j.util.Strings.isBlank;

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
    @PostMapping("/starkkrumm/sendV2")
    public void saveRoad(RoadRequest roadRequest) {
        validatorService.validate(roadRequest);
        roadService.save(transform(roadRequest));
    }

    @ResponseBody
    @GetMapping("/starkkrumm/month")
    public List<Road> getRoads(@RequestParam("date") String date, @RequestParam("carNumber") Integer carNumber) {
        validatorService.validate(date, carNumber);
        if (isBlank(date)) {
            return Collections.emptyList();
        }
        return roadService.getRoadsByDateAndCarNumber(date, carNumber);
    }

    @ResponseBody
    @RequestMapping(value = "/starkkrumm/print")
    public void uploadRoad(@RequestParam("date") String date, @RequestParam("carNumber") Integer carNumber) {
        validatorService.validate(date, carNumber);
        roadService.uploadRoad(date, carNumber);
    }

}
