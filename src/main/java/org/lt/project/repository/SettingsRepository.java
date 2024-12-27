package org.lt.project.repository;

import org.lt.project.model.Settings;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SettingsRepository extends JpaRepository<Settings,Long> {
    List<Settings> findBySettingType(Settings.SettingType settingType);
    Optional<Settings> findFirstBySettingKey(String settingKey);
}
