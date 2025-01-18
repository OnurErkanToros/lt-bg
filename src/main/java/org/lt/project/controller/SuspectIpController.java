package org.lt.project.controller;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.lt.project.dto.SuspectIpRequestDto;
import org.lt.project.dto.SuspectIpResponseDto;
import org.lt.project.model.SuspectIP;
import org.lt.project.service.SuspectIpService;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/suspect-ip/")
@SecurityRequirement(name = "Authorization")
public class SuspectIpController {
    private final SuspectIpService suspectIpService;

    public SuspectIpController(SuspectIpService suspectIpService) {
        this.suspectIpService = suspectIpService;
    }

    @GetMapping("get-all")
    public ResponseEntity<Page<SuspectIpResponseDto>> getSuspectIpList(@RequestParam(defaultValue = "0") int page,
                                                                       @RequestParam(defaultValue = "30") int size,
                                                                       @RequestParam(required = false) String ip,
                                                                       @RequestParam(required = false) SuspectIP.IpStatus status,
                                                                       @RequestParam(required = false) String host) {
        return ResponseEntity.ok(suspectIpService.getAllFiltered(page, size,status,host,ip));
    }

    @PostMapping("add")
    public ResponseEntity<SuspectIP> addSuspectIp(@RequestBody SuspectIpRequestDto suspectIpRequestDto) {
        return ResponseEntity.ok(suspectIpService.save(suspectIpRequestDto));
    }
}
