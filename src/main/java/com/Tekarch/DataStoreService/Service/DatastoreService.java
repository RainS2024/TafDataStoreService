package com.Tekarch.DataStoreService.Service;

import com.Tekarch.DataStoreService.DTO.BookingDto;
import com.Tekarch.DataStoreService.DTO.UserDto;
import com.Tekarch.DataStoreService.Mapper.EntityDtoMapper;
import com.Tekarch.DataStoreService.Model.Booking;
import com.Tekarch.DataStoreService.Model.Flight;
import com.Tekarch.DataStoreService.Model.User;
import com.Tekarch.DataStoreService.Repositories.BookingRepository;
import com.Tekarch.DataStoreService.Repositories.FlightRepository;
import com.Tekarch.DataStoreService.Repositories.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class DatastoreService {
    @Autowired
    private static final Logger logger = LogManager.getLogger(DatastoreService.class);
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private BookingRepository bookingRepository;
    @Autowired
    private FlightRepository flightRepository;

    // Get All Users
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    // Get User By ID
    @Transactional
    public User getUserById(Long id) {
        return userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found with id " + id));
    }

    // Create User
    public User createUser(User user) {
        return userRepository.save(user);
    }

    // Update User
    public User updateUser(Long id, User user) {
        if (!userRepository.existsById(id)) {
            throw new RuntimeException("User not found");
        }
        user.setUserId(id);  // Ensure we are updating the correct user
        return userRepository.save(user);
    }

    // Delete User
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    // Get All Flights
    public List<Flight> getAllFlights() {
        return flightRepository.findAll();
    }

    // Get Flight By ID
    public Flight getFlightById(Long id) {
        return flightRepository.findById(id).orElseThrow(() -> new RuntimeException("Flight not found"));
    }

    // Create Flight
    public Flight createFlight(Flight flight) {
        return flightRepository.save(flight);
    }

    // Update Flight
    public Flight updateFlight(Long id, Flight flight) {
        if (!flightRepository.existsById(id)) {
            throw new RuntimeException("Flight not found");
        }
        flight.setFlightId(id);
        return flightRepository.save(flight);
    }

    // Delete Flight
    public void deleteFlight(Long id) {
        flightRepository.deleteById(id);
    }

    // Get All Bookings
    public List<BookingDto> getAllBookings() {
        List<Booking> bookings = bookingRepository.findAll();
        //return EntityDtoMapper.fromEntity(bookings);
        // Convert list of entities to list of DTOs
        return bookings.stream()
                .map(EntityDtoMapper::fromEntity) // Convert list of Booking entities to BookingDto
                .collect(Collectors.toList());
    }

//    // Get Booking By ID
//    public Booking getBookingById(Long id) {
//        return bookingRepository.findById(id).orElseThrow(() -> new RuntimeException("Booking not found"));
//    }

    // Get Booking By ID
    public BookingDto getBookingById(Long id) {
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Booking not found"));
        return EntityDtoMapper.fromEntity(booking);  // Convert entity to DTO
    }

    // Retrieve all bookings for a specific user
    public List<BookingDto> getBookingsByUserId(Long userId) {
        // Check if the user exists
        if (!userRepository.existsById(userId)) {
            throw new EntityNotFoundException("User with ID " + userId + " does not exist.");
        }
        // Fetch bookings by user ID
        List<Booking> bookings = bookingRepository.findByUser_UserId(userId);
        if (bookings.isEmpty()) {
            logger.info("No bookings found for user ID {}", userId);
        }
        // Convert entities to DTOs
        return EntityDtoMapper.fromEntities(bookings);
    }



    // Create Booking
    @Transactional
    public BookingDto createBooking(BookingDto bookingDto) {
        // Fetch Flight and User entities
        Flight flight = flightRepository.findById(bookingDto.getFlightId())
                .orElseThrow(() -> new RuntimeException("Flight not found"));

        User user = userRepository.findById(bookingDto.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Use EntityDtoMapper to convert BookingDto to Booking entity
        Booking booking = EntityDtoMapper.toEntity(bookingDto, user, flight);
        // Set additional fields like createdAt and updatedAt
        booking.setCreatedAt(LocalDateTime.now());
        booking.setUpdatedAt(LocalDateTime.now());

        // Save the Booking entity to the database
        Booking savedBooking = bookingRepository.save(booking);

        // Convert the saved Booking entity back to a BookingDto and return it
        return EntityDtoMapper.fromEntity(savedBooking);
    }

    @Transactional
    public BookingDto updateBooking(Long id, BookingDto bookingDto) {
        // Log the incoming update request
        logger.info("Updating booking with ID: {}", id);
        // Fetch the existing Booking entity
        Booking existingBooking = bookingRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Booking not found"));

        // Fetch the Flight and User entities to update the Booking
        Flight flight = flightRepository.findById(bookingDto.getFlightId())
                .orElseThrow(() -> new RuntimeException("Flight not found"));

        User user = userRepository.findById(bookingDto.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Use EntityDtoMapper to convert BookingDto to Booking entity
        Booking updatedBooking = EntityDtoMapper.toEntity(bookingDto, user, flight);

        updatedBooking.setBookingId(id);
        updatedBooking.setCreatedAt(existingBooking.getCreatedAt());  // Preserve createdAt
        updatedBooking.setUpdatedAt(LocalDateTime.now());  // Update updatedAt

        // Save the updated Booking entity to the database
        Booking savedBooking = bookingRepository.save(updatedBooking);
        // Log the update
        logger.info("Booking updated with ID: {}", savedBooking.getBookingId());

        // Convert the saved Booking entity back to a BookingDto and return it
        return EntityDtoMapper.fromEntity(savedBooking);
    }

    @Transactional
    public void markBookingAsCancelled(Long id) {
        logger.info("Attempting to cancel booking with ID {}", id);
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Booking not found with id " + id));
        if (booking.getStatus() == Booking.BookingStatus.CANCELLED) {
            throw new RuntimeException("Booking is already cancelled.");
        }
                 booking.setStatus(Booking.BookingStatus.valueOf("CANCELLED"));
        bookingRepository.save(booking);
        // Log booking cancellation
        logger.info("Booking with ID {} has been marked as cancelled", id);
    }
    // Delete Booking
    @Transactional
    public void deleteBooking(Long id) {
        if (!bookingRepository.existsById(id)) {
            throw new EntityNotFoundException("Booking not found with id " + id);
        }
        bookingRepository.deleteById(id);
    }
}
