package com.Tekarch.DataStoreService.DTO;

import com.Tekarch.DataStoreService.Model.Booking;
import com.Tekarch.DataStoreService.Model.Flight;
import com.Tekarch.DataStoreService.Model.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode
public class BookingDto {

    private Long bookingId;
    @NotNull(message = "User ID cannot be null")
    private Long userId;// Reference to User instead of the full User object
    @NotNull(message = "Flight ID cannot be null")
    private Long flightId; // Reference to Flight instead of the full Flight object
    @NotNull(message = "Status cannot be null")
    private Booking.BookingStatus status; // Enum for "BOOKED" or "CANCELLED"
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;


}
