package org.lt.project.api;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.lt.project.dto.ServerRequestDto;
import org.lt.project.dto.resultDto.Result;
import org.lt.project.service.ServerService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/lt-api/1.0/server/")
@SecurityRequirement(name = "Authorization")
@RequiredArgsConstructor
public class ServerApi {
    private final ServerService serverService;

    @GetMapping("send-block-conf")
    public List<Result> sendBlockIpConf() {
            return null;
    }

    @PostMapping("add")
    public Result addServer(@RequestBody ServerRequestDto requestDto) {
        return serverService.addServer(requestDto);
    }

    @DeleteMapping("delete/{id}")
    public Result deleteServer(@PathVariable int id){
        return serverService.deleteServer(id);
    }
    @GetMapping("get-all")
    public Result getAllServer() {
        return serverService.getServerList();
    }

    @DeleteMapping("delete")
    public Result deleteServerByIdList(@RequestBody List<Integer> idList){
        return serverService.deleteServerByIdList(idList);
    }

}
