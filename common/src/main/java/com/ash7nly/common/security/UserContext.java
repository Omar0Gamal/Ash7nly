package com.ash7nly.common.security;

import com.ash7nly.common.constant.Headers;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

public class UserContext {
    public static String getUserId() {
        return getHeader(Headers.USER_ID);
    }

    public static String getUserRole() {
        return getHeader(Headers.USER_ROLE);
    }

    private static String getHeader(String headerName) {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attributes != null) {
            HttpServletRequest request = attributes.getRequest();
            return request.getHeader(headerName);
        }
        return null; // Or throw exception if preferred
    }
}
