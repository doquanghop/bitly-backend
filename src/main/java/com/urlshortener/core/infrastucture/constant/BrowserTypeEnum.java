package com.urlshortener.core.infrastucture.constant;

public enum BrowserTypeEnum {
    CHROME, FIREFOX, EDGE, SAFARI, OPERA, INTERNET_EXPLORER, UNKNOWN;

    public static BrowserTypeEnum fromUserAgent(eu.bitwalker.useragentutils.Browser browser) {
        return switch (browser.getGroup()) {
            case CHROME -> CHROME;
            case FIREFOX -> FIREFOX;
            case EDGE -> EDGE;
            case SAFARI -> SAFARI;
            case OPERA -> OPERA;
            default -> UNKNOWN;
        };
    }
}