package com.snk.starkkrumm.controller;

import com.snk.starkkrumm.model.RoadRequest;
import com.snk.starkkrumm.service.SnkService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

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
    public void send(RoadRequest road) {
        log.info("send called {}", road);
        log.info("snkService {}", excelCreatorService);
        excelCreatorService.insert(road);
        //excelCreatorService.instertData(road);
    }
}
