package com.taxiapp.booking.repository;

import com.taxiapp.booking.entity.Booking;
import com.taxiapp.booking.entity.BookingStatus;
import com.taxiapp.driver.entity.DriverProfile;
import com.taxiapp.user.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface BookingRepository extends JpaRepository<Booking, UUID> {
    Page<Booking> findByPassengerOrderByCreatedAtDesc(User passenger, Pageable pageable);
    Page<Booking> findByDriverOrderByCreatedAtDesc(DriverProfile driver, Pageable pageable);
    Optional<Booking> findByIdAndPassenger(UUID id, User passenger);
    Optional<Booking> findByIdAndDriver(UUID id, DriverProfile driver);
    List<Booking> findByDriverAndStatusIn(DriverProfile driver, List<BookingStatus> statuses);
    List<Booking> findByPassengerAndStatusIn(User passenger, List<BookingStatus> statuses);
    boolean existsByPassengerAndStatusIn(User passenger, List<BookingStatus> statuses);
    boolean existsByDriverAndStatusIn(DriverProfile driver, List<BookingStatus> statuses);
} 