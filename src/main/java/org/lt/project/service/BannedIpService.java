package org.lt.project.service;

import lombok.RequiredArgsConstructor;
import org.lt.project.dto.resultDto.*;
import org.lt.project.model.BannedIp;
import org.lt.project.repository.BannedIpRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BannedIpService {
    private final BannedIpRepository bannedIpRepository;

    public DataResult<Page<BannedIp>> getBannedIpList(int page, int size) {
        Pageable pageable = PageRequest.of(page,size);
        Page<BannedIp> bannedIpList = bannedIpRepository.findAllByTransferredTrue(pageable);
        if(bannedIpList.isEmpty()){
            return new ErrorDataResult<>("Liste boş.");
        }else {
            return new SuccessDataResult<>(bannedIpList);
        }
    }

    public DataResult<List<BannedIp>> addAllIp(List<BannedIp> bannedIps) {
        if(bannedIps.isEmpty()){
            return new ErrorDataResult<>("Liste boş");
        }else{
            List<String> ipList = bannedIps.stream().map(BannedIp::getIp).toList();
            List<BannedIp> existingIpAddress = bannedIpRepository.findByIpIn(ipList);

            List<BannedIp> filteredIpAddress = bannedIps.stream()
                    .filter(ip-> existingIpAddress
                            .stream()
                            .noneMatch(existingIp ->existingIp.getIp().equals(ip.getIp())))
                    .toList();

            List<BannedIp> addedBannedIpList = new ArrayList<>(bannedIpRepository.saveAll(filteredIpAddress));
            if (addedBannedIpList.isEmpty()) {
                return new ErrorDataResult<>("Liste eklenemedi.");
            } else {
                return new SuccessDataResult<>(addedBannedIpList);
            }
        }
    }

    public DataResult<BannedIp> addBannedIp(BannedIp bannedIp) {
        Optional<BannedIp> existingBannedIp = bannedIpRepository.findByIp(bannedIp.getIp());
        if (existingBannedIp.isEmpty()) {
            return new SuccessDataResult<>(bannedIpRepository.save(bannedIp));
        } else {
            return new ErrorDataResult<>("Zaten var.");
        }
    }

    public Result deleteBannedIpList(List<BannedIp> bannedIpList) {
        if (bannedIpList.isEmpty()) {
            return new ErrorResult("Liste boş.");
        } else {
            bannedIpRepository.deleteAll(bannedIpList);
            return new SuccessResult();
        }
    }

    public Result deleteBannedIp(BannedIp bannedIp) {
        bannedIpRepository.delete(bannedIp);
        return new SuccessResult();
    }

    public DataResult<Long> getUnTransferredCount(){
        return new SuccessDataResult<>(bannedIpRepository.countAllByTransferredFalse());
    }
}
