package com.carai.assistant.controller;
import com.carai.assistant.dto.VehicleRequest;
import com.carai.assistant.entity.VehicleAnalysis;
import com.carai.assistant.service.VehicleService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/vehicles")
@CrossOrigin(origins = "https://Y")
public class VehicleController {

    private final VehicleService service;

    public VehicleController(VehicleService service) {
        this.service = service;
    }

    @PostMapping("/analyze")
    public ResponseEntity<VehicleAnalysis> analyze(
            @Valid @RequestBody VehicleRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(service.analyze(request));
    }

    @GetMapping
    public List<VehicleAnalysis> getAll() {
        return service.getAll();
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, String>> handleException(Exception ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(Map.of("message",
                        "Request could not be processed. Check that all fields are valid."));
    }
}
```
