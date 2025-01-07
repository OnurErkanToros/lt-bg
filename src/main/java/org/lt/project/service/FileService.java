package org.lt.project.service;

import org.lt.project.exception.customExceptions.BadRequestException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class FileService {
    @Value("${blokip.conf.filepath}")
    private Path blockConfPath;

    public boolean addIPAddress(String ipAddress)  {
        return addIPAddresses(Collections.singletonList(ipAddress));
    }

    public boolean removeIPAddress(String ipAddress)  {
        return removeIPAddresses(Collections.singletonList(ipAddress));
    }

    public boolean isHaveAnyIp(String ip) {
        try {
            List<String> lines = Files.readAllLines(blockConfPath);
            Set<String> currentIPs = new HashSet<>(lines);

            try (BufferedWriter writer = new BufferedWriter(new FileWriter(blockConfPath.toFile(), true))) {
                String lineToAdd = "deny " + ip + ";";
                return currentIPs.contains(lineToAdd);
            }
        } catch (IOException e) {
            throw new BadRequestException(e.getMessage());
        }
    }

    public boolean removeDuplicateIPAddresses() {
        try {
            List<String> lines = Files.readAllLines(blockConfPath);
            Set<String> uniqueLines = new LinkedHashSet<>(lines);
            if (lines.size() == uniqueLines.size()) {
                System.out.println("Dosyada tekrar eden kayıt yok.");
                return false;
            }
            Files.write(blockConfPath, uniqueLines);
            System.out.println("Tekrar eden kayıtlar silindi.");
            return true;
        } catch (IOException e) {
            throw new BadRequestException(e.getMessage());
        }
    }


    public boolean addIPAddresses(List<String> ipAddresses) {
        var isAdded = false;
        List<String> addedIpList = new ArrayList<>();
        try {
            List<String> lines = Files.readAllLines(blockConfPath);
            Set<String> currentIPs = new HashSet<>(lines);

            try (BufferedWriter writer = new BufferedWriter(new FileWriter(blockConfPath.toFile(), true))) {
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
            throw new BadRequestException("Bu ip zaten dosyada var, yani banlanmış. " + addedIpList.getFirst());
        } else if (addedIpList.size() > 1) {
            throw new BadRequestException("Bu ipler zaten dosyada var, yani banlanmış. " + addedIpList);
        }

        return isAdded;
    }

    public boolean removeIPAddresses(List<String> ipAddresses) {
        try {
            List<String> lines = Files.readAllLines(blockConfPath);
            Set<String> ipsToRemove = ipAddresses.stream()
                    .map(ip -> "deny " + ip + ";")
                    .collect(Collectors.toSet());

            List<String> updatedLines = lines.stream()
                    .filter(line -> !ipsToRemove.contains(line))
                    .collect(Collectors.toList());

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