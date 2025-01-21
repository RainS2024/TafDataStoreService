package com.Tekarch.DataStoreService.Repositories;

import com.Tekarch.DataStoreService.Model.Flight;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FlightRepository extends JpaRepository<Flight, Long> {
}
