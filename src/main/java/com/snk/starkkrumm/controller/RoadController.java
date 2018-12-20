package com.snk.starkkrumm.controller;

import com.snk.starkkrumm.model.Road;
import com.snk.starkkrumm.service.RoadService;
import com.snk.starkkrumm.service.RoadValidatorService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Slf4j
@Controller
@RequiredArgsConstructor
public class RoadController {

    private final RoadValidatorService validatorService;
    private final RoadService roadService;

    @ResponseBody
    @RequestMapping(value = "/send")
    public ResponseEntity saveRoad(Road road, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body(null);
        }
        validatorService.validate(road);
        roadService.save(road);
        return ResponseEntity.ok().body(null);
    }

    @ResponseBody
    @RequestMapping(value = "/print")
    public ResponseEntity printRoads(String month, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body(null);
        }
        validatorService.validate(month);
        //roadService.populateExcelFiles(month);
        //printerService.printExcelFiles(month)
        return ResponseEntity.ok().body(null);
    }

    @RequestMapping
    public List<String> getRoads(String month) {
        validatorService.validate(month);
        return roadService.getRoadsByMonth(month);
    }

}
