package org.lt.project.service;

import org.apache.commons.io.input.Tailer;
import org.apache.commons.io.input.TailerListener;
import org.lt.project.core.result.ErrorResult;
import org.lt.project.core.result.Result;
import org.lt.project.core.result.SuccessResult;
import org.lt.project.dto.SuspectIpDto;
import org.lt.project.entity.SuspectIPEntity;
import org.springframework.stereotype.Service;

import java.io.File;
import java.time.Duration;
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


    public LogListenerService(SuspectIpService suspectIpService) {
        this.suspectIpService=suspectIpService;
        String patternString = ".*access forbidden by rule.*client: ([^,]+).*host: ([^,]+).*";
        pattern = Pattern.compile(patternString, Pattern.MULTILINE);
    }

    public Result startService(){
        try{
            if(isServiceRunning){
                return new ErrorResult("Servis zaten çalışıyor.");
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
            return new SuccessResult("Başarıyla başlatıldı");
        }catch (Exception e){
            return new ErrorResult("Başlatılırken bir hata oluştu. "+e.getMessage());
        }

    }
    public Result stopService(){
        try{
            if(isServiceRunning){
                tailer.close();
                return new SuccessResult("Başarıyla durduruldu.");
            }else {
                return new ErrorResult("Zaten başlatılmadı.");
            }
        }catch (Exception e){
            return new ErrorResult("Durdurulurken bir hata oluştu. "+e.getMessage());
        }

    }

    @Override
    public void handle(String line) {
            Matcher matcher = pattern.matcher(line);
            if (matcher.matches()) {
                String ip = matcher.group(1);
                String host = matcher.group(2);
                Integer accessForbiddenNumber = getAccessForbiddenNumber(line);
                suspectIpService.saveSuspectIp(new SuspectIpDto(ip, host,line, accessForbiddenNumber));
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
