package org.lt.project.controller;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.lt.project.service.IpCheckService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/ip-check/")
@RequiredArgsConstructor
@CrossOrigin
@SecurityRequirement(name = "Authorization")
public class IpCheckController {
  private final IpCheckService ipCheckService;

  @GetMapping("check")
  public ResponseEntity<Void> checkIp(@RequestParam String ipAddress) {
    if(ipCheckService.isRiskyIp(ipAddress)){
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }else {
      return ResponseEntity.ok().build();
    }
  }
}
