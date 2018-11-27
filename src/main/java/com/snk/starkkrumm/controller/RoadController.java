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

import java.time.LocalDateTime;

@Slf4j
@Controller
@RequiredArgsConstructor
public class RoadController {

    private final RoadValidatorService validatorService;
    private final RoadService roadService;

    @RequestMapping(value = "/")
    public String home() {
        return "form";
    }

    @ResponseBody
    @RequestMapping(value = "/send")
    public ResponseEntity send(Road road, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body(null);
        }
        //validatorService.validate(road);
        //roadService.save(road);
        roadService.populateExcelFiles(road.getDeparture());
        return ResponseEntity.ok().body(null);
    }
}
