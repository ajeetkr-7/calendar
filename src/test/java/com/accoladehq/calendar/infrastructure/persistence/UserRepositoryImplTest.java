package com.accoladehq.calendar.infrastructure.persistence;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import com.accoladehq.calendar.domain.model.User;
import com.accoladehq.calendar.infrastructure.persistence.jpa.JpaUserRepository;

@DataJpaTest
@Import(UserRepositoryImpl.class)
class UserRepositoryImplTest {

    @Autowired
    private UserRepositoryImpl userRepository;

    @Autowired
    private JpaUserRepository jpaUserRepository;

    @BeforeEach
    void setUp() {
        jpaUserRepository.deleteAll();
        jpaUserRepository.flush();
    }

    User buildValidUser() {
        return User.builder()
                .id(null)
                .name("Jane Doe")
                .email("jane.doe@example.com")
                .build();
    }

    @Test
    void testSave() {
        User user = buildValidUser();
        User saved = userRepository.save(user);
        assertNotNull(saved.getId());
        assertEquals(user.getName(), saved.getName());
        assertEquals(user.getEmail(), saved.getEmail());
    }

    @Test
    void testFindById_existingUser() {
        User user = buildValidUser();
        User saved = userRepository.save(user);
        Optional<User> found = userRepository.findById(saved.getId());
        assertTrue(found.isPresent());
        assertEquals(saved.getId(), found.get().getId());
    }

    @Test
    void testFindById_nonExistingUser() {
        Optional<User> found = userRepository.findById(java.util.UUID.randomUUID());
        assertTrue(found.isEmpty());
    }

    @Test
    void testExistsByEmail() {
        User user = buildValidUser();
        userRepository.save(user);
        assertTrue(userRepository.existsByEmail(user.getEmail()));
        assertFalse(userRepository.existsByEmail("notfound@example.com"));
    }

    // existsById tests
    @Test
    void testExistsById_existingUser() {
        User user = buildValidUser();
        User saved = userRepository.save(user);
        assertTrue(userRepository.existsById(saved.getId()));
    }

    @Test
    void testExistsById_nonExistingUser() {
        assertFalse(userRepository.existsById(java.util.UUID.randomUUID()));
    }
}
