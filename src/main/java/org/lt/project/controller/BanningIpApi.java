package org.lt.project.controller;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.lt.project.dto.BanIpRequest;
import org.lt.project.dto.resultDto.Result;
import org.lt.project.model.BanningIp;
import org.lt.project.service.BanningIpService;
import org.lt.project.service.IPDatabaseSyncService;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequestMapping("/banned-ip/")
@SecurityRequirement(name = "Authorization")
@RequiredArgsConstructor
public class BanningIpApi {
  private final BanningIpService banningIpService;
  private final IPDatabaseSyncService databaseSyncService;

  @PostMapping("sync")
  public ResponseEntity<Boolean> syncDatabaseWithFile() {
    return ResponseEntity.ok(databaseSyncService.syncDatabaseWithFile());
  }

  @GetMapping("get-all")
  public ResponseEntity<Page<BanningIp>> getAll(
      @RequestParam(required = false) String ip,
      @RequestParam(required = false) BanningIp.BanningIpType ipType,
      @RequestParam(defaultValue = "0") int page,
      @RequestParam(defaultValue = "30") int size) {
    return ResponseEntity.ok(banningIpService.getBannedAllIpPageable(ip, ipType, page, size));
  }

  @PostMapping("ban")
  public ResponseEntity<List<Result>> banThisIpList(
      @RequestBody List<BanIpRequest> banIpRequestList) {
    return ResponseEntity.ok(banningIpService.banThisIpList(banIpRequestList));
  }

  @PostMapping("unban")
  public ResponseEntity<List<Result>> unbanThisIpList(
      @RequestBody List<BanIpRequest> banIpRequestList) {
    return ResponseEntity.ok(banningIpService.unbanThisIpList(banIpRequestList));
  }
}
