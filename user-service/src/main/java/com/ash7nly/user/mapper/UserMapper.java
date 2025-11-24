package com.ash7nly.user.mapper;

import com.ash7nly.common.dto.UserDto;
import com.ash7nly.user.entity.UserEntity;

public class UserMapper {

    public static UserDto toDto(UserEntity entity) {
        if (entity == null) return null;

        return UserDto.builder()
                .id(entity.getId())
                .email(entity.getEmail())
                .fullName(entity.getFullName())
                .phoneNumber(entity.getPhoneNumber())
                .role(entity.getRole())
                .build();
    }
}