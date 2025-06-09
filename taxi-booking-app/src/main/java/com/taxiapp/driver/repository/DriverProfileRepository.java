package com.taxiapp.driver.repository;

import com.taxiapp.driver.entity.DriverProfile;
import com.taxiapp.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface DriverProfileRepository extends JpaRepository<DriverProfile, UUID> {
    Optional<DriverProfile> findByUser(User user);
    
    @Query("""
        SELECT dp FROM DriverProfile dp
        WHERE dp.available = true
        AND dp.currentLatitude IS NOT NULL
        AND dp.currentLongitude IS NOT NULL
        AND function('earth_distance',
            function('ll_to_earth', dp.currentLatitude, dp.currentLongitude),
            function('ll_to_earth', :latitude, :longitude)
        ) <= :radiusInMeters
        ORDER BY function('earth_distance',
            function('ll_to_earth', dp.currentLatitude, dp.currentLongitude),
            function('ll_to_earth', :latitude, :longitude)
        )
    """)
    List<DriverProfile> findNearbyAvailableDrivers(Double latitude, Double longitude, Double radiusInMeters);
} 