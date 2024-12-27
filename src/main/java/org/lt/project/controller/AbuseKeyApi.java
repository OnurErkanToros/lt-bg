package org.lt.project.controller;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.lt.project.dto.AbuseDbKeyRequestDto;
import org.lt.project.dto.AbuseDbKeyResponseDto;
import org.lt.project.service.AbuseDBKeyService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/lt-api/1.0/abuse-key/")
@SecurityRequirement(name = "Authorization")
@RequiredArgsConstructor
public class AbuseKeyApi {
    private final AbuseDBKeyService abuseDBKeyService;

    @GetMapping("get-all")
    public ResponseEntity<List<AbuseDbKeyResponseDto>> getAll() {
        return ResponseEntity.ok(abuseDBKeyService.getAllKey());
    }

    @PostMapping("add")
    public ResponseEntity<AbuseDbKeyResponseDto> addAbuseKey(@RequestBody AbuseDbKeyRequestDto abuseDbKeyRequestDto) {
        return ResponseEntity.ok(abuseDBKeyService.addKey(abuseDbKeyRequestDto));
    }

    @DeleteMapping("delete/{id}")
    public ResponseEntity<Boolean> deleteAbuseKey(@PathVariable int id) {
        return ResponseEntity.ok(abuseDBKeyService.deleteAbuseKey(id));
    }
}
