package com.SAMProject.CarSharing.persistence.jpa;

import com.SAMProject.CarSharing.persistence.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepositoryJakarta extends JpaRepository<User, Integer> {

    Optional<User> findByUsername(String username);
}
