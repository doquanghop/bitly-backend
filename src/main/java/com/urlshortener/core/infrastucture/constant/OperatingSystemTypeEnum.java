package com.urlshortener.core.infrastucture.constant;

public enum OperatingSystemTypeEnum {
    WINDOWS, MAC_OS, LINUX, ANDROID, IOS, UNKNOWN;

    public static OperatingSystemTypeEnum fromUserAgent(eu.bitwalker.useragentutils.OperatingSystem os) {
        return switch (os.getGroup()) {
            case WINDOWS -> WINDOWS;
            case MAC_OS -> MAC_OS;
            case LINUX -> LINUX;
            case ANDROID -> ANDROID;
            case IOS -> IOS;
            default -> UNKNOWN;
        };
    }
}
