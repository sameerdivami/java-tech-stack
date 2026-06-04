package com.familyleague.config;

import com.familyleague.entity.User;
import com.familyleague.enums.Role;
import com.familyleague.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class DataInitializer implements ApplicationRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(ApplicationArguments args) {
        if (!userRepository.existsByUsernameAndDeletedFalse("admin")) {
            User admin = User.builder()
                    .username("admin")
                    .email("admin@familyleague.com")
                    .password(passwordEncoder.encode("Admin@123"))
                    .displayName("Administrator")
                    .avatarName("Admin")
                    .role(Role.ADMIN)
                    .build();
            userRepository.save(admin);
            log.info("Default admin user created — username: admin, password: Admin@123");
        }
    }
}
