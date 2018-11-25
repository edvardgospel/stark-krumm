package com.snk.starkkrumm.controller;

import com.snk.starkkrumm.model.RoadRequest;
import com.snk.starkkrumm.service.SnkService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Slf4j
@Controller
public class SnkController {


    SnkService excelCreatorService = new SnkService();

    @RequestMapping(value = "/")
    public String home() {
        return "snk";
    }

    @ResponseBody
    @RequestMapping(value = "/send")
    public ResponseEntity send(RoadRequest road, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body(null);
        }
        //excelCreatorService.insert(road);
        List<RoadRequest> roadRequests = excelCreatorService.findRequestByDeparture(road.getDeparture());
        log.info("ROADREQUESTS: " + roadRequests);
        excelCreatorService.mapRoad(roadRequests);
        return ResponseEntity.ok().body(null);
    }
}
