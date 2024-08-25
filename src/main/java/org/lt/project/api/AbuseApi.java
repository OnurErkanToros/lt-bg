package org.lt.project.api;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import org.lt.project.dto.AbuseBlackListResponseDto;
import org.lt.project.dto.resultDto.DataResult;
import org.lt.project.dto.resultDto.Result;
import org.lt.project.service.AbuseDBApiService;
import org.lt.project.service.AbuseDBService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/lt-api/1.0/abuse/")
@SecurityRequirement(name = "Authorization")
public class AbuseApi {
    private final AbuseDBApiService abuseApiService;
    private final AbuseDBService abuseDBService;
    public AbuseApi(AbuseDBApiService abuseApiService, AbuseDBService abuseDBService) {
        this.abuseApiService = abuseApiService;
        this.abuseDBService = abuseDBService;
    }

    @PostMapping("check-ip")
    public Result checkIp(@Valid @RequestParam @Positive int maxAgeInDays,
                          @RequestParam @Pattern(regexp = "\\b(?:\\d{1,3}\\.){3}\\d{1,3}\\b") String ipAddress) {
        return abuseApiService.checkIp(maxAgeInDays,ipAddress);
    }

    @PostMapping("blacklist/refresh")
    public Result refreshBlackList() {
        DataResult<List<AbuseBlackListResponseDto>> upToDateBlacklistDataResult = abuseApiService.getBlackList();
        if(upToDateBlacklistDataResult.isSuccess()){
            return abuseDBService.refreshBlackList(upToDateBlacklistDataResult.getData());
        }else {
            return upToDateBlacklistDataResult;
        }
    }

    @GetMapping("blacklist/all")
    public Result getAllBlackList(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "30") int size) {
        return this.abuseDBService.getAllBlackList(page, size);
    }
    @PostMapping("blacklist/ban")
    public Result setBanForBlacklist(@RequestBody List<String> ipList){
        return abuseDBService.setBanForBlacklist(ipList);
    }
    @PostMapping("check-ip/ban")
    public Result setBanForCheckIp(@RequestBody String ip){
        return abuseDBService.setBanForCheckIp(ip);
    }
}
