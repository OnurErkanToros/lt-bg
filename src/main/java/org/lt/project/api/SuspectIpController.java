package org.lt.project.api;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.lt.project.dto.SuspectIpRequestDto;
import org.lt.project.dto.SuspectIpResponseDto;
import org.lt.project.dto.resultDto.DataResult;
import org.lt.project.dto.resultDto.Result;
import org.lt.project.service.SuspectIpService;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/lt-api/1.0/suspect-ip/")
@SecurityRequirement(name = "Authorization")
public class SuspectIpController {
    private final SuspectIpService suspectIpService;

    public SuspectIpController(SuspectIpService suspectIpService) {
        this.suspectIpService = suspectIpService;
    }

    @GetMapping("get-all")
    public DataResult<Page<SuspectIpResponseDto>> getSuspectIpList(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "30") int size) {
        return suspectIpService.getAll(page,size);
    }

    @PostMapping("add")
    public Result addSuspectIp(@RequestBody SuspectIpRequestDto suspectIpRequestDto) {
        return suspectIpService.save(suspectIpRequestDto);
    }

    @PostMapping("ban")
    public Result setBanSuspectIpList(@RequestBody List<String> ipList){
        return suspectIpService.setBanSuspectIpList(ipList);
    }
}
