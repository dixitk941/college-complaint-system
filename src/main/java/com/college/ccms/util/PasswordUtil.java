package com.college.ccms.util;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Stores a SHA-256 hash instead of the plain password.
 * A production system would use a slower hash such as bcrypt.
 */
public final class PasswordUtil {

    private static final String PREFIX = "ccms-sha256:";

    private PasswordUtil() {
    }

    public static String hash(String rawPassword) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hashed = digest.digest(("ccms::" + rawPassword).getBytes(StandardCharsets.UTF_8));
            StringBuilder hex = new StringBuilder(PREFIX);
            for (byte value : hashed) {
                hex.append(String.format("%02x", value));
            }
            return hex.toString();
        } catch (NoSuchAlgorithmException ex) {
            throw new IllegalStateException("SHA-256 is not available", ex);
        }
    }

    public static boolean matches(String rawPassword, String storedHash) {
        if (rawPassword == null || storedHash == null) {
            return false;
        }
        return hash(rawPassword).equals(storedHash);
    }
}
