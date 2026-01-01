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
        if (rawPassword == null || encodedPassword == null) {
            return false;
        }
        
        // Try BCrypt first (if it looks like a BCrypt hash)
        // BCrypt hashes start with $2a$, $2b$, $2x$, or $2y$ and are typically 60 characters long
        // But we'll be more lenient and check if it starts with $2
        if (encodedPassword.startsWith("$2")) {
            try {
                if (bcryptEncoder.matches(rawPassword, encodedPassword)) {
                    return true;
                }
            } catch (IllegalArgumentException e) {
                // Not a valid BCrypt hash, fall through to plain text comparison
                // This can happen if the hash format is corrupted
            }
        }
        
        // Fallback to plain text comparison for backward compatibility
        return rawPassword.toString().equals(encodedPassword);
    }
}

