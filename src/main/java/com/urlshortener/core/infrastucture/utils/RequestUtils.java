package com.urlshortener.core.infrastucture.utils;

import jakarta.servlet.http.HttpServletRequest;

public class RequestUtils {
    public static String getIpAddress(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }

    public static String getUserAgent(HttpServletRequest request) {
        return request.getHeader("User-Agent");
    }

    public static String getReferrer(HttpServletRequest request) {
        return request.getHeader("Referer");
    }
}
