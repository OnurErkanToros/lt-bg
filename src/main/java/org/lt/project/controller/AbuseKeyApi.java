package org.lt.project.controller;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.lt.project.dto.AbuseDbKeyRequestDto;
import org.lt.project.dto.AbuseDbKeyResponseDto;
import org.lt.project.service.AbuseDBKeyService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/lt-api/1.0/abuse-key/")
@SecurityRequirement(name = "Authorization")
public class AbuseKeyApi {
    private final AbuseDBKeyService abuseDBKeyService;

    public AbuseKeyApi(AbuseDBKeyService abuseDBKeyService) {
        this.abuseDBKeyService = abuseDBKeyService;
    }

    @GetMapping("get-all")
    public List<AbuseDbKeyResponseDto> getAll() {
        return abuseDBKeyService.getAllKey();
    }
    @PostMapping("add")
    public AbuseDbKeyResponseDto addAbuseKey(@RequestBody AbuseDbKeyRequestDto abuseDbKeyRequestDto) {
        return abuseDBKeyService.addKey(abuseDbKeyRequestDto);
    }
}
