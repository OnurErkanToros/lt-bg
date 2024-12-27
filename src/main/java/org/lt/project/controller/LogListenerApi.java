package org.lt.project.controller;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.lt.project.dto.LogListenerStatusResponseDto;
import org.lt.project.service.LogListenerService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/lt-api/1.0/log-listener/")
@SecurityRequirement(name = "Authorization")
public class LogListenerApi {
    private final LogListenerService listenerService;

    public LogListenerApi(LogListenerService listenerService) {
        this.listenerService = listenerService;
    }

    @PostMapping("start")
    public ResponseEntity<Boolean> start() {
        return ResponseEntity.ok(listenerService.startService());
    }

    @PostMapping("stop")
    public ResponseEntity<Boolean> stop() {
        return ResponseEntity.ok(listenerService.stopService());
    }

    @GetMapping("status")
    public ResponseEntity<LogListenerStatusResponseDto> status() {
        return ResponseEntity.ok(listenerService.getStatus());
    }
}
