package com.urlshortener.core.infrastucture.utils;

import com.urlshortener.core.infrastucture.constant.BrowserTypeEnum;
import com.urlshortener.core.infrastucture.constant.DeviceTypeEnum;
import com.urlshortener.core.infrastucture.constant.OperatingSystemTypeEnum;
import eu.bitwalker.useragentutils.Browser;
import eu.bitwalker.useragentutils.UserAgent;

public class UserAgentParser {
    public static DeviceTypeEnum getDeviceType(String userAgent) {
        UserAgent userAgentObj = UserAgent.parseUserAgentString(userAgent);
        return DeviceTypeEnum.fromUserAgent(userAgentObj.getOperatingSystem().getDeviceType());
    }
    public static BrowserTypeEnum getBrowser(String userAgent) {
        UserAgent userAgentObj = UserAgent.parseUserAgentString(userAgent);
        Browser browser = userAgentObj.getBrowser();
        return BrowserTypeEnum.fromUserAgent(userAgentObj.getBrowser());
    }

    public static OperatingSystemTypeEnum getOperatingSystem(String userAgent) {
        UserAgent userAgentObj = UserAgent.parseUserAgentString(userAgent);
        return OperatingSystemTypeEnum.fromUserAgent(userAgentObj.getOperatingSystem());
    }
}