package org.lt.project.controller;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.lt.project.dto.LogListenerPatternRequestDto;
import org.lt.project.dto.LogListenerPatternResponseDto;
import org.lt.project.service.LogListenerRegexService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/lt-api/1.0/log-pattern/")
@SecurityRequirement(name = "Authorization")
@RequiredArgsConstructor
public class LogListenerPatternApi {
    private final LogListenerRegexService logListenerRegexService;


    @GetMapping("get-all")
    public ResponseEntity<List<LogListenerPatternResponseDto>> getAllPatternList() {
        return ResponseEntity.ok(logListenerRegexService.getAllPattern());
    }
    @PostMapping("add")
    public ResponseEntity<Boolean> addPattern(@RequestBody LogListenerPatternRequestDto
                                                                             logListenerPatternRequestDto) {

        return ResponseEntity.ok(logListenerRegexService.addPattern(logListenerPatternRequestDto));
    }

    @DeleteMapping("delete/{id}")
    public ResponseEntity<Boolean> deletePattern(@PathVariable int id) {
        return ResponseEntity.ok(logListenerRegexService.deletePattern(id));
    }

    @PatchMapping("update/{id}")
    public ResponseEntity<LogListenerPatternResponseDto> updatePattern(@RequestBody LogListenerPatternRequestDto pattern,
                                                                       @PathVariable int id) {
        return ResponseEntity.ok(logListenerRegexService.updatePattern(pattern, id));
    }

}
