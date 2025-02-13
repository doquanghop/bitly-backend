package com.urlshortener.core.domain.shortener.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.Random;

@Service
public class UrlEncoderService  {

    private static final String BASE62 = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";

    @Value("${url.short.max-size}")
    private int maxEncodedLength;
    @Value("${url.short.min-size}")
    private int minEncodedLength;

    public String encode(String text) {
        try {
            // Bước 1: Băm URL với SHA-256
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(text.getBytes(StandardCharsets.UTF_8));

            // Bước 2: Chuyển đổi hash thành chuỗi Base62
            String base62 = encodeBase62(hash);

            int length = getLengthEncoded();
            return getRandomSubstring(base62, length);
        } catch (Exception e) {
            throw new RuntimeException("Error while encoding text", e);
        }
    }

    private String encodeBase62(byte[] hash) {
        StringBuilder sb = new StringBuilder();
        for (byte b : hash) {
            int index = (b & 0xFF) % BASE62.length(); // Lấy index trong phạm vi 62 ký tự
            sb.append(BASE62.charAt(index));
        }
        return sb.toString();
    }

    private int getLengthEncoded() {
        return (int) (Math.random() * (maxEncodedLength - minEncodedLength + 1)) + minEncodedLength;
    }

    private String getRandomSubstring(String base62, int length) {
        Random random = new Random();
        int start = random.nextInt(base62.length() - length); // Lấy vị trí bắt đầu ngẫu nhiên
        return base62.substring(start, start + length);
    }
}