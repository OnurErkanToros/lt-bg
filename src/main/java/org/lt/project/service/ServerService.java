package org.lt.project.service;


import org.lt.project.core.FileUtil;
import org.lt.project.core.convertor.ServerConverter;
import org.lt.project.dto.ServerRequestDto;
import org.lt.project.dto.ServerResponseDto;
import org.lt.project.dto.resultDto.*;
import org.lt.project.model.Server;
import org.lt.project.repository.ServerRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ServerService {
    private final FileUtil fileUtil;
    private final ServerRepository serverRepository;


    public ServerService(FileUtil fileUtil, ServerRepository serverRepository) {
        this.fileUtil = fileUtil;
        this.serverRepository = serverRepository;
    }

    public List<Result> blockIpConfUpload(List<String> ipList) {
        DataResult<List<ServerResponseDto>> dataResult = getServerList();
        List<Result> resultList = new ArrayList<>();
        if (dataResult.isSuccess()) {
            for (ServerResponseDto serverResponseDto : dataResult.getData()) {
                fileUtil.saveFile(ipList, "blockip.conf");
                Result result = fileUtil.sendFileViaFtp(
                        serverResponseDto.fileName(),
                        serverResponseDto.remoteFilePath(),
                        serverResponseDto.url(),
                        serverResponseDto.port(),
                        serverResponseDto.username(),
                        serverResponseDto.password());
                resultList.add(result);
            }
        }else {
            resultList.add(dataResult);
        }
        return resultList;
    }
    public DataResult<ServerResponseDto> getServerById(int id){
        Optional<Server> serverEntityOptional = serverRepository.findById(id);
        if (serverEntityOptional.isPresent()){
            ServerResponseDto serverResponseDto = ServerConverter.convert(serverEntityOptional.get());
            return new SuccessDataResult<>(serverResponseDto);
        }else {
            return new ErrorDataResult<>("Böyle bir kayıt bulunamadı.");
        }
    }

    public DataResult<ServerResponseDto> addServer(ServerRequestDto serverRequestDto) {
        Server server = serverRepository.save(ServerConverter.convert(serverRequestDto));
        return new SuccessDataResult<>("Sunucu başarıyla eklendi.", ServerConverter.convert(server));
    }
    public Result deleteServer(int id){
        Optional<Server> serverEntityOptional = serverRepository.findById(id);
        if (serverEntityOptional.isPresent()){
            serverRepository.delete(serverEntityOptional.get());
            return new SuccessResult(String.format("%s başarıyla silindi.",serverEntityOptional.get().getName()));
        }else {
            return new ErrorResult("Böyle bir kayıt yok.");
        }
    }

    public DataResult<List<ServerResponseDto>> getServerList() {
        List<Server> serverList = serverRepository.findAll();
        List<ServerResponseDto> serverResponseDtoList = serverList.stream().map(ServerConverter::convert).toList();
        if (serverList.isEmpty()) {
            return new ErrorDataResult<>("server listesi boş");
        } else {
            return new SuccessDataResult<>(serverResponseDtoList);
        }
    }
    public Result deleteServerByIdList(List<Integer> serverIds){
        try {
            serverRepository.deleteAllById(serverIds);
            long count = serverRepository.countByIdIn(serverIds);
            if(count==0){
                return new SuccessResult("Kayıtlar başarıyla silindi.");
            } else if (count<serverIds.size()) {
                return new ErrorResult("Kayıtların bazıları silinemedi.");
            }
            return new ErrorResult("Kayıtlar silinemedi.");
        } catch (Exception e) {
            return new ErrorResult("Kayıtlar silinemedi.");
        }
    }
}
