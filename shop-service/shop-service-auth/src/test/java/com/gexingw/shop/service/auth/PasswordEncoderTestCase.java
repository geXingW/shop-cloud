package com.gexingw.shop.service.auth;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * shop-cloud.
 *
 * @author GeXingW
 * @date 2023/7/30 18:54
 */
@ExtendWith(MockitoExtension.class)
public class PasswordEncoderTestCase {

    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Test
    void generatePassword(){
        System.out.println(passwordEncoder.encode("123456"));
    }
}
