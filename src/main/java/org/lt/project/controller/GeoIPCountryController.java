package org.lt.project.controller;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.lt.project.dto.GeoIPCountryResponse;
import org.lt.project.service.GeoIPCountryService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/geo-ip-countries")
@RequiredArgsConstructor
@SecurityRequirement(name = "Authorization")
public class GeoIPCountryController {
    private final GeoIPCountryService geoIPCountryService;

    @GetMapping("/countries")
    public ResponseEntity<List<GeoIPCountryResponse>> getAllCountries() {
        return ResponseEntity.ok(geoIPCountryService.getAllCountry());
    }

    @PostMapping("/allow")
    public ResponseEntity<List<GeoIPCountryResponse>> markAsAllowed(@RequestBody List<Integer> ids) {
        return ResponseEntity.ok(geoIPCountryService.markAsAllowed(ids));
    }

    @PostMapping("/deny")
    public ResponseEntity<List<GeoIPCountryResponse>> markAsDenied(@RequestBody List<Integer> ids) {
        return ResponseEntity.ok(geoIPCountryService.markAsDenied(ids));
    }
}
