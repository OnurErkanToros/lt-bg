package org.lt.project.service;

import lombok.RequiredArgsConstructor;
import org.lt.project.dto.BanRequestDto;
import org.lt.project.dto.SuspectIpRequestDto;
import org.lt.project.dto.SuspectIpResponseDto;
import org.lt.project.exception.customExceptions.ResourceNotFoundException;
import org.lt.project.model.BanningIp;
import org.lt.project.model.IpStatus;
import org.lt.project.model.SuspectIP;
import org.lt.project.repository.SuspectIpRepository;
import org.lt.project.util.convertor.SuspectIpConverter;
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
    private final BanningIpService banningIpService;


    public Page<SuspectIpResponseDto> getAll(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return repository.findAll(pageable).map(SuspectIpConverter::convert);
    }

    public SuspectIP save(SuspectIpRequestDto suspectIpRequestDto) {
        SuspectIP suspectIP = SuspectIpConverter.convert(suspectIpRequestDto);
        suspectIP.setStatus(IpStatus.NEW);
        suspectIP.setStatusAt(new Date());
        suspectIP.setStatusBy(UserService.getAuthenticatedUser());
        return repository.save(suspectIP);
    }

    public Boolean setBanSuspectIpList(List<BanRequestDto> banRequestDtoList) {
        List<String> banList = banRequestDtoList.stream().map(BanRequestDto::ip).toList();
        List<SuspectIP> suspectIPList = repository.findAllByStatusAndIpAddressIn(IpStatus.NEW, banList);

        List<BanningIp> banningIpList = new ArrayList<>();
        String user = UserService.getAuthenticatedUser();
        for (SuspectIP suspectIP : suspectIPList) {
            suspectIP.setStatus(IpStatus.READY_TRANSFER);
            suspectIP.setStatusAt(new Date());
            suspectIP.setStatusBy(user);
            banningIpList.add(
                    BanningIp.builder()
                            .ip(suspectIP.getIpAddress())
                            .status(BanningIp.BanningIpStatus.NOT_TRANSFERRED)
                            .ipType(BanningIp.BanningIpType.LISTENER)
                            .createdAt(new Date())
                            .createdBy(user)
                            .build());
        }
        repository.saveAll(suspectIPList);
        return true;
    }
}
