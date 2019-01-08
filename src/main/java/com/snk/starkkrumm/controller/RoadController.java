package com.snk.starkkrumm.controller;

import com.snk.starkkrumm.model.Road;
import com.snk.starkkrumm.model.RoadRequest;
import com.snk.starkkrumm.service.RoadService;
import com.snk.starkkrumm.service.RoadValidatorService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

import static com.snk.starkkrumm.service.transformer.RoadTransformer.transform;

@Slf4j
@Controller
@RequiredArgsConstructor
public class RoadController {

    private final RoadValidatorService validatorService;
    private final RoadService roadService;

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
    public void saveRoadV2(@RequestBody RoadRequest roadRequest) {
        validatorService.validate(roadRequest);
        roadService.save(transform(roadRequest));
    }

    @ResponseBody
    @RequestMapping(value = "/starkkrumm/print")
    public void printRoads(String month, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new RuntimeException();
        }
        validatorService.validate(month);
        //roadService.populateExcelFiles(month);
        //printerService.printExcelFiles(month)
    }

    @ResponseBody
    @GetMapping("/starkkrumm/month")
    public List<String> getRoads(@RequestParam("month") String month) {
        log.info("getRoads: month: {}", month);
        if (Strings.isBlank(month)) {
            return Collections.emptyList();
        }
        return roadService.getRoadsByMonth(month);
    }

}
