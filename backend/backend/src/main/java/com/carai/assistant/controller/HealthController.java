package com.carai.assistant.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.Map;

// Lightweight endpoint for Render's health check and for opening the URL in a browser.
@RestController
public class HealthController {
    @GetMapping({"/", "/health"})
    public Map<String, String> health() {
        return Map.of("status", "UP", "service", "ai-used-car-assistant");
    }
}
