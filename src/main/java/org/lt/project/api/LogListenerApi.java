package org.lt.project.api;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.lt.project.dto.LogListenerStatusResponseDto;
import org.lt.project.dto.resultDto.DataResult;
import org.lt.project.dto.resultDto.Result;
import org.lt.project.service.LogListenerService;
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
    public Result start() {
        return listenerService.startService();
    }

    @PostMapping("stop")
    public Result stop() {
        return listenerService.stopService();
    }

    @GetMapping("status")
    public DataResult<LogListenerStatusResponseDto> status() {
        return listenerService.getStatus();
    }
}
