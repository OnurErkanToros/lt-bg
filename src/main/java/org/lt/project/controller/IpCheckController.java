package org.lt.project.controller;

import lombok.RequiredArgsConstructor;
import org.lt.project.service.IpCheckService;
import org.lt.project.validator.IpAddressValidator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/ip-check/")
@RequiredArgsConstructor
@CrossOrigin
public class IpCheckController {
  private final IpCheckService ipCheckService;
  private final IpAddressValidator ipAddressValidator;

  @Value("${ipCheckService.secretKey}")
  private String SECRET_KEY;

  @GetMapping("check")
  public ResponseEntity<String> checkIp(
      @RequestParam String ipAddress, @RequestHeader("Secret-Key") String secretKey) {
    {
    }
    if (!ipAddressValidator.isValidIpAddress(ipAddress)) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid IP address");
    }
    if (SECRET_KEY.equals(secretKey)) {
      if (ipCheckService.isRiskyIp(ipAddress)) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body("IP address is blocked");
      } else {
        return ResponseEntity.ok("IP address is safe");
      }
    } else {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("unauthorized");
    }
  }
}
