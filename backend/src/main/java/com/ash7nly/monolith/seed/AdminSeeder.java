package com.ash7nly.monolith.seed;

import com.ash7nly.monolith.entity.User;
import com.ash7nly.monolith.enums.UserRole;
import com.ash7nly.monolith.repository.UserRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class AdminSeeder {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @PostConstruct
    public void init() {
        if (userRepository.findByEmail("admin@ash7nly.com").isEmpty()) {
            User admin = User.builder()
                    .email("admin@ash7nly.com")
                    .passwordHash(passwordEncoder.encode("admin123"))
                    .fullName("System Administrator")
                    .role(UserRole.ADMIN)
                    .isActive(true)
                    .build();
            userRepository.save(admin);
            log.info("Default admin created: admin@ash7nly.com / admin123");
        }
    }
}
