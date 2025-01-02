package org.lt.project.service;

import lombok.RequiredArgsConstructor;
import org.lt.project.exception.customExceptions.BadRequestException;
import org.lt.project.model.BanningIp;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class IPDatabaseSyncService {
    private final FileService fileService;
    private final BanningIpService banningIpService;

    private Set<String> readIPsFromFile() {
        return fileService.listIPAddresses().stream().collect(HashSet::new, HashSet::add, HashSet::addAll);
    }

    private Set<String> readIpsFromDB() {
        return banningIpService.getBannedAllIp().stream().map(BanningIp::getIp).collect(HashSet::new, HashSet::add, HashSet::addAll);
    }

    public boolean syncDatabaseWithFile() {
        try {
            Set<String> fileIPs = readIPsFromFile();
            Set<String> dbIPs = readIpsFromDB();

            for (String ip : fileIPs) {
                if (!dbIPs.contains(ip)) {
                    banningIpService.addBannedIp(
                            BanningIp
                                    .builder()
                                    .ip(ip)
                                    .createdAt(new Date())
                                    .createdBy(UserService.getAuthenticatedUser())
                                    .ipType(BanningIp.BanningIpType.LISTENER)
                                    .build()
                    );
                }
            }

            for (String ip : dbIPs) {
                if (!fileIPs.contains(ip)) {
                    banningIpService.deleteIpAddress(ip);
                }
            }
            return true;
        } catch (Exception e) {
            throw new BadRequestException(e.getMessage());
        }
    }

}
