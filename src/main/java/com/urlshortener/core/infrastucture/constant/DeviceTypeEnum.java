package com.urlshortener.core.infrastucture.constant;

public enum DeviceTypeEnum {
    MOBILE, TABLET, DESKTOP, UNKNOWN;

    public static DeviceTypeEnum fromUserAgent(eu.bitwalker.useragentutils.DeviceType deviceType) {
        return switch (deviceType) {
            case MOBILE -> MOBILE;
            case TABLET -> TABLET;
            case COMPUTER -> DESKTOP;
            default -> UNKNOWN;
        };
    }
}