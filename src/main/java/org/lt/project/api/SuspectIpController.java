package org.lt.project.api;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.lt.project.dto.SuspectIpRequestDto;
import org.lt.project.dto.SuspectIpResponseDto;
import org.lt.project.dto.resultDto.DataResult;
import org.lt.project.dto.resultDto.Result;
import org.lt.project.service.SuspectIpService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/lt-api/1.0/suspect-ip/")
@SecurityRequirement(name = "Authorization")
public class SuspectIpController {
    private SuspectIpService suspectIpService;

    public SuspectIpController(SuspectIpService suspectIpService) {
        this.suspectIpService = suspectIpService;
    }

    public DataResult<List<SuspectIpResponseDto>> getSuspectIpList() {
        return suspectIpService.getAllSuspectIpList();
    }

    @GetMapping("add-suspect")
    public Result addSuspectIp(SuspectIpRequestDto suspectIpRequestDto) {
        return suspectIpService.saveSuspectIp(suspectIpRequestDto);
    }
}
