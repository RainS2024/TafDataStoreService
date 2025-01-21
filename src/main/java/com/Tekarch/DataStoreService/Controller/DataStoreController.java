package com.Tekarch.DataStoreService.Controller;

import com.Tekarch.DataStoreService.DTO.BookingDto;
import com.Tekarch.DataStoreService.DTO.UserDto;
import com.Tekarch.DataStoreService.Exceptions.BookingNotFoundException;
import com.Tekarch.DataStoreService.Mapper.EntityDtoMapper;
import com.Tekarch.DataStoreService.Model.Booking;
import com.Tekarch.DataStoreService.Model.Flight;
import com.Tekarch.DataStoreService.Model.User;
import com.Tekarch.DataStoreService.Service.DatastoreService;
import jakarta.validation.Valid;
import lombok.Data;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/datastore")
public class DataStoreController {
    private static final Logger logger = LogManager.getLogger(DataStoreController.class);
    @Autowired
    private DatastoreService dataStoreService;

    // User CRUD Endpoints

    @GetMapping("/users")
    public ResponseEntity<List<User>> getAllUsers() {
        return ResponseEntity.ok(dataStoreService.getAllUsers());
    }

    @GetMapping("/users/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        User user = dataStoreService.getUserById(id);
        return ResponseEntity.ok(user);
    }

    @PostMapping("/users")
    public ResponseEntity<User> createUser(@RequestBody User user) {
        User createdUser = dataStoreService.createUser(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
    }

    @PutMapping("/users/{id}")
    public ResponseEntity<User> updateUser(@PathVariable Long id, @RequestBody User user) {
        User updatedUser = dataStoreService.updateUser(id, user);
        return ResponseEntity.ok(updatedUser);
    }

    @DeleteMapping("/users/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        dataStoreService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    // Flight CRUD Endpoints

    @GetMapping("/flights")
    public ResponseEntity<List<Flight>> getAllFlights() {
        return ResponseEntity.ok(dataStoreService.getAllFlights());
    }

    @GetMapping("/flights/{id}")
    public ResponseEntity<Flight> getFlightById(@PathVariable Long id) {
        Flight flight = dataStoreService.getFlightById(id);
        return ResponseEntity.ok(flight);
    }

    @PostMapping("/flights")
    public ResponseEntity<Flight> createFlight(@RequestBody Flight flight) {
        Flight createdFlight = dataStoreService.createFlight(flight);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdFlight);
    }

    @PutMapping("/flights/{id}")
    public ResponseEntity<Flight> updateFlight(@PathVariable Long id, @RequestBody Flight flight) {
        Flight updatedFlight = dataStoreService.updateFlight(id, flight);
        return ResponseEntity.ok(updatedFlight);
    }

    @DeleteMapping("/flights/{id}")
    public ResponseEntity<Void> deleteFlight(@PathVariable Long id) {
        dataStoreService.deleteFlight(id);
        return ResponseEntity.noContent().build();
    }

    // Booking CRUD Endpoints

//    @GetMapping("/bookings")
//    public ResponseEntity<List<Booking>> getAllBookings() {
//        return ResponseEntity.ok(dataStoreService.getAllBookings());
//    }

    // Get All Bookings
//    @GetMapping("/bookings")
//    public ResponseEntity<List<BookingDto>> getAllBookings() {
//        List<Booking> bookings = dataStoreService.getAllBookings();
//        List<BookingDto> bookingDtos = bookings.stream()
//                .map(EntityDtoMapper::fromEntity)
//                .collect(Collectors.toList());
//        return ResponseEntity.ok(bookingDtos);
//    }
    // Get All Bookings
    @GetMapping("/bookings")
    public ResponseEntity<List<BookingDto>> getAllBookings() {
        List<BookingDto> bookingDtos = dataStoreService.getAllBookings();
        return ResponseEntity.ok(bookingDtos);
    }

    @GetMapping("/bookings/{id}")
    public ResponseEntity<BookingDto> getBookingById(@PathVariable Long id) {
        // Fetch the booking entity from the service
        // Fetch the booking DTO from the service
        BookingDto bookingDto = dataStoreService.getBookingById(id);
        return ResponseEntity.ok(bookingDto);
    }

    @GetMapping("/bookings/user/{userId}")
    public ResponseEntity<List<BookingDto>> getBookingsByUserId(@PathVariable Long userId) {
        // Call the service layer to fetch bookings by user ID
        try {
            List<BookingDto> bookings = dataStoreService.getBookingsByUserId(userId);
            return ResponseEntity.ok(bookings);
        } catch (Exception e) {
            logger.error("Error fetching bookings for user {}: {}", userId, e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping("/bookings")
    public ResponseEntity<BookingDto> createBooking(@Valid @RequestBody BookingDto bookingDto) {
        // Call the service method to create the booking
        BookingDto createdBookingDto = dataStoreService.createBooking(bookingDto);

        // Return the created booking DTO with status CREATED
        return ResponseEntity.status(HttpStatus.CREATED).body(createdBookingDto);
    }

    @PutMapping("/bookings/{id}")
    public ResponseEntity<BookingDto> updateBooking(
            @PathVariable Long id,
            @Valid @RequestBody BookingDto bookingDto) {
        try {
            // Log the incoming request
            logger.info("Updating booking with ID: {}", id);

            // Call the service method to update the booking
            BookingDto updatedBookingDto = dataStoreService.updateBooking(id, bookingDto);

            // Return the updated booking DTO
            logger.info("Successfully updated booking with ID: {}", id);
            return ResponseEntity.ok(updatedBookingDto);
        } catch (BookingNotFoundException e) {
            logger.error("Booking not found for ID: {}. Error: {}", id, e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        } catch (Exception e) {
            logger.error("Error updating booking with ID: {}. Error: {}", id, e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @DeleteMapping("/bookings/{id}")
    public ResponseEntity<Void> deleteBooking(@PathVariable Long id) {
        dataStoreService.markBookingAsCancelled(id);
        return ResponseEntity.noContent().build();
    }
    @ExceptionHandler
    public ResponseEntity<String> responseWithError(Exception e) {
        logger.error("Exception Occured.Details : {}", e.getMessage());
        return new ResponseEntity<>("Exception Occured.More Info :"
                + e.getMessage(), HttpStatus.BAD_REQUEST);
    }
}
