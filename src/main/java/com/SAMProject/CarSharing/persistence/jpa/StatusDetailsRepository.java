package com.SAMProject.CarSharing.persistence.jpa;

import com.SAMProject.CarSharing.persistence.entity.StatusDetails;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StatusDetailsRepository extends JpaRepository<StatusDetails, Integer> {
}
