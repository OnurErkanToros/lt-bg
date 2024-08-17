package org.lt.project.api;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.lt.project.dto.AbuseDbKeyRequestDto;
import org.lt.project.dto.resultDto.Result;
import org.lt.project.service.AbuseDBKeyService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/lt-api/1.0/abuse-key/")
@SecurityRequirement(name = "Authorization")
public class AbuseKeyApi {
    private final AbuseDBKeyService abuseDBKeyService;

    public AbuseKeyApi(AbuseDBKeyService abuseDBKeyService) {
        this.abuseDBKeyService = abuseDBKeyService;
    }

    @GetMapping("get-all")
    public Result getAll(){
        return abuseDBKeyService.getAllKey();
    }
    @PostMapping("add")
    public Result addAbuseKey(@RequestBody AbuseDbKeyRequestDto abuseDbKeyRequestDto){
        return abuseDBKeyService.addKey(abuseDbKeyRequestDto);
    }
}
