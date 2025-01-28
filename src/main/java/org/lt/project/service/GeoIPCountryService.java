package org.lt.project.service;

import lombok.RequiredArgsConstructor;
import org.lt.project.dto.GeoIPCountryResponse;
import org.lt.project.exception.customExceptions.ResourceNotFoundException;
import org.lt.project.model.GeoIPCountry;
import org.lt.project.repository.GeoIPCountryRepository;
import org.lt.project.util.converter.GeoIPCountryConverter;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class GeoIPCountryService {
    private final GeoIPCountryRepository geoIPCountryRepository;

    public List<GeoIPCountryResponse> getAllAllowedCountry() {
        return geoIPCountryRepository.findAll().stream().map(GeoIPCountryConverter::convert).toList();
    }

    public boolean isAllowed(String isoCode) {
        return geoIPCountryRepository.existsByIsoCodeAndAllowed(isoCode,true);
    }


    public GeoIPCountryResponse markAsAllowed(int id){
        Optional<GeoIPCountry> geoIPCountryOptional = geoIPCountryRepository.findById(id);
        if (geoIPCountryOptional.isPresent()) {
            geoIPCountryOptional.get().setAllowed(true);
            return GeoIPCountryConverter.convert(geoIPCountryRepository.save(geoIPCountryOptional.get()));
        } else {
            throw new ResourceNotFoundException("Böyle bir kayıt bulunamadı.");
        }
    }

    public GeoIPCountryResponse markAsDenied(int id){
        Optional<GeoIPCountry> geoIPCountryOptional = geoIPCountryRepository.findById(id);
        if (geoIPCountryOptional.isPresent()) {
            geoIPCountryOptional.get().setAllowed(false);
            return GeoIPCountryConverter.convert(geoIPCountryRepository.save(geoIPCountryOptional.get()));
        } else {
            throw new ResourceNotFoundException("Böyle bir kayıt bulunamadı.");
        }
    }

}
