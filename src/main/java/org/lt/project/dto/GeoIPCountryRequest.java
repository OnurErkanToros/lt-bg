package org.lt.project.dto;

public record GeoIPCountryRequest(
    String countryName,
    String isoCode
) {}
