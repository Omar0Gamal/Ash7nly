package com.ash7nly.monolith.service;

import com.ash7nly.monolith.dto.request.UserUpdateRequest;
import com.ash7nly.monolith.dto.response.UserResponse;
import com.ash7nly.monolith.entity.User;
import com.ash7nly.monolith.exception.ForbiddenException;
import com.ash7nly.monolith.exception.NotFoundException;
import com.ash7nly.monolith.mapper.UserMapper;
import com.ash7nly.monolith.repository.UserRepository;
import com.ash7nly.monolith.security.CurrentUserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final CurrentUserService currentUserService;

    public UserService(UserRepository userRepository, CurrentUserService currentUserService) {
        this.userRepository = userRepository;
        this.currentUserService = currentUserService;
    }

    public UserResponse getUserById(Long id) {
        Long currentUserId = currentUserService.getCurrentUserId();

        if (!currentUserId.equals(id) && !currentUserService.isAdmin()) {
            throw new ForbiddenException("Access denied");
        }

        User user = userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("User not found with id: " + id));

        return UserMapper.toResponse(user);
    }

    public UserResponse getMyProfile() {
        Long userId = currentUserService.getCurrentUserId();
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User not found"));
        return UserMapper.toResponse(user);
    }

    @Transactional
    public UserResponse updateUser(Long id, UserUpdateRequest request) {
        Long currentUserId = currentUserService.getCurrentUserId();

        if (!currentUserId.equals(id) && !currentUserService.isAdmin()) {
            throw new ForbiddenException("Access denied");
        }

        User user = userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("User not found"));

        boolean isUpdated = false;

        if (request.getFullName() != null && !request.getFullName().isBlank()) {
            user.setFullName(request.getFullName());
            isUpdated = true;
        }

        if (request.getPhoneNumber() != null && !request.getPhoneNumber().isBlank()) {
            user.setPhoneNumber(request.getPhoneNumber());
            isUpdated = true;
        }

        if (isUpdated) {
            user = userRepository.save(user);
        }

        return UserMapper.toResponse(user);
    }
}

