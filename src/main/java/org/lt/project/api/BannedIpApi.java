package org.lt.project.api;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.lt.project.dto.resultDto.Result;
import org.lt.project.service.BannedIpService;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequestMapping("/lt-api/1.0/banned-ip/")
@SecurityRequirement(name = "Authorization")
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
