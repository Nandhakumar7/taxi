package com.taxiapp.booking.controller;

import com.taxiapp.booking.entity.Booking;
import com.taxiapp.booking.service.BookingService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/bookings")
@RequiredArgsConstructor
@Validated
public class BookingController {

    private final BookingService bookingService;

    @PostMapping
    @PreAuthorize("hasRole('PASSENGER')")
    public ResponseEntity<Booking> createBooking(@Valid @RequestBody Booking booking) {
        return ResponseEntity.ok(bookingService.createBooking(booking));
    }

    @PostMapping("/{bookingId}/accept")
    @PreAuthorize("hasRole('DRIVER')")
    public ResponseEntity<Booking> acceptBooking(@PathVariable UUID bookingId) {
        return ResponseEntity.ok(bookingService.acceptBooking(bookingId));
    }

    @PostMapping("/{bookingId}/start")
    @PreAuthorize("hasRole('DRIVER')")
    public ResponseEntity<Booking> startRide(@PathVariable UUID bookingId) {
        return ResponseEntity.ok(bookingService.startRide(bookingId));
    }

    @PostMapping("/{bookingId}/complete")
    @PreAuthorize("hasRole('DRIVER')")
    public ResponseEntity<Booking> completeRide(@PathVariable UUID bookingId) {
        return ResponseEntity.ok(bookingService.completeRide(bookingId));
    }

    @PostMapping("/{bookingId}/cancel")
    public ResponseEntity<Booking> cancelBooking(
            @PathVariable UUID bookingId,
            @RequestParam(required = false) String reason) {
        return ResponseEntity.ok(bookingService.cancelBooking(bookingId, reason));
    }

    @PostMapping("/{bookingId}/rate")
    public ResponseEntity<Booking> rateBooking(
            @PathVariable UUID bookingId,
            @RequestParam @Min(1) @Max(5) Integer rating,
            @RequestParam(required = false) String review,
            @RequestParam boolean isPassenger) {
        return ResponseEntity.ok(bookingService.rateBooking(bookingId, rating, review, isPassenger));
    }

    @GetMapping
    public ResponseEntity<Page<Booking>> getMyBookings(Pageable pageable) {
        return ResponseEntity.ok(bookingService.getMyBookings(pageable));
    }

    @GetMapping("/{bookingId}")
    public ResponseEntity<Booking> getBooking(@PathVariable UUID bookingId) {
        return ResponseEntity.ok(bookingService.getBooking(bookingId));
    }
} 