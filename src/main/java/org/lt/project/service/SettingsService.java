package org.lt.project.service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import lombok.RequiredArgsConstructor;
import org.lt.project.dto.SettingsRequestDto;
import org.lt.project.dto.SettingsResponseDto;
import org.lt.project.exception.customExceptions.ResourceNotFoundException;
import org.lt.project.model.Settings;
import org.lt.project.repository.SettingsRepository;
import org.lt.project.util.converter.SettingsConverter;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SettingsService {
  private final SettingsRepository settingsRepository;

  public boolean createSettings() {
    try {
      var blokConfPathSetting = settingsRepository.findFirstBySettingKey("confFilePath");
      var errorLogPathSetting = settingsRepository.findFirstBySettingKey("logFilePath");
      var maxRetry = settingsRepository.findFirstBySettingKey("maxRetry");
      var findTime = settingsRepository.findFirstBySettingKey("findTime");
      var findTimeType = settingsRepository.findFirstBySettingKey("findTimeType");
      var maxMindDatabaseCountryFilePathSetting = settingsRepository.findFirstBySettingKey("maxMindDatabaseCountryFilePath");
      if (blokConfPathSetting.isEmpty()) {
        var setting =SettingsConverter.convert(
            SettingsRequestDto.builder()
                .settingType(Settings.SettingType.LOG_LISTENER)
                .description("ip listesi dosyasının konumu")
                .key("confFilePath")
                .title("ip listesi dosyası konumu")
                .variableType(Settings.VariableType.STRING)
                .value("/etc/nginx/blokip.conf")
                .build());
        settingsRepository.save(setting);
      }
      if (errorLogPathSetting.isEmpty()) {
        var setting =SettingsConverter.convert(
            SettingsRequestDto.builder()
                .settingType(Settings.SettingType.LOG_LISTENER)
                .description("Log dosyasının konumu")
                .key("logFilePath")
                .title("Log dosyası konumu")
                .variableType(Settings.VariableType.STRING)
                .value("/var/log/nginx/error.log")
                .build());
        settingsRepository.save(setting);
      }
      if (maxRetry.isEmpty()) {
        var setting = SettingsConverter.convert(
            SettingsRequestDto.builder()
                .settingType(Settings.SettingType.LOG_LISTENER)
                .description("Maksimum deneme sayısı")
                .key("maxRetry")
                .title("Maksimum deneme sayısı")
                .variableType(Settings.VariableType.INTEGER)
                .value("10")
                .build());
        settingsRepository.save(setting);
      }
      if (findTime.isEmpty()) {
        var setting = SettingsConverter.convert(
            SettingsRequestDto.builder()
                .settingType(Settings.SettingType.LOG_LISTENER)
                .description("Aranacak zaman aralığı")
                .key("findTime")
                .title("Aranacak zaman aralığı")
                .variableType(Settings.VariableType.INTEGER)
                .value("10")
                .build());
        settingsRepository.save(setting);
      }
      if (findTimeType.isEmpty()) {
        var setting =
            SettingsConverter.convert( SettingsRequestDto.builder()
                .key("findTimeType")
                .settingType(Settings.SettingType.LOG_LISTENER)
                .description("Aranacak zaman aralığının tipi")
                .title("Aranacak zaman aralığının tipi")
                .variableType(Settings.VariableType.STRING)
                .value("minute")
                .build());
        settingsRepository.save(setting);
      }
      if (maxMindDatabaseCountryFilePathSetting.isEmpty()) {
        var setting = SettingsConverter.convert(
            SettingsRequestDto.builder()
                .settingType(Settings.SettingType.LOG_LISTENER)
                .description("maxmind database konumu")
                .key("maxMindDatabaseCountryFilePath")
                .title("maxmind database konumu")
                .variableType(Settings.VariableType.STRING)
                .value("/usr/share/GeoIP/GeoLite2-Country.mmdb")
                .build());
        settingsRepository.save(setting);
      }
      return true;
    } catch (Exception e) {
      throw new ResourceNotFoundException("ayarlar oluşturulamadı.");
    }
  }

  public SettingsResponseDto add(SettingsRequestDto settingsRequestDto) {
    Settings setting = SettingsConverter.convert(settingsRequestDto);
    Settings savedSetting = settingsRepository.save(setting);
    return SettingsConverter.convert(savedSetting);
  }

  public SettingsResponseDto update(SettingsRequestDto settingsRequestDto, long id) {
    Optional<Settings> settingsOptional = settingsRepository.findById(id);
    if (settingsOptional.isPresent()) {
      settingsOptional.get().setSettingType(settingsRequestDto.settingType());
      settingsOptional.get().setVariableType(settingsRequestDto.variableType());
      settingsOptional.get().setSettingKey(settingsRequestDto.key());
      settingsOptional.get().setValue(settingsRequestDto.value());
      settingsOptional.get().setTitle(settingsRequestDto.title());
      settingsOptional.get().setDescription(settingsRequestDto.description());
      settingsOptional.get().setUpdatedAt(new Date());
      settingsOptional.get().setUpdatedBy(UserService.getAuthenticatedUser());
      return SettingsConverter.convert(settingsRepository.save(settingsOptional.get()));
    } else {
      throw new ResourceNotFoundException();
    }
  }

  public List<SettingsResponseDto> getAll() {
    return settingsRepository.findAll().stream().map(SettingsConverter::convert).toList();
  }

  public List<SettingsResponseDto> getAllBySettingType(Settings.SettingType settingType) {
    return settingsRepository.findBySettingType(settingType).stream()
        .map(SettingsConverter::convert)
        .toList();
  }

  public SettingsResponseDto getByKey(String key) {
    Optional<Settings> settingsOptional = settingsRepository.findFirstBySettingKey(key);
    if (settingsOptional.isEmpty()) {
      throw new ResourceNotFoundException(String.format("%s ayarı bulunamadı!", key));
    }
    return SettingsConverter.convert(settingsOptional.get());
  }

  public SettingsResponseDto updateValueByKey(String key, String value) {
    Optional<Settings> settingsOptional = settingsRepository.findFirstBySettingKey(key);
    if (settingsOptional.isEmpty()) {
      throw new ResourceNotFoundException(
          String.format("Güncellemek için %s ayarı bulunamadı!", key));
    }
    settingsOptional.get().setValue(value);
    settingsOptional.get().setUpdatedAt(new Date());
    settingsOptional.get().setUpdatedBy(UserService.getAuthenticatedUser());
    var savedSetting = settingsRepository.save(settingsOptional.get());
    return SettingsConverter.convert(savedSetting);
  }
}
