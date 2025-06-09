package com.taxiapp.driver.controller;

import com.taxiapp.driver.entity.DriverProfile;
import com.taxiapp.driver.service.DriverService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/drivers")
@RequiredArgsConstructor
@Validated
public class DriverController {

    private final DriverService driverService;

    @GetMapping("/me")
    @PreAuthorize("hasRole('DRIVER')")
    public ResponseEntity<DriverProfile> getCurrentDriverProfile() {
        return ResponseEntity.ok(driverService.getCurrentDriverProfile());
    }

    @PostMapping("/profile")
    @PreAuthorize("hasRole('DRIVER')")
    public ResponseEntity<DriverProfile> createDriverProfile(@Valid @RequestBody DriverProfile driverProfile) {
        return ResponseEntity.ok(driverService.createDriverProfile(driverProfile));
    }

    @PutMapping("/location")
    @PreAuthorize("hasRole('DRIVER')")
    public ResponseEntity<DriverProfile> updateLocation(
            @RequestParam @Min(-90) @Max(90) Double latitude,
            @RequestParam @Min(-180) @Max(180) Double longitude) {
        return ResponseEntity.ok(driverService.updateLocation(latitude, longitude));
    }

    @PutMapping("/availability")
    @PreAuthorize("hasRole('DRIVER')")
    public ResponseEntity<DriverProfile> updateAvailability(@RequestParam boolean available) {
        return ResponseEntity.ok(driverService.updateAvailability(available));
    }

    @GetMapping("/nearby")
    public ResponseEntity<List<DriverProfile>> findNearbyDrivers(
            @RequestParam @Min(-90) @Max(90) Double latitude,
            @RequestParam @Min(-180) @Max(180) Double longitude,
            @RequestParam @Min(0) @Max(50) Double radiusInKm) {
        return ResponseEntity.ok(driverService.findNearbyDrivers(latitude, longitude, radiusInKm));
    }
} 