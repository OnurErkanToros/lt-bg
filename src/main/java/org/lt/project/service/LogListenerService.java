package org.lt.project.service;

import java.io.File;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
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
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LogListenerService extends LogListenerAdapter {
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
  private String username = "admin";

    private final Map<String, List<LocalDateTime>> ipLogMap = new HashMap<>();
    private int MAX_RETRY;
    private Duration TIME_WINDOW;

    public LogListenerStatusResponseDto getStatus() {
    username = UserService.getAuthenticatedUser();
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
      if (!logFile.exists()) {
        throw new ResourceNotFoundException("Log dosyası bulunamadı.");
      }
            TailerListener tailerListener = this;
      tailer =
          Tailer.builder()
              .setFile(logFile)
              .setTailerListener(tailerListener)
              .setStartThread(false)
              .setDelayDuration(Duration.ofSeconds(1))
              .setTailFromEnd(true)
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
  public void fileRotated() {

      System.out.println("fileRotated tetiklendi.");
      new Thread(() -> {
          try {
              stopService();
              Thread.sleep(1000);
              startService();
          } catch (InterruptedException e) {
              Thread.currentThread().interrupt();
              System.err.println("Sleep sırasında kesinti oluştu: " + e.getMessage());
          } catch (Exception e) {
              System.err.println("fileRotated işlemi sırasında hata oluştu: " + e.getMessage());
          }
      }).start();
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
                if (ipLogMapSize >= MAX_RETRY && !suspectIpService.isHaveAnyIp(ip)) {
                    System.out.println("Suspicious IP detected: " + ip);
          suspectIpService.save(
              SuspectIpRequestDto.builder()
                  .ip(ip)
                  .host(host)
                  .retry(ipLogMapSize)
                  .pattern(pattern.pattern())
                  .createdAt(new Date())
                  .line(line)
                  .status(SuspectIP.IpStatus.NEW)
                  .statusAt(new Date())
                  .statusBy(username)
                  .build());
                }
            }
        }
    }

    private void prepareService() {
        regexList = logListenerRegexService.getActivePatternList();
        var maxretrySetting = settingsService.getByKey("maxRetry");
        var findTimeSetting = settingsService.getByKey("findTime");
        var findTimeTypeSetting = settingsService.getByKey("findTimeType");
    var logFilePathSetting = settingsService.getByKey("logFilePath");
    var findTime = Integer.parseInt(findTimeSetting.value());
        MAX_RETRY = Integer.parseInt(maxretrySetting.value());
    logFilePath = logFilePathSetting.value();

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
    }
}