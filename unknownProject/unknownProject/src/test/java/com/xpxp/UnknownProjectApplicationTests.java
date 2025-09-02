package com.xpxp;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootTest
class UnknownProjectApplicationTests {

    @Test
    void contextLoads() {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String rawPassword = "admin123"; // 例如 "123456"
        System.out.println(encoder.encode(rawPassword));
    }

    @Test
    void contextLoads2() {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String rawPassword = "user123"; // 例如 "123456"
        System.out.println(encoder.encode(rawPassword));
    }

}
