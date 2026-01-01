package org.tp1ex2.projetspring.security;

import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * Custom password encoder that supports both plain text (for backward compatibility)
 * and BCrypt encoded passwords.
 */
public class PlainTextPasswordEncoder implements PasswordEncoder {

    private final PasswordEncoder bcryptEncoder = new org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder();

    @Override
    public String encode(CharSequence rawPassword) {
        // Always encode new passwords with BCrypt
        return bcryptEncoder.encode(rawPassword);
    }

    @Override
    public boolean matches(CharSequence rawPassword, String encodedPassword) {
        // Try BCrypt first
        if (bcryptEncoder.matches(rawPassword, encodedPassword)) {
            return true;
        }
        // Fallback to plain text comparison for backward compatibility
        return rawPassword.toString().equals(encodedPassword);
    }
}

