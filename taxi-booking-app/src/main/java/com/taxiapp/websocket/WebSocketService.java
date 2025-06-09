package com.taxiapp.websocket;

import com.taxiapp.booking.entity.Booking;
import com.taxiapp.driver.entity.DriverProfile;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class WebSocketService {

    private final SimpMessagingTemplate messagingTemplate;

    public void sendBookingUpdate(String userId, Booking booking) {
        messagingTemplate.convertAndSendToUser(
            userId,
            "/queue/bookings",
            booking
        );
    }

    public void sendDriverLocationUpdate(String userId, DriverProfile driverProfile) {
        messagingTemplate.convertAndSendToUser(
            userId,
            "/queue/driver-location",
            new LocationUpdate(
                driverProfile.getId(),
                driverProfile.getCurrentLatitude(),
                driverProfile.getCurrentLongitude()
            )
        );
    }

    public void broadcastNearbyDriver(DriverProfile driverProfile) {
        messagingTemplate.convertAndSend(
            "/topic/nearby-drivers",
            new LocationUpdate(
                driverProfile.getId(),
                driverProfile.getCurrentLatitude(),
                driverProfile.getCurrentLongitude()
            )
        );
    }
}

record LocationUpdate(
    java.util.UUID driverId,
    Double latitude,
    Double longitude
) {} 