package org.lt.project.service;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.lt.project.exception.customExceptions.BadRequestException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FileService {
  private final SettingsService settingsService;

  private Path initConfFilePath() {
    var blockConfPathSetting = settingsService.getByKey("confFilePath");
    return Path.of(blockConfPathSetting.value());
  }

  public void addIPAddress(String ipAddress) {
    addIPAddresses(Collections.singletonList(ipAddress));
  }

  public void removeIPAddress(String ipAddress) {
    removeIPAddresses(Collections.singletonList(ipAddress));
  }

  public boolean isHaveAnyIp(String ip) {
    var blockConfPath = initConfFilePath();
    initConfFilePath();
    try {
      List<String> lines = Files.readAllLines(blockConfPath);
      Set<String> currentIPs = new HashSet<>(lines);

      try (BufferedWriter writer =
          new BufferedWriter(new FileWriter(blockConfPath.toFile(), true))) {
        String lineToAdd = "deny " + ip + ";";
        return currentIPs.contains(lineToAdd);
      }
    } catch (IOException e) {
      throw new BadRequestException(e.getMessage());
    }
  }

  public void removeDuplicateIPAddresses() {
    var blockConfPath = initConfFilePath();
    try {
      List<String> lines = Files.readAllLines(blockConfPath);
      Set<String> uniqueLines = new LinkedHashSet<>(lines);
      if (lines.size() == uniqueLines.size()) {
        System.out.println("Dosyada tekrar eden kayıt yok.");
        return;
      }
      Files.write(blockConfPath, uniqueLines);
      System.out.println("Tekrar eden kayıtlar silindi.");
    } catch (IOException e) {
      throw new BadRequestException(e.getMessage());
    }
  }

  public boolean addIPAddresses(List<String> ipAddresses) {
    var blockConfPath = initConfFilePath();
    var isAdded = false;
    List<String> addedIpList = new ArrayList<>();
    try {
      List<String> lines = Files.readAllLines(blockConfPath);
      Set<String> currentIPs = new HashSet<>(lines);

      try (BufferedWriter writer =
          new BufferedWriter(new FileWriter(blockConfPath.toFile(), true))) {
        for (String ip : ipAddresses) {
          String lineToAdd = "deny " + ip + ";";
          if (!currentIPs.contains(lineToAdd)) {
            writer.write(lineToAdd);
            writer.newLine();
            isAdded = true;
          } else {
            addedIpList.add(ip);
          }
        }
      }
    } catch (IOException e) {
      throw new BadRequestException(e.getMessage());
    }
    if (addedIpList.size() == 1) {
      throw new BadRequestException(
          "Bu ip zaten dosyada var, yani banlanmış. " + addedIpList.getFirst());
    } else if (addedIpList.size() > 1) {
      throw new BadRequestException("Bu ipler zaten dosyada var, yani banlanmış. " + addedIpList);
    }

    return isAdded;
  }

  public boolean removeIPAddresses(List<String> ipAddresses) {
    var blockConfPath = initConfFilePath();
    try {
      List<String> lines = Files.readAllLines(blockConfPath);
      Set<String> ipsToRemove =
          ipAddresses.stream().map(ip -> "deny " + ip + ";").collect(Collectors.toSet());

      List<String> updatedLines =
          lines.stream().filter(line -> !ipsToRemove.contains(line)).collect(Collectors.toList());

      if (lines.size() == updatedLines.size()) {
        throw new BadRequestException("Hiçbir IP bulunamadı, silme işlemi yapılmadı.");

      } else {
        Files.write(blockConfPath, updatedLines);
        System.out.println("IP adres(ler)i silindi: " + ipAddresses);
        return true;
      }
    } catch (IOException e) {
      throw new BadRequestException(e.getMessage());
    }
  }

  public List<String> listIPAddresses() {
    var blockConfPath = initConfFilePath();
    try {
      return Files.readAllLines(blockConfPath).stream()
          .filter(line -> line.startsWith("deny "))
          .map(line -> line.split(" ")[1].replace(";", "")) // IP kısmını al
          .collect(Collectors.toList());
    } catch (IOException e) {
      throw new BadRequestException(e.getMessage());
    }
  }
}
