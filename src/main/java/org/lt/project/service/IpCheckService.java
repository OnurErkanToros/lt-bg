package org.lt.project.service;

import lombok.RequiredArgsConstructor;
import org.lt.project.exception.customExceptions.BadRequestException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class IpCheckService {
  private final MaxMindService maxMindService;
  private final BanningIpService banningIpService;

  public boolean isRiskyIp(String ipAddress) {
    return maxMindService.checkCountry(ipAddress) || banningIpService.isHaveAnyIP(ipAddress);
  }
}
