package org.lt.project.controller;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.lt.project.dto.SendBlockConfRequestDto;
import org.lt.project.dto.ServerRequestDto;
import org.lt.project.dto.ServerResponseDto;
import org.lt.project.dto.resultDto.DataResult;
import org.lt.project.dto.resultDto.Result;
import org.lt.project.service.ServerService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/lt-api/1.0/server/")
@SecurityRequirement(name = "Authorization")
@RequiredArgsConstructor
public class ServerApi {
    private final ServerService serverService;

    @GetMapping("send-block-conf")
    public ResponseEntity<DataResult<List<Result>>> sendBlockIpConf(@RequestBody SendBlockConfRequestDto sendBlockConfRequestDto) {
        return ResponseEntity.ok(serverService.sendBlockConf(sendBlockConfRequestDto));
    }

    @PostMapping("add")
    public ResponseEntity<ServerResponseDto> addServer(@RequestBody ServerRequestDto requestDto) {
        return ResponseEntity.ok(serverService.addServer(requestDto));
    }

    @DeleteMapping("delete/{id}")
    public ResponseEntity<Boolean> deleteServer(@PathVariable int id) {
        return ResponseEntity.ok(serverService.deleteServer(id));
    }
    public ResponseEntity<Boolean> deleteServerByIdList(@RequestBody List<Integer> idList){
        return ResponseEntity.ok(serverService.deleteServerByIdList(idList));
    }
    @GetMapping("get-all")
    public ResponseEntity<List<ServerResponseDto>> getAllServer() {
        return ResponseEntity.ok(serverService.getServerList());
    }
}
