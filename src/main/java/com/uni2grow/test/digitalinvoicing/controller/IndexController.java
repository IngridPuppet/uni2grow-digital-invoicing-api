package com.uni2grow.test.digitalinvoicing.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;

@RestController
public class IndexController {
    @Value("${spring.application.name}")
    private String appName;

    @GetMapping("/")
    public Object index() {
        return new Object() {
            private final String app = appName;
            private final Instant clk = Instant.now();
        };
    }
}
