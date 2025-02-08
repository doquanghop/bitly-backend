package com.urlshortener.core.domain.shortener.service.impl;

import com.urlshortener.core.domain.shortener.service.IUrlEncoder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

@Service
public class Sha256Base62Encoder implements IUrlEncoder {

    private static final String BASE62 = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";

    @Value("${url.short.max-size}")
    private int maxEncodedLength;
    @Value("${url.short.min-size}")
    private int minEncodedLength;

    @Override
    public String encode(String text) {
        try {
            // Bước 1: Băm URL với SHA-256
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(text.getBytes(StandardCharsets.UTF_8));

            // Bước 2: Chuyển đổi hash thành chuỗi Base62
            return encodeBase62(hash).substring(0, maxEncodedLength); // Lấy 6 ký tự đầu tiên
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
}