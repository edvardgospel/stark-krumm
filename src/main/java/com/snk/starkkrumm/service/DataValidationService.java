package com.snk.starkkrumm.service;

import org.thymeleaf.util.StringUtils;

public class DataValidationService {
    public void validate(String data) {
        if (StringUtils.isEmpty(data)) {
            throw new NullPointerException();
        }
    }
}
