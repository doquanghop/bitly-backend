package com.urlshortener.core.infrastucture.utils;

import eu.bitwalker.useragentutils.Browser;
import eu.bitwalker.useragentutils.DeviceType;
import eu.bitwalker.useragentutils.OperatingSystem;
import eu.bitwalker.useragentutils.UserAgent;

public class UserAgentParser {
    public static DeviceType getDeviceType(String userAgent) {
        UserAgent userAgentObj = UserAgent.parseUserAgentString(userAgent);
        return userAgentObj.getOperatingSystem().getDeviceType();
    }
    public static String getBrowser(String userAgent) {
        UserAgent userAgentObj = UserAgent.parseUserAgentString(userAgent);
        Browser browser = userAgentObj.getBrowser();
        return browser.getName();
    }

    public static String getOperatingSystem(String userAgent) {
        UserAgent userAgentObj = UserAgent.parseUserAgentString(userAgent);
        OperatingSystem os = userAgentObj.getOperatingSystem();
        return os.getName();
    }
}