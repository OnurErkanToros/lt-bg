package org.lt.project.service;

import lombok.RequiredArgsConstructor;
import org.lt.project.dto.BanRequestDto;
import org.lt.project.dto.SuspectIpRequestDto;
import org.lt.project.dto.SuspectIpResponseDto;
import org.lt.project.model.BanningIp;
import org.lt.project.model.SuspectIP;
import org.lt.project.repository.SuspectIpRepository;
import org.lt.project.specification.SuspectIpSpecification;
import org.lt.project.util.convertor.SuspectIpConverter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SuspectIpService {
    private final SuspectIpRepository repository;
    private final BanningIpService banningIpService;
    private final FileService fileService;


    public Page<SuspectIpResponseDto> getAll(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        return repository.findAll(pageable).map(SuspectIpConverter::convert);
    }

    public Page<SuspectIpResponseDto> getAllFiltered(int page, int size, SuspectIP.IpStatus ipStatus, String host, String ip) {
        Pageable pageable = PageRequest.of(page,size, Sort.by("createdAt").descending());
        Specification<SuspectIP> spec = Specification.where(SuspectIpSpecification.hasIp(ip))
                .and(SuspectIpSpecification.hasStatus(ipStatus))
                .and(SuspectIpSpecification.hasHost(host));
        return repository.findAll(spec,pageable).map(SuspectIpConverter::convert);
    }

    public SuspectIP save(SuspectIpRequestDto suspectIpRequestDto) {
        SuspectIP suspectIP = SuspectIpConverter.convert(suspectIpRequestDto);
        return repository.save(suspectIP);
    }

    public Boolean setBanSuspectIpList(List<BanRequestDto> banRequestDtoList) {
        List<String> banList = banRequestDtoList.stream().map(BanRequestDto::ip).toList();
        List<SuspectIP> suspectIPList = repository.findAllByStatusAndIpAddressIn(SuspectIP.IpStatus.NEW, banList);

        List<BanningIp> banningIpList = new ArrayList<>();
        String user = UserService.getAuthenticatedUser();
        for (SuspectIP suspectIP : suspectIPList) {
            suspectIP.setStatus(SuspectIP.IpStatus.BANNED);
            suspectIP.setStatusAt(new Date());
            suspectIP.setStatusBy(user);

            banningIpList.add(
                    BanningIp.builder()
                            .ip(suspectIP.getIpAddress())
                            .ipType(BanningIp.BanningIpType.LISTENER)
                            .createdAt(new Date())
                            .createdBy(user)
                            .build());
        }
        fileService.addIPAddresses(banningIpList.stream().map(BanningIp::getIp).toList());
        banningIpService.addAllIp(banningIpList);
        repository.saveAll(suspectIPList);
        return true;
    }
    public boolean isHaventThisIpAddress(String ipAddress){
        Optional<SuspectIP> suspectIP = repository.findFirstByIpAddress(ipAddress);
        return suspectIP.isEmpty();
    }
}
