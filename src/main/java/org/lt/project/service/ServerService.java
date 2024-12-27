package org.lt.project.service;


import org.lt.project.dto.BanRequestDto;
import org.lt.project.dto.SendBlockConfRequestDto;
import org.lt.project.dto.ServerRequestDto;
import org.lt.project.dto.ServerResponseDto;
import org.lt.project.dto.resultDto.DataResult;
import org.lt.project.dto.resultDto.ErrorDataResult;
import org.lt.project.dto.resultDto.Result;
import org.lt.project.dto.resultDto.SuccessDataResult;
import org.lt.project.exception.customExceptions.ResourceNotFoundException;
import org.lt.project.model.Server;
import org.lt.project.repository.ServerRepository;
import org.lt.project.util.FileUtil;
import org.lt.project.util.convertor.ServerConverter;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ServerService {
    private final FileUtil fileUtil;
    private final ServerRepository serverRepository;
    private final BanningIpService banningIpService;


    public ServerService(FileUtil fileUtil, ServerRepository serverRepository, BanningIpService banningIpService) {
        this.fileUtil = fileUtil;
        this.serverRepository = serverRepository;
        this.banningIpService = banningIpService;
    }

    public DataResult<List<Result>> sendBlockConf(SendBlockConfRequestDto requestDto) {
        File file = addNewIpToFile(requestDto.ipForBan());
        return fileUtil.sendFileByFtpForAllServers(file, requestDto.serverList());
    }

    private File addNewIpToFile(List<BanRequestDto> banningIpList) {
        List<BanRequestDto> oldIpList = new ArrayList<>(banningIpService.getBannedIpList()
                .stream().map(banningIp -> new BanRequestDto(banningIp.getIp())).toList());
        oldIpList.addAll(banningIpList);
        return fileUtil.createAndWriteFile(oldIpList);
    }

    public DataResult<ServerResponseDto> getServerById(int id) {
        Optional<Server> serverEntityOptional = serverRepository.findById(id);
        if (serverEntityOptional.isPresent()) {
            ServerResponseDto serverResponseDto = ServerConverter.convert(serverEntityOptional.get());
            return new SuccessDataResult<>(serverResponseDto);
        } else {
            return new ErrorDataResult<>("Böyle bir kayıt bulunamadı.");
        }
    }

    public ServerResponseDto addServer(ServerRequestDto serverRequestDto) {
        Server server = serverRepository.save(ServerConverter.convert(serverRequestDto));
        return ServerConverter.convert(server);
    }

    public Boolean deleteServer(int id) {
        Optional<Server> serverEntityOptional = serverRepository.findById(id);
        if (serverEntityOptional.isEmpty()) {
            throw new ResourceNotFoundException("Silinecek server bulunamadı.");
        }
        serverRepository.delete(serverEntityOptional.get());
        return true;
    }

    public List<ServerResponseDto> getServerList() {
        List<Server> serverList = serverRepository.findAll();
        return serverList.stream().map(ServerConverter::convert).toList();
    }

    public boolean deleteServerByIdList(List<Integer> serverIds) {
        try {
            serverRepository.deleteAllById(serverIds);
            long count = serverRepository.countByIdIn(serverIds);
            if (count == 0) {
                return true;
            } else if (count < serverIds.size()) {
                throw new ResourceNotFoundException("Kayıtların bazıları silinemedi.");
            }
            throw new ResourceNotFoundException("Kayıtlar silinemedi.");
        } catch (Exception e) {
            throw new ResourceNotFoundException("Kayıtlar silinemedi.");
        }
    }
}
