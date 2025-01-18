package org.lt.project.controller;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import org.lt.project.model.AbuseDBBlackList;
import org.lt.project.service.AbuseDBApiService;
import org.lt.project.service.AbuseDBService;
import org.lt.project.validator.IpAddressValidator;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@CrossOrigin
@RestController
@RequestMapping("/abuse/")
@SecurityRequirement(name = "Authorization")
public class AbuseApi {
    private final AbuseDBApiService abuseApiService;
    private final AbuseDBService abuseDBService;
    private final IpAddressValidator ipAddressValidator;

    public AbuseApi(AbuseDBApiService abuseApiService, 
                    AbuseDBService abuseDBService,
                    IpAddressValidator ipAddressValidator) {
        this.abuseApiService = abuseApiService;
        this.abuseDBService = abuseDBService;
        this.ipAddressValidator = ipAddressValidator;
    }

    @PostMapping("check-ip")
    public ResponseEntity<?> checkIp(@Valid @RequestParam @Positive int maxAgeInDays,
                                    @RequestParam String ipAddress) {
        if (!ipAddressValidator.isValidIpAddress(ipAddress)) {
            return ResponseEntity
                .badRequest()
                .body("Geçersiz IP adresi formatı");
        }
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
}
