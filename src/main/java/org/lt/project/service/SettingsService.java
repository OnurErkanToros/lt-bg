package org.lt.project.service;

import lombok.RequiredArgsConstructor;
import org.lt.project.dto.SettingsRequestDto;
import org.lt.project.dto.SettingsResponseDto;
import org.lt.project.exception.customExceptions.ResourceNotFoundException;
import org.lt.project.model.Settings;
import org.lt.project.repository.SettingsRepository;
import org.lt.project.util.convertor.SettingsConverter;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SettingsService {
    private final SettingsRepository settingsRepository;
    public SettingsResponseDto add(SettingsRequestDto settingsRequestDto){
        Settings setting = SettingsConverter.convert(settingsRequestDto);
        Settings savedSetting = settingsRepository.save(setting);
        return SettingsConverter.convert(savedSetting);
    }

    public SettingsResponseDto update(SettingsRequestDto settingsRequestDto, long id){
        Optional<Settings> settingsOptional = settingsRepository.findById(id);
        if(settingsOptional.isPresent()){
            settingsOptional.get().setSettingType(settingsRequestDto.settingType());
            settingsOptional.get().setVariableType(settingsRequestDto.variableType());
            settingsOptional.get().setSettingKey(settingsRequestDto.key());
            settingsOptional.get().setValue(settingsRequestDto.value());
            settingsOptional.get().setTitle(settingsRequestDto.title());
            settingsOptional.get().setDescription(settingsRequestDto.description());
            settingsOptional.get().setUpdatedAt(new Date());
            settingsOptional.get().setUpdatedBy(UserService.getAuthenticatedUser());
            return SettingsConverter.convert(settingsRepository.save(settingsOptional.get()));
        }else {
            throw new ResourceNotFoundException();
        }
    }

    public List<SettingsResponseDto> getAll(){
        return settingsRepository.findAll().stream().map(SettingsConverter::convert).toList();
    }

    public List<SettingsResponseDto> getAllBySettingType(Settings.SettingType settingType){
        return settingsRepository.findBySettingType(settingType).stream().map(SettingsConverter::convert).toList();
    }

    public SettingsResponseDto getByKey(String key){
        Optional<Settings> settingsOptional = settingsRepository.findFirstBySettingKey(key);
        if (settingsOptional.isEmpty()){
            throw new ResourceNotFoundException("İlgili key için sonuç yok.");
        }
        return SettingsConverter.convert(settingsOptional.get());
    }

    public SettingsResponseDto updateValueByKey(String key,String value){
        Optional<Settings> settingsOptional = settingsRepository.findFirstBySettingKey(key);
        if(settingsOptional.isEmpty()){
            throw new ResourceNotFoundException("İlgili key için sonuç yok.");
        }
        settingsOptional.get().setValue(value);
        settingsOptional.get().setUpdatedAt(new Date());
        settingsOptional.get().setUpdatedBy(UserService.getAuthenticatedUser());
        var savedSetting = settingsRepository.save(settingsOptional.get());
        return SettingsConverter.convert(savedSetting);
    }
}
