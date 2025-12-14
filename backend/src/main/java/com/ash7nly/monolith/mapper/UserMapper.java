package com.ash7nly.monolith.mapper;

import com.ash7nly.monolith.dto.response.UserResponse;
import com.ash7nly.monolith.entity.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public static UserResponse toResponse(User entity) {
        if (entity == null) {
            return null;
        }

        return UserResponse.builder()
                .id(entity.getId())
                .email(entity.getEmail())
                .fullName(entity.getFullName())
                .phoneNumber(entity.getPhoneNumber())
                .role(entity.getRole())
                .build();
    }
}
