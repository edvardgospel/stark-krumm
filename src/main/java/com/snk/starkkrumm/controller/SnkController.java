package com.snk.starkkrumm.controller;

import com.snk.starkkrumm.model.Road;
import com.snk.starkkrumm.service.SnkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;

@Controller
public class SnkController {


    SnkService excelCreatorService;

    @RequestMapping(value = "/")
    public String home() {
        return "snk";
    }

    @ResponseBody
    @RequestMapping(value = "/send")
    public void send(@RequestBody Road road) throws IOException {
        excelCreatorService.instertData(road);
    }
}
