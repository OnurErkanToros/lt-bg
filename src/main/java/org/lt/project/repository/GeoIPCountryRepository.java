package org.lt.project.repository;

import org.lt.project.model.GeoIPCountry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface GeoIPCountryRepository extends JpaRepository<GeoIPCountry, Integer> {
    boolean existsByIsoCodeAndAllowed(String isoCode,boolean allowed);
}
