package com.taxiapp.booking.service;

import com.taxiapp.booking.entity.Booking;
import com.taxiapp.booking.entity.BookingStatus;
import com.taxiapp.booking.repository.BookingRepository;
import com.taxiapp.common.exception.ApiException;
import com.taxiapp.driver.entity.DriverProfile;
import com.taxiapp.driver.service.DriverService;
import com.taxiapp.user.entity.User;
import com.taxiapp.user.entity.UserType;
import com.taxiapp.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BookingService {

    private final BookingRepository bookingRepository;
    private final UserService userService;
    private final DriverService driverService;

    @Transactional
    public Booking createBooking(Booking booking) {
        User currentUser = userService.getCurrentUser();
        if (currentUser.getUserType() != UserType.PASSENGER) {
            throw new ApiException(HttpStatus.FORBIDDEN, "Only passengers can create bookings");
        }

        List<BookingStatus> activeStatuses = List.of(
            BookingStatus.PENDING, BookingStatus.ACCEPTED, BookingStatus.STARTED
        );

        if (bookingRepository.existsByPassengerAndStatusIn(currentUser, activeStatuses)) {
            throw new ApiException(HttpStatus.CONFLICT, "You already have an active booking");
        }

        booking.setPassenger(currentUser);
        booking.setStatus(BookingStatus.PENDING);
        return bookingRepository.save(booking);
    }

    @Transactional
    public Booking acceptBooking(UUID bookingId) {
        DriverProfile driverProfile = driverService.getCurrentDriverProfile();
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "Booking not found"));

        if (booking.getStatus() != BookingStatus.PENDING) {
            throw new ApiException(HttpStatus.CONFLICT, "Booking cannot be accepted");
        }

        List<BookingStatus> activeStatuses = List.of(
            BookingStatus.ACCEPTED, BookingStatus.STARTED
        );

        if (bookingRepository.existsByDriverAndStatusIn(driverProfile, activeStatuses)) {
            throw new ApiException(HttpStatus.CONFLICT, "You already have an active booking");
        }

        booking.setDriver(driverProfile);
        booking.setStatus(BookingStatus.ACCEPTED);
        return bookingRepository.save(booking);
    }

    @Transactional
    public Booking startRide(UUID bookingId) {
        DriverProfile driverProfile = driverService.getCurrentDriverProfile();
        Booking booking = bookingRepository.findByIdAndDriver(bookingId, driverProfile)
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "Booking not found"));

        if (booking.getStatus() != BookingStatus.ACCEPTED) {
            throw new ApiException(HttpStatus.CONFLICT, "Booking cannot be started");
        }

        booking.setStatus(BookingStatus.STARTED);
        booking.setStartedAt(LocalDateTime.now());
        return bookingRepository.save(booking);
    }

    @Transactional
    public Booking completeRide(UUID bookingId) {
        DriverProfile driverProfile = driverService.getCurrentDriverProfile();
        Booking booking = bookingRepository.findByIdAndDriver(bookingId, driverProfile)
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "Booking not found"));

        if (booking.getStatus() != BookingStatus.STARTED) {
            throw new ApiException(HttpStatus.CONFLICT, "Booking cannot be completed");
        }

        booking.setStatus(BookingStatus.COMPLETED);
        booking.setCompletedAt(LocalDateTime.now());
        booking.setFinalPrice(booking.getEstimatedPrice()); // In real app, calculate based on actual distance/time
        return bookingRepository.save(booking);
    }

    @Transactional
    public Booking cancelBooking(UUID bookingId, String reason) {
        User currentUser = userService.getCurrentUser();
        Booking booking;

        if (currentUser.getUserType() == UserType.PASSENGER) {
            booking = bookingRepository.findByIdAndPassenger(bookingId, currentUser)
                    .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "Booking not found"));
        } else if (currentUser.getUserType() == UserType.DRIVER) {
            DriverProfile driverProfile = driverService.getCurrentDriverProfile();
            booking = bookingRepository.findByIdAndDriver(bookingId, driverProfile)
                    .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "Booking not found"));
        } else {
            throw new ApiException(HttpStatus.FORBIDDEN, "Only passengers and drivers can cancel bookings");
        }

        if (!List.of(BookingStatus.PENDING, BookingStatus.ACCEPTED).contains(booking.getStatus())) {
            throw new ApiException(HttpStatus.CONFLICT, "Booking cannot be cancelled");
        }

        booking.setStatus(BookingStatus.CANCELLED);
        booking.setCancelledAt(LocalDateTime.now());
        booking.setCancellationReason(reason);
        return bookingRepository.save(booking);
    }

    @Transactional
    public Booking rateBooking(UUID bookingId, Integer rating, String review, boolean isPassenger) {
        User currentUser = userService.getCurrentUser();
        Booking booking;

        if (isPassenger && currentUser.getUserType() == UserType.PASSENGER) {
            booking = bookingRepository.findByIdAndPassenger(bookingId, currentUser)
                    .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "Booking not found"));
            booking.setDriverRating(rating);
            booking.setDriverReview(review);
        } else if (!isPassenger && currentUser.getUserType() == UserType.DRIVER) {
            DriverProfile driverProfile = driverService.getCurrentDriverProfile();
            booking = bookingRepository.findByIdAndDriver(bookingId, driverProfile)
                    .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "Booking not found"));
            booking.setPassengerRating(rating);
            booking.setPassengerReview(review);
        } else {
            throw new ApiException(HttpStatus.FORBIDDEN, "Invalid rating request");
        }

        if (booking.getStatus() != BookingStatus.COMPLETED) {
            throw new ApiException(HttpStatus.CONFLICT, "Can only rate completed bookings");
        }

        return bookingRepository.save(booking);
    }

    @Transactional(readOnly = true)
    public Page<Booking> getMyBookings(Pageable pageable) {
        User currentUser = userService.getCurrentUser();
        if (currentUser.getUserType() == UserType.PASSENGER) {
            return bookingRepository.findByPassengerOrderByCreatedAtDesc(currentUser, pageable);
        } else if (currentUser.getUserType() == UserType.DRIVER) {
            DriverProfile driverProfile = driverService.getCurrentDriverProfile();
            return bookingRepository.findByDriverOrderByCreatedAtDesc(driverProfile, pageable);
        }
        throw new ApiException(HttpStatus.FORBIDDEN, "Invalid user type for bookings");
    }

    @Transactional(readOnly = true)
    public Booking getBooking(UUID bookingId) {
        User currentUser = userService.getCurrentUser();
        if (currentUser.getUserType() == UserType.PASSENGER) {
            return bookingRepository.findByIdAndPassenger(bookingId, currentUser)
                    .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "Booking not found"));
        } else if (currentUser.getUserType() == UserType.DRIVER) {
            DriverProfile driverProfile = driverService.getCurrentDriverProfile();
            return bookingRepository.findByIdAndDriver(bookingId, driverProfile)
                    .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "Booking not found"));
        }
        throw new ApiException(HttpStatus.FORBIDDEN, "Invalid user type for bookings");
    }
} 