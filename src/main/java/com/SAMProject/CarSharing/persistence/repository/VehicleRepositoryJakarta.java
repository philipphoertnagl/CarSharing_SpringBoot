package com.SAMProject.CarSharing.persistence.repository;

import com.SAMProject.CarSharing.persistence.entity.Vehicle;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VehicleRepositoryJakarta extends JpaRepository<Vehicle, Integer> {
}
