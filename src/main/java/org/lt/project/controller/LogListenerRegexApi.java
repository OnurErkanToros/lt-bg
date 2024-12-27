package org.lt.project.controller;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.lt.project.dto.LogListenerRegexRequestDto;
import org.lt.project.dto.LogListenerRegexResponseDto;
import org.lt.project.service.LogListenerRegexService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/lt-api/1.0/log-pattern/")
@SecurityRequirement(name = "Authorization")
@RequiredArgsConstructor
public class LogListenerRegexApi {
    private final LogListenerRegexService logListenerRegexService;


    @GetMapping("get-all")
    public ResponseEntity<List<LogListenerRegexResponseDto>> getAllPatternList() {
        return ResponseEntity.ok(logListenerRegexService.getAllPattern());
    }

    @PostMapping("add")
    public ResponseEntity<Boolean> addPattern(@RequestBody LogListenerRegexRequestDto
                                                      logListenerRegexRequestDto) {

        return ResponseEntity.ok(logListenerRegexService.addPattern(logListenerRegexRequestDto));
    }

    @DeleteMapping("delete/{id}")
    public ResponseEntity<Boolean> deletePattern(@PathVariable int id) {
        return ResponseEntity.ok(logListenerRegexService.deletePattern(id));
    }

    @PatchMapping("update/{id}")
    public ResponseEntity<LogListenerRegexResponseDto> updatePattern(@RequestBody LogListenerRegexRequestDto pattern,
                                                                     @PathVariable int id) {
        return ResponseEntity.ok(logListenerRegexService.updatePattern(pattern, id));
    }

}
