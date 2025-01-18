package org.lt.project.controller;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.lt.project.dto.SettingsRequestDto;
import org.lt.project.dto.SettingsResponseDto;
import org.lt.project.model.Settings;
import org.lt.project.service.SettingsService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/settings/")
@SecurityRequirement(name = "Authorization")
@RequiredArgsConstructor
public class SettingsApi {
    private final SettingsService settingsService;

    @GetMapping("get/key/{key}")
    public ResponseEntity<SettingsResponseDto> getByKey(@PathVariable String key){
        return ResponseEntity.ok(settingsService.getByKey(key));
    }

    @GetMapping("get/all")
    public ResponseEntity<List<SettingsResponseDto>> getAll(){
        return ResponseEntity.ok(settingsService.getAll());
    }

    @GetMapping("get/setting-type/{settingType}")
    public ResponseEntity<List<SettingsResponseDto>> getAllBySettingType(@PathVariable Settings.SettingType settingType){
        return ResponseEntity.ok(settingsService.getAllBySettingType(settingType));
    }

    @PostMapping("add")
    public ResponseEntity<SettingsResponseDto> add(@RequestBody SettingsRequestDto settingsRequestDto){
        return ResponseEntity.ok(settingsService.add(settingsRequestDto));
    }

    @PatchMapping("update/{id}")
    public ResponseEntity<SettingsResponseDto> update(@RequestBody SettingsRequestDto settingsRequestDto,@PathVariable long id){
        return ResponseEntity.ok(settingsService.update(settingsRequestDto,id));
    }
    @PatchMapping("update")
    public ResponseEntity<SettingsResponseDto> update(@RequestParam String key, @RequestParam String value){
        System.out.println(key);
        return ResponseEntity.ok(settingsService.updateValueByKey(key, value));
    }
    @PostMapping("create-settings")
    public ResponseEntity<Boolean> createSettings(){
        return ResponseEntity.ok(settingsService.createSettings());
    }
}
