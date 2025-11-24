package com.ash7nly.user.dto.req;

import com.ash7nly.common.enums.UserRole;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RegisterRequest {
    private String email;
    private String password;
    private String fullName;

    // Optional: Defaults to CUSTOMER in service if null
    private UserRole role;
}
