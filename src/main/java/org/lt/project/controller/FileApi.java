package org.lt.project.controller;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.lt.project.service.FileService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/file/")
@SecurityRequirement(name = "Authorization")
@RequiredArgsConstructor
public class FileApi {
    private final FileService fileService;

    @PostMapping("add-list")
    public ResponseEntity<Boolean> addIpListToBlokIPConf(@RequestBody List<String> ipList) {
        return ResponseEntity.ok(fileService.addIPAddresses(ipList));
    }

    @DeleteMapping("delete-list")
    public ResponseEntity<Boolean> deleteIpListFromBlokIPConf(@RequestBody List<String> ipList) {
        return ResponseEntity.ok(fileService.removeIPAddresses(ipList));
    }

    @GetMapping("get-all")
    public ResponseEntity<List<String>> listIpAddresses() {
        return ResponseEntity.ok(fileService.listIPAddresses());
    }
}
