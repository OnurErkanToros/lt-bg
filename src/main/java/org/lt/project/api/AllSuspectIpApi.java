package org.lt.project.api;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.lt.project.core.result.Result;
import org.lt.project.dto.AllSuspectIpRequestDto;
import org.lt.project.entity.AllSuspectIpEntity;
import org.lt.project.service.AllSuspectIpService;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@RestController
@RequestMapping("/lt-api/1.0/all-suspect-ip/")
@SecurityRequirement(name = "Authorization")
public class AllSuspectIpApi {
    private final AllSuspectIpService allSuspectIpService;

    public AllSuspectIpApi(AllSuspectIpService allSuspectIpService) {
        this.allSuspectIpService = allSuspectIpService;
    }

    @GetMapping("get-all")
    public Result getAllSuspectIp(@RequestParam(defaultValue = "0") int page,@RequestParam(defaultValue = "30") int size) {
        return allSuspectIpService.getAllSuspectIp(page,size);
    }

    @GetMapping("get-banned-ip-list")
    public Result getAllBannedIp(@RequestParam(defaultValue = "0") int page,@RequestParam(defaultValue = "30") int size) {
        return allSuspectIpService.getAllSuspectIp(true,page,size);
    }

    @GetMapping("get-unbanned-ip-list")
    public Result getAllUnbannedIp(@RequestParam(defaultValue = "0") int page,@RequestParam(defaultValue = "30") int size) {
        return allSuspectIpService.getAllSuspectIp(false,page,size);
    }

    @PostMapping("add-new-suspect-ip")
    public Result addNewIp(@RequestBody AllSuspectIpRequestDto requestDto) {
        return allSuspectIpService.addSuspectIp(new AllSuspectIpEntity(
                requestDto.getIpAddress(),
                "manuel",
                new Date(),
                false));
    }
}
