package com.festival.initData;

import com.festival.domain.admin.data.entity.Admin;
import com.festival.domain.admin.repository.AdminRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

@Configuration
public class NotProd {
    @Bean
    CommandLineRunner initData(
            AdminRepository adminRepository,
            PasswordEncoder passwordEncoder
    ){
        return new CommandLineRunner() {
            @Override
            @Transactional
            public void run(String... args) throws Exception {
                Admin admin = Admin.builder()
                        .username("admin")
                        .password(passwordEncoder.encode("12345"))
                        .roles(Collections.singletonList("ADMIN")).build();

                adminRepository.save(admin);
            }
        };
    }
}
