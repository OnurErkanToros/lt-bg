package org.lt.project.service;

import org.apache.commons.io.input.Tailer;
import org.apache.commons.io.input.TailerListener;
import org.lt.project.dto.LogListenerStatusResponseDto;
import org.lt.project.dto.SuspectIpRequestDto;
import org.lt.project.exception.customExceptions.BadRequestException;
import org.lt.project.exception.customExceptions.InternalServerErrorException;
import org.springframework.stereotype.Service;

import java.io.File;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.concurrent.Executor;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.util.concurrent.Executors.newSingleThreadExecutor;

@Service
public class LogListenerService extends LogListenerAdapter {
    private SuspectIpService suspectIpService;

    private final Pattern pattern;
    private Tailer tailer;
    private boolean isServiceRunning = false;
    private LocalDateTime startTime;
    private LocalDateTime endTime;


    public LogListenerService(SuspectIpService suspectIpService) {
        this.suspectIpService=suspectIpService;
        String patternString = ".*access forbidden by rule.*client: ([^,]+).*host: ([^,]+).*";
        pattern = Pattern.compile(patternString, Pattern.MULTILINE);
    }

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
        try{
            if(isServiceRunning){
                throw new BadRequestException("Servis zaten çalışıyor.");
            }
            String logFilePath="src/main/resources/error.log";
            File logFile = new File(logFilePath);
            TailerListener tailerListener = this;
            tailer = Tailer.builder()
                    .setFile(logFile)
                    .setTailerListener(tailerListener)
                    .setStartThread(false)
                    .setDelayDuration(Duration.ofSeconds(2))
                    .get();
            Executor executor = newSingleThreadExecutor();
            executor.execute(tailer);
            isServiceRunning=true;
            startTime = LocalDateTime.now();
            return true;
        }catch (Exception e){
            throw new InternalServerErrorException("Başlatılırken bir hata oluştu. " + e.getMessage());
        }

    }

    public boolean stopService() {
        try{
            if(isServiceRunning){
                tailer.close();
                isServiceRunning=false;
                endTime = LocalDateTime.now();
                return true;
            }else {
                throw new BadRequestException("Zaten duruyor.");
            }
        }catch (Exception e){
            throw new InternalServerErrorException("Durdurulurken bir hata oluştu. " + e.getMessage());
        }

    }

    @Override
    public void handle(String line) {
            Matcher matcher = pattern.matcher(line);
            if (matcher.matches()) {
                String ip = matcher.group(1);
                String host = matcher.group(2);
                Integer accessForbiddenNumber = getAccessForbiddenNumber(line);
                suspectIpService.save(SuspectIpRequestDto.builder()
                        .ip(ip)
                        .host(host)
                        .accessForbiddenNumber(accessForbiddenNumber)
                        .pattern(pattern.pattern())
                        .createdAt(new Date())
                        .line(line)
                        .build());
            }
    }

    private Integer getAccessForbiddenNumber(String line) {
        String accessPatternString = ".*\\*(\\d+) access forbidden by rule.*";
        Pattern accessPattern = Pattern.compile(accessPatternString);
        Matcher accessMatcher = accessPattern.matcher(line);
        if (accessMatcher.matches()) {
            return Integer.valueOf(accessMatcher.group(1));
        }
        return -1;
    }

}
