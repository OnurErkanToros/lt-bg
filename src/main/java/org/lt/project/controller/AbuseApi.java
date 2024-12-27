package org.lt.project.controller;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import org.lt.project.dto.AbuseCheckResponseDto;
import org.lt.project.dto.BanRequestDto;
import org.lt.project.model.AbuseDBBlackList;
import org.lt.project.service.AbuseDBApiService;
import org.lt.project.service.AbuseDBService;
import org.springframework.data.domain.Page;
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

    @PostMapping("check-ip")
    public ResponseEntity<AbuseCheckResponseDto> checkIp(@Valid @RequestParam @Positive int maxAgeInDays,
                                                         @RequestParam @Pattern(regexp = "\\b(?:\\d{1,3}\\.){3}\\d{1,3}\\b") String ipAddress) {
        return ResponseEntity.ok(abuseApiService.checkIp(maxAgeInDays, ipAddress));
    }

    @PostMapping("blacklist/refresh")
    public ResponseEntity<Boolean> refreshBlackList() {
        final var upToDateBlacklist = abuseApiService.getBlackList();
        return ResponseEntity.ok(abuseDBService.refreshBlackList(upToDateBlacklist));
    }

    @GetMapping("blacklist/all")
    public ResponseEntity<Page<AbuseDBBlackList>> getAllBlackList(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "30") int size) {
        return ResponseEntity.ok(abuseDBService.getAllBlackList(page, size));
    }

    @GetMapping("blacklist/count-new")
    public ResponseEntity<Long> getCountBlacklistNewStatus() {
        return ResponseEntity.ok(abuseDBService.getCountBlacklistNewStatus());
    }

    @PostMapping("blacklist/ban")
    public ResponseEntity<Boolean> setBanForBlacklist() {
        return ResponseEntity.ok(abuseDBService.setBanForBlacklist());
    }

    @PostMapping("check-ip/ban")
    public ResponseEntity<Boolean> setBanForCheckIp(@RequestBody BanRequestDto banRequestDto) {
        return ResponseEntity.ok(abuseDBService.setBanForCheckIp(banRequestDto));
    }
}
