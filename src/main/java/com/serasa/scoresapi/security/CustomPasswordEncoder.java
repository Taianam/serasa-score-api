package com.serasa.scoresapi.security;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class CustomPasswordEncoder implements PasswordEncoder {

    private final PasswordUtil passwordUtil;

    public CustomPasswordEncoder(PasswordUtil passwordUtil) {
        this.passwordUtil = passwordUtil;
    }

    @Override
    public String encode(CharSequence rawPassword) {
        return passwordUtil.encryptPassword(rawPassword.toString());
    }

    @Override
    public boolean matches(CharSequence rawPassword, String encodedPassword) {
        return passwordUtil.verifyPassword(rawPassword.toString(), encodedPassword);
    }
}
