package com.snk.starkkrumm.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class SnkController {

    @RequestMapping(value = "/")
    public String home() {
        return "snk";
    }
}
