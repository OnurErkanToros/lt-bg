package org.lt.project.service;

import com.jcraft.jsch.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.lt.project.dto.ServerRequestDto;
import org.lt.project.dto.ServerResponseDto;
import org.lt.project.dto.resultDto.*;
import org.lt.project.exception.customExceptions.ResourceNotFoundException;
import org.lt.project.model.Server;
import org.lt.project.repository.ServerRepository;
import org.lt.project.util.convertor.ServerConverter;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ServerService {
  private final ServerRepository serverRepository;
  private final SettingsService settingsService;

  public DataResult<ServerResponseDto> getServerById(int id) {
    Optional<Server> serverEntityOptional = serverRepository.findById(id);
    if (serverEntityOptional.isPresent()) {
      ServerResponseDto serverResponseDto = ServerConverter.convert(serverEntityOptional.get());
      return new SuccessDataResult<>(serverResponseDto);
    } else {
      return new ErrorDataResult<>("Böyle bir kayıt bulunamadı.");
    }
  }

  public ServerResponseDto addServer(ServerRequestDto serverRequestDto) {
    System.out.println(serverRequestDto.isActive());
    Server server = serverRepository.save(ServerConverter.convert(serverRequestDto));
    return ServerConverter.convert(server);
  }

  public Boolean deleteServer(int id) {
    Optional<Server> serverEntityOptional = serverRepository.findById(id);
    if (serverEntityOptional.isEmpty()) {
      throw new ResourceNotFoundException("Silinecek server bulunamadı.");
    }
    serverRepository.delete(serverEntityOptional.get());
    return true;
  }

  public List<ServerResponseDto> getServerList() {
    List<Server> serverList = serverRepository.findAll();
    return serverList.stream().map(ServerConverter::convert).toList();
  }

  public boolean deleteServerByIdList(List<Integer> serverIds) {
    try {
      serverRepository.deleteAllById(serverIds);
      long count = serverRepository.countByIdIn(serverIds);
      if (count == 0) {
        return true;
      } else if (count < serverIds.size()) {
        throw new ResourceNotFoundException("Kayıtların bazıları silinemedi.");
      }
      throw new ResourceNotFoundException("Kayıtlar silinemedi.");
    } catch (Exception e) {
      throw new ResourceNotFoundException("Kayıtlar silinemedi.");
    }
  }

  public ServerResponseDto updateServer(ServerRequestDto requestDto, int id) {
    Optional<Server> serverEntityOptional = serverRepository.findById(id);
    if (serverEntityOptional.isEmpty()) {
      throw new ResourceNotFoundException("Güncellenecek server bulunamadı.");
    }
    serverEntityOptional.get().setName(requestDto.name());
    serverEntityOptional.get().setHost(requestDto.host());
    serverEntityOptional.get().setUsername(requestDto.username());
    serverEntityOptional.get().setPassword(requestDto.password());
    serverEntityOptional.get().setPort(requestDto.port());
    serverEntityOptional.get().setRemoteFilePath(requestDto.remoteFilePath());
    serverEntityOptional.get().setFileName(requestDto.fileName());
    serverEntityOptional.get().setActive(requestDto.isActive());
    serverEntityOptional.get().setSFTP(requestDto.isSFTP());
    Server server = serverRepository.save(serverEntityOptional.get());
    return ServerConverter.convert(server);
  }

  public List<Result> transferFile(List<ServerRequestDto> serverRequestDtos) {
    List<Result> resultList = new ArrayList<>();
    var confFileSetting = settingsService.getByKey("confFilePath");
    File localFile = new File(confFileSetting.value());
    if (!localFile.exists()) {
      throw new RuntimeException("Dosya bulunamadı: " + confFileSetting.value());
    }

    for (ServerRequestDto server : serverRequestDtos) {
      if (server.isSFTP()) {
        transferSftp(server, localFile, resultList);
      } else {
        transferFtp(server, localFile, resultList);
      }
    }
    return resultList;
  }

  private void transferFtp(ServerRequestDto server, File localFile, List<Result> resultList) {
    FTPClient ftpClient = new FTPClient();
    try (FileInputStream fis = new FileInputStream(localFile)) {
      ftpClient.connect(server.host(), server.port());
      boolean loginSuccess = ftpClient.login(server.username(), server.password());
      if (!loginSuccess) {
        resultList.add(new ErrorResult("FTP: Giriş başarısız: " + server.host()));
        return;
      }

      ftpClient.setFileType(FTP.BINARY_FILE_TYPE);

      String remoteFileName = server.remoteFilePath() + "/" + localFile.getName();
      boolean done = ftpClient.storeFile(remoteFileName, fis);

      if (done) {
        resultList.add(new SuccessResult("FTP: Dosya başarıyla transfer edildi: " + server.host()));
      } else {
        resultList.add(new ErrorResult("FTP: Dosya transferi başarısız: " + server.host()));
      }
    } catch (IOException e) {
      resultList.add(new ErrorResult("FTP Hatası: " + e.getMessage()));
    } finally {
      try {
        ftpClient.logout();
        ftpClient.disconnect();
      } catch (IOException e) {
        System.err.println("FTP bağlantısı kapatılamadı: " + e.getMessage());
      }
    }
  }

  private void transferSftp(ServerRequestDto server, File localFile, List<Result> resultList) {
    JSch jsch = new JSch();
    Session session = null;
    ChannelSftp sftpChannel = null;
    try {
      // Initialize and configure the session
      session = jsch.getSession(server.username(), server.host(), server.port());
      session.setPassword(server.password());
      session.setConfig("StrictHostKeyChecking", "no");

      // Connect the session
      session.connect();

      // Open the SFTP channel
      sftpChannel = (ChannelSftp) session.openChannel("sftp");
      sftpChannel.connect();

      // Perform the file transfer to the remote file path
      String remoteFilePath = server.remoteFilePath();
      sftpChannel.put(localFile.getAbsolutePath(), remoteFilePath + "/" + server.fileName());

      // Add success result
      resultList.add(new SuccessResult("SFTP: Dosya başarıyla transfer edildi: " + server.host()));
    } catch (JSchException | SftpException e) {
      // Add error result on failure
      resultList.add(new ErrorResult("SFTP Hatası: " + e.getMessage()));
    } finally {
      // Disconnect from the SFTP channel and session
      if (sftpChannel != null && sftpChannel.isConnected()) {
        sftpChannel.disconnect();
      }
      if (session != null && session.isConnected()) {
        session.disconnect();
      }
    }
  }
}
