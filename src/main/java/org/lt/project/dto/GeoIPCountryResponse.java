package org.lt.project.dto;

import lombok.Builder;

import java.util.Date;

@Builder
public record GeoIPCountryResponse(
        int id,
        String countryName,
        String isoCode,
        Date createdAt,
        String createdBy
) {}
