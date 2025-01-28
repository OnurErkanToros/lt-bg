package org.lt.project.util.converter;

import org.lt.project.dto.GeoIPCountryResponse;
import org.lt.project.model.GeoIPCountry;

public class GeoIPCountryConverter {
    public static GeoIPCountryResponse convert(GeoIPCountry geoIPCountry) {
        return GeoIPCountryResponse.builder()
                .id(geoIPCountry.getId())
                .countryName(geoIPCountry.getCountryName())
                .isoCode(geoIPCountry.getIsoCode())
                .createdBy(geoIPCountry.getCreatedBy())
                .createdAt(geoIPCountry.getCreatedAt())
                .build();
    }

}
