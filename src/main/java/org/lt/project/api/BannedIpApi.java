package org.lt.project.api;

import lombok.RequiredArgsConstructor;
import org.lt.project.dto.resultDto.Result;
import org.lt.project.service.BannedIpService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/lt-api/1.0/banned-ip/")
@RequiredArgsConstructor
public class BannedIpApi {
    private final BannedIpService bannedIpService;

    @GetMapping("get-all")
    public Result getAll(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "30") int size){
        return bannedIpService.getBannedIpList(page, size);
    }
    @GetMapping("untransferred-count")
    public Result getUnTransferredCount(){
        return bannedIpService.getUnTransferredCount();
    }

}
