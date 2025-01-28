package org.lt.project.service;

import java.util.Date;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.lt.project.dto.SuspectIpRequestDto;
import org.lt.project.dto.SuspectIpResponseDto;
import org.lt.project.exception.customExceptions.ResourceNotFoundException;
import org.lt.project.model.SuspectIP;
import org.lt.project.repository.SuspectIpRepository;
import org.lt.project.specification.SuspectIpSpecification;
import org.lt.project.util.converter.SuspectIpConverter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SuspectIpService {
  private final SuspectIpRepository suspectIpRepository;


  public Page<SuspectIpResponseDto> getAll(int page, int size) {
    Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
    return suspectIpRepository.findAll(pageable).map(SuspectIpConverter::convert);
  }

  public Page<SuspectIpResponseDto> getAllFiltered(
      int page, int size, SuspectIP.IpStatus ipStatus, String host, String ip) {
    Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
    Specification<SuspectIP> spec =
        Specification.where(SuspectIpSpecification.hasIp(ip))
            .and(SuspectIpSpecification.hasStatus(ipStatus))
            .and(SuspectIpSpecification.hasHost(host));
    Page<SuspectIpResponseDto> suspectIpResponseDtos = suspectIpRepository.findAll(spec, pageable).map(SuspectIpConverter::convert);
    return suspectIpResponseDtos;
  }

  public SuspectIP save(SuspectIpRequestDto suspectIpRequestDto) {
    SuspectIP suspectIP = SuspectIpConverter.convert(suspectIpRequestDto);
    return suspectIpRepository.save(suspectIP);
  }

  public boolean isHaveAnyIp(String ipAddress) {
    Optional<SuspectIP> suspectIP = suspectIpRepository.findFirstByIpAddress(ipAddress);
    return suspectIP.isPresent();
  }

  public void changeStatus(SuspectIP.IpStatus ipStatus, String ipAddress) {
    Optional<SuspectIP> suspectIPOptional = suspectIpRepository.findFirstByIpAddress(ipAddress);
    if (suspectIPOptional.isEmpty()) {
      throw new ResourceNotFoundException("Status değiştirme için gerekli IP bulunamadı.");
    }

    suspectIPOptional.get().setStatus(ipStatus);
    suspectIPOptional.get().setStatusAt(new Date());
    suspectIPOptional.get().setStatusBy(UserService.getAuthenticatedUser());
    suspectIpRepository.save(suspectIPOptional.get());
  }
}
