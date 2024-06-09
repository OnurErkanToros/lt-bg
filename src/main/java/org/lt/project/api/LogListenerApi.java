package org.lt.project.api;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.lt.project.core.result.Result;
import org.lt.project.service.LogListenerService;
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
    public Result logListenerStart(){
        return listenerService.startService();
    }

    @PostMapping("stop")
    public Result logListenerStop(){
        return listenerService.stopService();
    }
}
