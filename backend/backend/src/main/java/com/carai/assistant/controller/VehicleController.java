package com.carai.assistant.controller;

import com.carai.assistant.dto.VehicleRequest;
import com.carai.assistant.entity.VehicleAnalysis;
import com.carai.assistant.service.VehicleService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;
@CrossOrigin(origins = "https://ai-used-car-assistant-e846.vercel.app")
@RestController
@RequestMapping("/api/vehicles")
public class VehicleController {
public class VehicleController {
    private static final Logger log = LoggerFactory.getLogger(VehicleController.class);
    private final VehicleService service;
    public VehicleController(VehicleService service) { this.service = service; }

    @PostMapping("/analyze")
    public ResponseEntity<VehicleAnalysis> analyze(@Valid @RequestBody VehicleRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.analyze(request));
    }

    @GetMapping
    public List<VehicleAnalysis> getAll() { return service.getAll(); }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, String>> handleException(Exception ex) {
        // Log the real cause so database/config problems show up in Render's logs
        log.error("Request failed", ex);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("message", "Request could not be processed. Check that all fields are valid."));
    }
}
