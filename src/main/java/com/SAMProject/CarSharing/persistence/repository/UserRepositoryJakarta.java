package com.SAMProject.CarSharing.persistence.repository;

import com.SAMProject.CarSharing.persistence.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepositoryJakarta extends JpaRepository<User, Integer> {
}
