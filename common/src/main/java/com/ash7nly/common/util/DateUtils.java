package com.ash7nly.common.util;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public class DateUtils {
    private static final String DATE_PATTERN = "yyyy-MM-dd HH:mm:ss";
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern(DATE_PATTERN)
            .withZone(ZoneId.of("UTC"));

    private DateUtils() {}

    public static String format(Instant instant) {
        if (instant == null) return null;
        return FORMATTER.format(instant);
    }

    public static Instant now() {
        return Instant.now();
    }
}
