package com.Tekarch.DataStoreService.Mapper;

import com.Tekarch.DataStoreService.DTO.BookingDto;
import com.Tekarch.DataStoreService.DTO.FlightDto;
import com.Tekarch.DataStoreService.DTO.UserDto;
import com.Tekarch.DataStoreService.Model.Booking;
import com.Tekarch.DataStoreService.Model.Flight;
import com.Tekarch.DataStoreService.Model.User;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

@Component
public class EntityDtoMapper {

    // Convert User Entity to UserDto
    public static UserDto fromEntity(User user) {
        return new UserDto(
                user.getUserId(),
                user.getUserName(),
                user.getEmail(),
                user.getPhoneNumber(),
                user.getCreatedAt(),
                user.getUpdatedAt()
                );
    }

    public static User toEntity(UserDto userDto) {
        User user = new User();
        user.setUserId(userDto.getUserId());
        user.setUserName(userDto.getUserName());
        user.setEmail(userDto.getEmail());
        user.setPhoneNumber(userDto.getPhoneNumber());
        user.setCreatedAt(userDto.getCreatedAt());
        user.setUpdatedAt(userDto.getUpdatedAt());
        return user;
    }

    // Convert Flight Entity to FlightDto
    public static FlightDto fromEntity(Flight flight) {
        return new FlightDto(flight.getFlightId(),
                flight.getFlightNumber(),
                flight.getDeparture(),
                flight.getArrival(),
                flight.getDepartureTime(),
                flight.getArrivalTime(),
                flight.getPrice(),
                flight.getAvailableSeats(),
                flight.getCreatedAt(),
                flight.getUpdatedAt()
        );
    }

    public static Flight toEntity(FlightDto flightDto) {
        Flight flight = new Flight();
        flight.setFlightId(flightDto.getFlightId());
        flight.setFlightNumber(flightDto.getFlightNumber());
        flight.setDeparture(flightDto.getDeparture());
        flight.setArrival(flightDto.getArrival());
        flight.setDepartureTime(flightDto.getDepartureTime());
        flight.setArrivalTime(flightDto.getArrivalTime());
        flight.setPrice(flightDto.getPrice());
        flight.setAvailableSeats(flightDto.getAvailableSeats());
        flight.setCreatedAt(flightDto.getCreatedAt());  // If required
        flight.setUpdatedAt(flightDto.getUpdatedAt());  // If required
        return flight;
    }

    // Convert Booking Entity to BookingDto
    public static BookingDto fromEntity(Booking booking) {
        return new BookingDto(
                booking.getBookingId(),
                booking.getUser() != null ? booking.getUser().getUserId() : null,  // Safely access userId
                booking.getFlight() != null ? booking.getFlight().getFlightId() : null,  // Safely access flightId
                booking.getStatus(),
                booking.getCreatedAt(),
                booking.getUpdatedAt());
    }

    public static Booking toEntity(BookingDto bookingDto, User user, Flight flight) {
        if (user == null) {
            throw new IllegalArgumentException("User with ID " + bookingDto.getUserId() + " not found.");
        }
        if (flight == null) {
            throw new IllegalArgumentException("Flight with ID " + bookingDto.getFlightId() + " not found.");
        }

        Booking booking = new Booking();
        booking.setBookingId(bookingDto.getBookingId());
        booking.setUser(user);
        booking.setFlight(flight);
        booking.setStatus(bookingDto.getStatus());
        booking.setCreatedAt(bookingDto.getCreatedAt());
        booking.setUpdatedAt(bookingDto.getUpdatedAt());
        return booking;
    }
    // Convert List of Booking Entities to List of BookingDtos
    public static List<BookingDto> fromEntities(List<Booking> bookings) {
        return bookings.stream()
                .map(EntityDtoMapper::fromEntity)  // Convert each Booking entity to BookingDto
                .collect(Collectors.toList());
    }
}
