package com.ash7nly.common.security;

import com.ash7nly.common.enums.UserRole;
import com.ash7nly.common.exception.UnauthorizedException;
import jakarta.servlet.http.HttpServletRequest;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import java.util.Arrays;
import java.util.List;

@Aspect
@Component
public class RoleCheckAspect {

    @Before("@annotation(requiresRole)")
    public void checkRole(RequiresRole requiresRole) {
        String currentRoleStr = UserContext.getUserRole();

        if (currentRoleStr == null || currentRoleStr.isBlank()) {
            throw new UnauthorizedException("Access Denied: Missing user role header");
        }

        try {
            // Convert String header to Enum
            UserRole currentRole = UserRole.valueOf(currentRoleStr);

            // Check if the current role is in the allowed list
            List<UserRole> allowedRoles = Arrays.asList(requiresRole.value());

            if (!allowedRoles.contains(currentRole)) {
                throw new UnauthorizedException("Access Denied: You do not have permission to perform this action");
            }
        } catch (IllegalArgumentException e) {
            throw new UnauthorizedException("Access Denied: Invalid role data");
        }
    }
}
