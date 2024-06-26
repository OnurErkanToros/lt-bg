package org.lt.project.api;

import org.lt.project.core.result.DataResult;
import org.lt.project.core.result.Result;
import org.lt.project.dto.SuspectIpDto;
import org.lt.project.service.SuspectIpService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/lt-api/1.0/suspect-ip/")
public class SuspectIpController {
    private SuspectIpService suspectIpService;

    public SuspectIpController(SuspectIpService suspectIpService) {
        this.suspectIpService = suspectIpService;
    }

    public DataResult<List<SuspectIpDto>> getSuspectIpList(){
        return suspectIpService.getAllSuspectIpList();
    }
    public Result addSuspectIp(SuspectIpDto suspectIpDto){
        return suspectIpService.saveSuspectIp(suspectIpDto);
    }
}
