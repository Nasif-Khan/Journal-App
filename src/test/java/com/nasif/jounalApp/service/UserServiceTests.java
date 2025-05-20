package com.nasif.jounalApp.service;

import com.nasif.jounalApp.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Disabled
public class UserServiceTests {
    @Autowired
    private UserRepository userRepository;

    @Test
    public void testFindByUserName() {
        assertNotNull(userRepository.findByUserName("nasif"));
    }
}
