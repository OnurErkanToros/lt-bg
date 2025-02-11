package org.lt.project.service;

import lombok.RequiredArgsConstructor;
import org.lt.project.dto.GeoIPCountryResponse;
import org.lt.project.exception.customExceptions.ResourceNotFoundException;
import org.lt.project.model.GeoIPCountry;
import org.lt.project.repository.GeoIPCountryRepository;
import org.lt.project.util.converter.GeoIPCountryConverter;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GeoIPCountryService {
    private final GeoIPCountryRepository geoIPCountryRepository;

    public List<GeoIPCountryResponse> getAllCountry() {
        return geoIPCountryRepository.findAll(Sort.by(Sort.Order.asc("countryName")))
                .stream()
                .map(GeoIPCountryConverter::convert)
                .toList();
    }

    public boolean isAllowed(String isoCode) {
        return geoIPCountryRepository.existsByIsoCodeAndAllowed(isoCode, true);
    }

    public List<GeoIPCountryResponse> markAsAllowed(List<Integer> ids) {
        List<GeoIPCountry> countries = geoIPCountryRepository.findAllById(ids);

        if (countries.size() != ids.size()) {
            throw new ResourceNotFoundException("Bazı ülkeler bulunamadı.");
        }

        countries.forEach(country -> country.setAllowed(true));
        return geoIPCountryRepository.saveAll(countries)
                .stream()
                .map(GeoIPCountryConverter::convert)
                .collect(Collectors.toList());
    }

    public List<GeoIPCountryResponse> markAsDenied(List<Integer> ids) {
        List<GeoIPCountry> countries = geoIPCountryRepository.findAllById(ids);

        if (countries.size() != ids.size()) {
            throw new ResourceNotFoundException("Bazı ülkeler bulunamadı.");
        }

        countries.forEach(country -> country.setAllowed(false));
        return geoIPCountryRepository.saveAll(countries)
                .stream()
                .map(GeoIPCountryConverter::convert)
                .collect(Collectors.toList());
    }
}
