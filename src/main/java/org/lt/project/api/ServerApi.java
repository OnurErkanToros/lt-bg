package org.lt.project.api;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.lt.project.core.result.DataResult;
import org.lt.project.core.result.Result;
import org.lt.project.dto.ServerRequestDto;
import org.lt.project.entity.AllSuspectIpEntity;
import org.lt.project.service.AllSuspectIpService;
import org.lt.project.service.ServerService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/lt-api/1.0/server/")
@SecurityRequirement(name = "Authorization")
public class ServerApi {
    private final ServerService serverService;
    private final AllSuspectIpService allSuspectIpService;

    public ServerApi(ServerService serverService, AllSuspectIpService allSuspectIpService) {
        this.serverService = serverService;
        this.allSuspectIpService = allSuspectIpService;
    }

    @GetMapping("send-block-conf")
    public List<Result> sendBlockIpConf() {
        DataResult<List<AllSuspectIpEntity>> allSuspectIpEntityDataResult = allSuspectIpService.getAllSuspectIp(false);
        if (allSuspectIpEntityDataResult.isSuccess()) {
            return serverService.blockIpConfUpload(allSuspectIpEntityDataResult.getData().stream().map(AllSuspectIpEntity::getIpAddress).toList());
        } else {
            return List.of(allSuspectIpEntityDataResult);
        }

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
