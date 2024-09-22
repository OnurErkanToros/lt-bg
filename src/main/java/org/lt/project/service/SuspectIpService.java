package org.lt.project.service;

import lombok.RequiredArgsConstructor;
import org.lt.project.core.convertor.SuspectIpConverter;
import org.lt.project.dto.BanRequestDto;
import org.lt.project.dto.SuspectIpRequestDto;
import org.lt.project.dto.SuspectIpResponseDto;
import org.lt.project.dto.resultDto.*;
import org.lt.project.model.BannedIp;
import org.lt.project.model.BannedIpType;
import org.lt.project.model.SuspectIP;
import org.lt.project.repository.SuspectIpRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SuspectIpService {
    private final SuspectIpRepository repository;
    private final BannedIpService bannedIpService;


    public DataResult<Page<SuspectIpResponseDto>> getAll(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<SuspectIpResponseDto> suspectIpResponseDtos =  repository.findAll(pageable).map(SuspectIpConverter::convert);
        if (!suspectIpResponseDtos.isEmpty()) {
            return new SuccessDataResult<>  (suspectIpResponseDtos);
        }else {
            return new ErrorDataResult<>("Şüpheli ip yok");
        }
    }

    public Result save(SuspectIpRequestDto suspectIpRequestDto) {
        SuspectIP suspectIP = SuspectIpConverter.convert(suspectIpRequestDto);
        SuspectIP savedSuspectIP = repository.save(suspectIP);
        if (savedSuspectIP != null) {
            return new SuccessResult();
        }else {
            return new ErrorResult();
        }
    }

    public Result setBanSuspectIpList(List<BanRequestDto> banRequestDtoList){
        List<String> banList = banRequestDtoList.stream().map(BanRequestDto::ip).toList();
        List<SuspectIP> suspectIPList = repository.findAllByBannedFalseAndIpAddressIn(banList);
        if(suspectIPList.isEmpty()){
            return new ErrorResult("Banlanacak bişey yok.");
        }
        List<BannedIp> bannedIpList = new ArrayList<>();
        for (SuspectIP suspectIP : suspectIPList) {
            suspectIP.setBanned(true);
            bannedIpList.add(
                    BannedIp.builder()
                            .ip(suspectIP.getIpAddress())
                            .transferred(false)
                            .ipType(BannedIpType.LISTENER)
                            .createdAt(new Date())
                            .createdBy(UserService.getAuthenticatedUser())
                            .build());
        }
        repository.saveAll(suspectIPList);
        bannedIpService.addAllIp(bannedIpList);
        return new SuccessResult();
    }
}
