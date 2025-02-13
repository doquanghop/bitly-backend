package com.urlshortener.core.infrastucture.constant;

import lombok.Getter;

@Getter
public enum ReferrerTypeEnum {
    DIRECT("Direct"),
    GOOGLE("Google"),
    BING("Bing"),
    YAHOO("Yahoo"),
    FACEBOOK("Facebook"),
    TWITTER("Twitter"),
    INSTAGRAM("Instagram"),
    LINKEDIN("LinkedIn"),
    YOUTUBE("YouTube"),
    REDDIT("Reddit"),
    PINTEREST("Pinterest"),
    TIKTOK("TikTok"),
    WHATSAPP("WhatsApp"),
    EMAIL("Email"),
    GOOGLE_ADS("Google Ads"),
    FACEBOOK_ADS("Facebook Ads"),
    MAILCHIMP("Mailchimp"),
    ADWORDS("AdWords"),
    REFERRAL("Referral"),
    OTHER("Other");

    private final String referrerName;

    ReferrerTypeEnum(String referrerName) {
        this.referrerName = referrerName;
    }

    public static ReferrerTypeEnum fromString(String referrer) {
        for (ReferrerTypeEnum type : ReferrerTypeEnum.values()) {
            if (type.referrerName.equalsIgnoreCase(referrer)) {
                return type;
            }
        }
        return OTHER;
    }
}
