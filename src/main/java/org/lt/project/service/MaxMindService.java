package org.lt.project.service;

import com.maxmind.geoip2.DatabaseReader;
import com.maxmind.geoip2.exception.GeoIp2Exception;
import com.maxmind.geoip2.model.CountryResponse;
import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class MaxMindService {
  private final SettingsService settingsService;
  private final GeoIPCountryService geoIPCountryService;

  public boolean checkCountry(String ipAddress) {
    var maxMindDatabaseCountryFilePathSetting =
        settingsService.getByKey("maxMindDatabaseCountryFilePath");
    File countryDatabase = new File(maxMindDatabaseCountryFilePathSetting.value());
    if (!countryDatabase.exists()) {
      throw new RuntimeException("MaxMind country database dosyası bulunamadı.");
    }

    try (DatabaseReader countryReader = new DatabaseReader.Builder(countryDatabase).build(); ) {
      InetAddress inetAddress = InetAddress.getByName(ipAddress);
      CountryResponse countryResponse = countryReader.country(inetAddress);

      return !geoIPCountryService.isAllowed(countryResponse.getCountry().getIsoCode());
    } catch (IOException | GeoIp2Exception e) {
      throw new RuntimeException(e.getMessage());
    }
  }
}
