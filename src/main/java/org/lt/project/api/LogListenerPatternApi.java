package org.lt.project.api;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.lt.project.dto.LogListenerPatternRequestDto;
import org.lt.project.dto.LogListenerPatternResponseDto;
import org.lt.project.dto.resultDto.DataResult;
import org.lt.project.dto.resultDto.Result;
import org.lt.project.service.LogListenerRegexService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/lt-api/1.0/log-pattern/")
@SecurityRequirement(name = "Authorization")
public class LogListenerPatternApi {
    private final LogListenerRegexService logListenerRegexService;

    public LogListenerPatternApi(LogListenerRegexService logListenerRegexService) {
        this.logListenerRegexService = logListenerRegexService;
    }

    @GetMapping("get-all")
    public DataResult<List<LogListenerPatternResponseDto>> getAllPatternList(){
        return logListenerRegexService.getAllPattern();
    }
    @PostMapping("add")
    public Result addPattern(@RequestBody LogListenerPatternRequestDto logListenerPatternRequestDto){
        return logListenerRegexService.addPattern(logListenerPatternRequestDto);
    }

    @DeleteMapping("delete/{id}")
    public Result deletePattern(@PathVariable int id){
        return logListenerRegexService.deletePattern(id);
    }

    @PatchMapping("update/{id}")
    public DataResult<LogListenerPatternResponseDto> updatePattern(@RequestBody LogListenerPatternRequestDto pattern,@PathVariable int id){
        return logListenerRegexService.updatePattern(pattern,id);
    }

}
