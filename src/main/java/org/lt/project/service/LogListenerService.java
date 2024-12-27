package org.lt.project.service;

import lombok.RequiredArgsConstructor;
import org.apache.commons.io.input.Tailer;
import org.apache.commons.io.input.TailerListener;
import org.lt.project.dto.LogListenerRegexResponseDto;
import org.lt.project.dto.LogListenerStatusResponseDto;
import org.lt.project.dto.SuspectIpRequestDto;
import org.lt.project.exception.customExceptions.BadRequestException;
import org.lt.project.exception.customExceptions.InternalServerErrorException;
import org.lt.project.exception.customExceptions.ResourceNotFoundException;
import org.lt.project.model.SuspectIP;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class LogListenerService extends LogListenerAdapter {
    @Value("${nginx.error.log.filepath}")
    private String logFilePath;
    private List<LogListenerRegexResponseDto> regexList;
    private final SuspectIpService suspectIpService;
    private final LogListenerRegexService logListenerRegexService;
    private final SettingsService settingsService;
    private Tailer tailer;
    private boolean isServiceRunning = false;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private ExecutorService executor;

    private final Map<String, List<LocalDateTime>> ipLogMap = new HashMap<>();
    private int MAX_RETRY;
    private Duration TIME_WINDOW;

    public LogListenerStatusResponseDto getStatus() {
        var startTimeString = "";
        var endTimeString = "";
        if (startTime != null) {
            startTimeString = startTime.toString();
        }
        if (endTime != null) {
            endTimeString = endTime.toString();
        }
        return LogListenerStatusResponseDto.builder()
                .status(isServiceRunning ? LogListenerStatusResponseDto.LogListenerStatus.STARTED :
                        LogListenerStatusResponseDto.LogListenerStatus.STOPPED)
                .startTime(startTimeString)
                .endTime(endTimeString)
                .build();
    }

    public boolean startService() {
        prepareService();
        System.out.println("servis başlatıldı.");
        try {
            if (isServiceRunning) {
                throw new BadRequestException("Servis zaten çalışıyor.");
            }
            executor = Executors.newFixedThreadPool(4);
            File logFile = new File(logFilePath);
            TailerListener tailerListener = this;
            tailer = Tailer.builder()
                    .setFile(logFile)
                    .setTailerListener(tailerListener)
                    .setStartThread(false)
                    .setDelayDuration(Duration.ofSeconds(2))
                    .setTailFromEnd(false)
                    .get();

            executor.execute(tailer);
            isServiceRunning = true;
            startTime = LocalDateTime.now();
            endTime = null;
            return true;
        } catch (Exception e) {
            throw new InternalServerErrorException("Başlatılırken bir hata oluştu. " + e.getMessage());
        }

    }

    public boolean stopService() {
        try {
            if (isServiceRunning) {
                tailer.close();
                executor.shutdownNow();
                System.out.println(executor.isShutdown());
                isServiceRunning = false;
                endTime = LocalDateTime.now();
                return true;
            } else {
                throw new BadRequestException("Zaten duruyor.");
            }
        } catch (Exception e) {
            throw new InternalServerErrorException("Durdurulurken bir hata oluştu. " + e.getMessage());
        }

    }

    @Override
    public void handle(String line) {
        System.out.println(line);
        for (LogListenerRegexResponseDto regex : regexList) {
            Pattern pattern = Pattern.compile(regex.pattern(), Pattern.MULTILINE);
            Matcher matcher = pattern.matcher(line);
            if (matcher.matches()) {
                String ip = matcher.group(1);
                String host = matcher.group(2);
                LocalDateTime now = LocalDateTime.now();
                System.out.println(ip);
                ipLogMap.putIfAbsent(ip, new ArrayList<>());
                ipLogMap.get(ip).add(now);
                ipLogMap.get(ip).removeIf(timestamp -> Duration.between(timestamp, now).compareTo(TIME_WINDOW) > 0);
                var ipLogMapSize = ipLogMap.get(ip).size();
                if (ipLogMapSize >= MAX_RETRY && suspectIpService.isHaventThisIpAddress(ip)) {
                    System.out.println("Suspicious IP detected: " + ip);
                    suspectIpService.save(SuspectIpRequestDto.builder()
                            .ip(ip)
                            .host(host)
                            .retry(ipLogMapSize)
                            .pattern(pattern.pattern())
                            .createdAt(new Date())
                            .line(line)
                            .status(SuspectIP.IpStatus.NEW)
                            .statusAt(new Date())
                            .build());
                }
            }
        }
    }

    private void prepareService() {
        regexList = logListenerRegexService.getActivePatternList();
        var maxretrySetting = settingsService.getByKey("maxretry");
        var findTimeSetting = settingsService.getByKey("findtime");
        var findTimeTypeSetting = settingsService.getByKey("findtimetype");

        var findTime = Integer.parseInt(findTimeSetting.value());
        if (findTimeTypeSetting == null || findTimeTypeSetting.value() == null) {
            throw new ResourceNotFoundException("findtimetype ayarı bulunamadı.");
        }
        MAX_RETRY = Integer.parseInt(maxretrySetting.value());
        switch (findTimeTypeSetting.value()) {
            case "minute":
                TIME_WINDOW = Duration.ofMinutes(findTime);
                break;
            case "second":
                TIME_WINDOW = Duration.ofSeconds(findTime);
                break;
            default:
                throw new ResourceNotFoundException(findTimeSetting.value() + " tanımsız. Sadece minute/second olabilir.");
        }

        // String patternString = ".*access forbidden by rule.*client: ([^,]+).*host: ([^,]+).*";
    }
}
