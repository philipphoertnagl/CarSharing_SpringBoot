package com.SAMProject.CarSharing;

import com.SAMProject.CarSharing.persistence.entity.User;
import com.SAMProject.CarSharing.persistence.jpa.UserRepositoryJakarta;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
public class UserRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private UserRepositoryJakarta userRepository;

    @BeforeEach
    public void setUp() {
        User user = new User("admin", "okey123");
        user.setRole(User.Role.MANAGER);
        entityManager.persist(user);
    }

    @Test
    public void findByUsernameReturnUserTest() {
        Optional<User> found = userRepository.findByUsername("admin");

        assertTrue(found.isPresent());
        assertEquals("admin", found.get().getUsername());
    }

    @Test
    public void noUserFindTest() {
        Optional<User> found = userRepository.findByUsername("maria");

        assertTrue(found.isEmpty());
    }
}
