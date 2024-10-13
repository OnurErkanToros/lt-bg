package org.lt.project.util;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.lt.project.dto.BanRequestDto;
import org.lt.project.dto.ServerRequestDto;
import org.lt.project.dto.resultDto.*;
import org.springframework.stereotype.Component;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

@Component
public class FileUtil {
    public File createAndWriteFile(List<BanRequestDto> ipAddressesToBanList) {
        File file = new File("src/main/resources/block.conf");
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            for (BanRequestDto item : ipAddressesToBanList) {
                writer.write("deny " + item.ip() + ";");
                writer.newLine();
            }
            return file;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public DataResult<List<Result>> sendFileByFtpForAllServers(File file, List<ServerRequestDto> serverRequestDtoList) {
        List<Result> resultList = new ArrayList<>();
        AtomicBoolean areThereAnyErrors = new AtomicBoolean(false);
        serverRequestDtoList.forEach(serverRequestDto -> {
            try {
                sendFileByFtp(file, serverRequestDto);
                resultList.add(new SuccessResult());
            } catch (Exception e) {
                areThereAnyErrors.set(true);
                resultList.add(new ErrorResult(e.getMessage()));
            }
        });
        if (areThereAnyErrors.get()) {
            return new ErrorDataResult<>(resultList);
        }
        return new SuccessDataResult<>(resultList);
    }

    private Boolean sendFileByFtp(File file, ServerRequestDto serverRequestDto) {
        FTPClient ftpClient = new FTPClient();
        try {
            ftpClient.connect(serverRequestDto.url(), serverRequestDto.port());
            ftpClient.login(serverRequestDto.username(), serverRequestDto.password());
            ftpClient.enterLocalPassiveMode();
            ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
            String remoteServerFilePath = serverRequestDto.remoteFilePath() + serverRequestDto.fileName();
            try (FileInputStream fileInputStream = new FileInputStream(file)) {
                boolean done = ftpClient.storeFile(remoteServerFilePath, fileInputStream);
                ftpClient.logout();
                return done;
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                if (ftpClient.isConnected()) {
                    ftpClient.disconnect();
                }
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
        }
    }
}
