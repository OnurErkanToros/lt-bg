package org.lt.project.core;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.lt.project.core.result.ErrorResult;
import org.lt.project.core.result.Result;
import org.lt.project.core.result.SuccessResult;
import org.springframework.stereotype.Component;

import java.io.*;
import java.util.List;

@Component
public class FileUtil {
    public void saveFile(List<String> ipAddressesToBanList, String fileName) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("src/main/resources/"+fileName))) {
            for (String item : ipAddressesToBanList) {
                writer.write("deny " + item + ";");
                writer.newLine();
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public Result sendFileViaFtp(String localFilePath, String remoteFilePath, String server, int port, String username, String password) {
        FTPClient ftpClient = new FTPClient();
        try {
            ftpClient.connect(server, port);
            ftpClient.login(username, password);
            ftpClient.enterLocalPassiveMode();
            ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
            FTPFile[] files = ftpClient.listFiles();
            for (FTPFile file : files) {
                System.out.println("File: " + file.getName());
            }
            File localFile = new File("src/main/resources/"+localFilePath);
            InputStream inputStream = new FileInputStream(localFile);
            boolean done = ftpClient.storeFile(remoteFilePath, inputStream);
            ftpClient.logout();
            if (done) {
                return new SuccessResult();
            } else {
                return new ErrorResult();
            }
        } catch (IOException e) {
            return new ErrorResult(e.getMessage());
        }finally {
            try {
                if (ftpClient.isConnected()){
                    ftpClient.disconnect();
                }
            }catch (IOException e){
                System.out.println(e.getMessage());
            }
        }
    }
}
