package nl.krijnschelvis.place2be.services;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class PasswordHasher {

    public static String hash(String password) {
        try {
            // Hash password
            MessageDigest messageDigest = MessageDigest.getInstance("MD5");
            messageDigest.update(password.getBytes());
            byte[] resultByteArray = messageDigest.digest();

            // Build string from byte[]
            StringBuilder sb = new StringBuilder();
            for (byte b: resultByteArray) {
                sb.append(String.format("%02x", b));
            }

            // Return hashed password
            return sb.toString();

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        // Return empty string
        return "";
    }
}
