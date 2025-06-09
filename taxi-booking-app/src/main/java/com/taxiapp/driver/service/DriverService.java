package com.taxiapp.driver.service;

import com.taxiapp.common.exception.ApiException;
import com.taxiapp.driver.entity.DriverProfile;
import com.taxiapp.driver.repository.DriverProfileRepository;
import com.taxiapp.user.entity.User;
import com.taxiapp.user.entity.UserType;
import com.taxiapp.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DriverService {

    private final DriverProfileRepository driverProfileRepository;
    private final UserService userService;

    @Transactional(readOnly = true)
    public DriverProfile getCurrentDriverProfile() {
        User currentUser = userService.getCurrentUser();
        if (currentUser.getUserType() != UserType.DRIVER) {
            throw new ApiException(HttpStatus.FORBIDDEN, "Only drivers can access driver profiles");
        }
        return driverProfileRepository.findByUser(currentUser)
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "Driver profile not found"));
    }

    @Transactional
    public DriverProfile createDriverProfile(DriverProfile driverProfile) {
        User currentUser = userService.getCurrentUser();
        if (currentUser.getUserType() != UserType.DRIVER) {
            throw new ApiException(HttpStatus.FORBIDDEN, "Only drivers can create driver profiles");
        }
        
        if (driverProfileRepository.findByUser(currentUser).isPresent()) {
            throw new ApiException(HttpStatus.CONFLICT, "Driver profile already exists");
        }

        driverProfile.setUser(currentUser);
        return driverProfileRepository.save(driverProfile);
    }

    @Transactional
    public DriverProfile updateLocation(Double latitude, Double longitude) {
        DriverProfile profile = getCurrentDriverProfile();
        profile.setCurrentLatitude(latitude);
        profile.setCurrentLongitude(longitude);
        return driverProfileRepository.save(profile);
    }

    @Transactional
    public DriverProfile updateAvailability(boolean available) {
        DriverProfile profile = getCurrentDriverProfile();
        profile.setAvailable(available);
        return driverProfileRepository.save(profile);
    }

    @Transactional(readOnly = true)
    public List<DriverProfile> findNearbyDrivers(Double latitude, Double longitude, Double radiusInKm) {
        return driverProfileRepository.findNearbyAvailableDrivers(latitude, longitude, radiusInKm * 1000);
    }
} 