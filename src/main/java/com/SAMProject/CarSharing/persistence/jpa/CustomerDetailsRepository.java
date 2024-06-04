package com.SAMProject.CarSharing.persistence.jpa;

import com.SAMProject.CarSharing.persistence.entity.CustomerDetails;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerDetailsRepository extends JpaRepository<CustomerDetails, Integer> {
}
