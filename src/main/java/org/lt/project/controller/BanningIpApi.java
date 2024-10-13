package org.lt.project.controller;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.lt.project.model.BanningIp;
import org.lt.project.service.BanningIpService;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequestMapping("/lt-api/1.0/banned-ip/")
@SecurityRequirement(name = "Authorization")
@RequiredArgsConstructor
public class BanningIpApi {
    private final BanningIpService banningIpService;

    @GetMapping("get-all")
    public ResponseEntity<Page<BanningIp>> getAll(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "30") int size) {
        return ResponseEntity.ok(banningIpService.getUntransferedIpList(page, size));
    }
    @GetMapping("untransferred-count")
    public ResponseEntity<Long> getUnTransferredCount() {
        return ResponseEntity.ok(banningIpService.getUnTransferredCount());
    }

}
