package org.lt.project.api;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import org.lt.project.core.result.ErrorResult;
import org.lt.project.core.result.Result;
import org.lt.project.dto.AbuseCheckRequestDto;
import org.lt.project.service.AbuseDBApiService;
import org.lt.project.service.AbuseDBService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequestMapping("/lt-api/1.0/abuse/")
@SecurityRequirement(name = "Authorization")
public class AbuseApi {
    private final AbuseDBApiService abuseApiService;
    private final AbuseDBService abuseDBService;
    public AbuseApi(AbuseDBApiService abuseApiService, AbuseDBService abuseDBService) {
        this.abuseApiService = abuseApiService;
        this.abuseDBService = abuseDBService;
    }

    @GetMapping("check-ip")
    public Result checkIp(@Valid @RequestParam @Positive int maxAgeInDays,
                          @RequestParam @Pattern(regexp = "\\b(?:\\d{1,3}\\.){3}\\d{1,3}\\b") String ipAddress) {
        return abuseApiService.checkIp(maxAgeInDays,ipAddress);
    }

    @GetMapping("blacklist/refresh")
    public Result refreshBlackList() {
            return abuseDBService.refreshBlackList(abuseApiService.getBlackList().getData());

    }

    @GetMapping("blacklist/all")
    public Result getAllBlackList(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "30") int size) {
        return this.abuseDBService.getAllBlackList(page, size);
    }
}
