package com.urlshortener.core.infrastucture.utils;

import java.util.Date;

public class TimeUtils {
    public static long calculateRemainingTime (Date from, Date to) {
        long diffInMillis = to.getTime() - from.getTime();
        return diffInMillis > 0 ? diffInMillis / 1000 : 0;
    }
}
