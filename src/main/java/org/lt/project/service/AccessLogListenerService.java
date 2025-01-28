package org.lt.project.service;

import lombok.RequiredArgsConstructor;
import org.apache.commons.io.input.Tailer;
import org.apache.commons.io.input.TailerListener;
import org.lt.project.exception.customExceptions.BadRequestException;
import org.lt.project.exception.customExceptions.InternalServerErrorException;
import org.lt.project.exception.customExceptions.ResourceNotFoundException;
import org.lt.project.util.json.JsonParser;
import org.springframework.stereotype.Service;

import java.io.File;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
@RequiredArgsConstructor
public class AccessLogListenerService extends LogListenerAdapter {
  private final SettingsService settingsService;
  private Tailer tailer;
  private boolean isServiceRunning = false;
  private LocalDateTime startTime;
  private LocalDateTime endTime;
  private ExecutorService executorService;
  private String username = "admin";
  private final JsonParser jsonParser;
  public void startService() {
    if (isServiceRunning) {
      throw new BadRequestException("Servis zaten çalışıyor.");
    }
    try {

      var accessLogFilePath = settingsService.getByKey("accessLogFilePath");
      executorService = Executors.newFixedThreadPool(4);
      File accessLogFile = new File(accessLogFilePath.value());
      if (!accessLogFile.exists()) {
        throw new ResourceNotFoundException("Log dosyası bulunamadı.");
      }
      TailerListener tailerListener = this;
      tailer =
          Tailer.builder()
              .setFile(accessLogFile)
              .setTailerListener(tailerListener)
              .setStartThread(false)
              .setDelayDuration(Duration.ofSeconds(1))
              .setTailFromEnd(true)
              .get();
      executorService.execute(tailer);
      isServiceRunning = true;
      startTime = LocalDateTime.now();
      endTime = null;
    } catch (Exception e) {
      throw new InternalServerErrorException("Başlatılırken bir hata oluştu. " + e.getMessage());
    }
  }

  public void stopService() {
    if (!isServiceRunning) {
      throw new BadRequestException("Zaten duruyor.");
    }
    try {
      tailer.close();
      executorService.shutdownNow();
      isServiceRunning = false;
      endTime = LocalDateTime.now();
    } catch (Exception e) {
      throw new InternalServerErrorException("Durdurulurken bir hata oluştu. " + e.getMessage());
    }
  }

    @Override
    public void fileRotated() {
        new Thread(
                () -> {
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
                })
                .start();
    }

    @Override
    public void handle(String line) {
    System.out.println(line);
    String ipAddress = jsonParser.jsonToIp(line);
    //todo burada ipadresini maxmind içine yollayacak json işlemini burada yapmak gerek
    }
}
