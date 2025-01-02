package org.lt.project.service;

import lombok.RequiredArgsConstructor;
import org.lt.project.model.BanningIp;
import org.lt.project.repository.BanningIpRepository;
import org.lt.project.specification.BanningIpSpecification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BanningIpService {
    private final BanningIpRepository banningIpRepository;

    public Page<BanningIp> getBannedAllIpPageable(String ip, BanningIp.BanningIpType ipType, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Specification<BanningIp> spec = Specification.where(BanningIpSpecification.hasIp(ip)).and(BanningIpSpecification.hasIpType(ipType));
        return banningIpRepository.findAll(spec,pageable);
    }

    public List<BanningIp> getBannedAllIp(){
        return banningIpRepository.findAll();
    }

    public void addAllIp(List<BanningIp> banningIps) {
        List<String> ipList = banningIps.stream().map(BanningIp::getIp).toList();
        List<BanningIp> existingIpAddress = banningIpRepository.findByIpIn(ipList);
        List<BanningIp> filteredIpAddress = banningIps.stream()
                .filter(ip -> existingIpAddress
                        .stream()
                        .noneMatch(existingIp -> existingIp.getIp().equals(ip.getIp())))
                .toList();
        banningIpRepository.saveAll(filteredIpAddress);
    }

    public void addBannedIp(BanningIp banningIp) {
        Optional<BanningIp> existingBannedIp = banningIpRepository.findByIp(banningIp.getIp());
        if (existingBannedIp.isEmpty()) {
            banningIpRepository.save(banningIp);
        }
    }

    public void deleteIpAddress(String ip) {
        Optional<BanningIp> optionalBanningIp = banningIpRepository.findByIp(ip);
        optionalBanningIp.ifPresent(banningIpRepository::delete);
    }
}
