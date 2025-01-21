package com.Tekarch.DataStoreService.Repositories;

import com.Tekarch.DataStoreService.Model.Booking;
import com.Tekarch.DataStoreService.Model.Flight;
import com.Tekarch.DataStoreService.Model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BookingRepository extends JpaRepository<Booking, Long> {
    List<Booking> findByUser(User user);
    List<Booking> findByFlight(Flight flight);
    List<Booking> findByStatus(Booking.BookingStatus status);
    Optional<Booking> findByUserAndFlight(User user, Flight flight);
    List<Booking> findByUser_UserId(Long userId);
}
