package com.snk.starkkrumm.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
public class DefaultController {
    @GetMapping("/")
    public String home() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String role = authentication.getAuthorities().toString();
        log.info("ROLE_home: {}", role);
        if (role.equals("[ROLE_ADMIN]")) {
            return "admin";
        } else if (role.equals("[ROLE_USER]")) {
            return "user";
        }
        return "login";
    }
}
