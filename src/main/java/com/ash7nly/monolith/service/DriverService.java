package com.ash7nly.monolith.service;

import com.ash7nly.monolith.dto.request.CreateDriverRequest;
import com.ash7nly.monolith.dto.request.UpdateDriverRequest;
import com.ash7nly.monolith.dto.response.DriverResponse;
import com.ash7nly.monolith.entity.Driver;
import com.ash7nly.monolith.entity.User;
import com.ash7nly.monolith.enums.UserRole;
import com.ash7nly.monolith.exception.BadRequestException;
import com.ash7nly.monolith.exception.ForbiddenException;
import com.ash7nly.monolith.exception.NotFoundException;
import com.ash7nly.monolith.mapper.DriverMapper;
import com.ash7nly.monolith.repository.DriverRepository;
import com.ash7nly.monolith.repository.UserRepository;
import com.ash7nly.monolith.security.CurrentUserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class DriverService {

    private final DriverRepository driverRepository;
    private final UserRepository userRepository;
    private final DriverMapper driverMapper;
    private final CurrentUserService currentUserService;

    public DriverService(DriverRepository driverRepository, UserRepository userRepository,
                         DriverMapper driverMapper, CurrentUserService currentUserService) {
        this.driverRepository = driverRepository;
        this.userRepository = userRepository;
        this.driverMapper = driverMapper;
        this.currentUserService = currentUserService;
    }

    public List<DriverResponse> getAllDrivers() {
        if (!currentUserService.isAdmin()) {
            throw new ForbiddenException("Only admins can view all drivers");
        }
        return driverRepository.findAll().stream()
                .map(driverMapper::toResponse)
                .collect(Collectors.toList());
    }

    public DriverResponse getDriver(Long id) {
        Driver driver = driverRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Driver not found with id: " + id));
        return driverMapper.toResponse(driver);
    }

    public DriverResponse getMyDriverProfile() {
        Long userId = currentUserService.getCurrentUserId();
        Driver driver = driverRepository.findByUserId(userId)
                .orElseThrow(() -> new NotFoundException("Driver profile not found"));
        return driverMapper.toResponse(driver);
    }

    @Transactional
    public DriverResponse createDriver(CreateDriverRequest request) {
        if (!currentUserService.isAdmin()) {
            throw new ForbiddenException("Only admins can create driver profiles");
        }

        Long userId = currentUserService.getCurrentUserId();
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User not found"));

        // Check if driver profile already exists
        if (driverRepository.findByUserId(userId).isPresent()) {
            throw new BadRequestException("Driver profile already exists for this user");
        }

        // Update user role to DRIVER
        user.setRole(UserRole.DRIVER);
        userRepository.save(user);

        Driver driver = driverMapper.toEntity(request, user);
        driver = driverRepository.save(driver);

        return driverMapper.toResponse(driver);
    }

    @Transactional
    public DriverResponse createDriverForUser(Long userId, CreateDriverRequest request) {
        if (!currentUserService.isAdmin()) {
            throw new ForbiddenException("Only admins can create driver profiles");
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User not found"));

        // Check if driver profile already exists
        if (driverRepository.findByUserId(userId).isPresent()) {
            throw new BadRequestException("Driver profile already exists for this user");
        }

        // Update user role to DRIVER
        user.setRole(UserRole.DRIVER);
        userRepository.save(user);

        Driver driver = driverMapper.toEntity(request, user);
        driver = driverRepository.save(driver);

        return driverMapper.toResponse(driver);
    }

    @Transactional
    public DriverResponse updateDriver(Long id, UpdateDriverRequest request) {
        Driver driver = driverRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Driver not found"));

        // Only admin or the driver themselves can update
        Long currentUserId = currentUserService.getCurrentUserId();
        if (!currentUserService.isAdmin() && !driver.getUser().getId().equals(currentUserId)) {
            throw new ForbiddenException("Access denied");
        }

        driverMapper.updateEntityFromRequest(driver, request);
        driver = driverRepository.save(driver);

        return driverMapper.toResponse(driver);
    }

    @Transactional
    public DriverResponse updateAvailability(Long id, boolean available) {
        Driver driver = driverRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Driver not found"));

        Long currentUserId = currentUserService.getCurrentUserId();
        if (!driver.getUser().getId().equals(currentUserId) && !currentUserService.isAdmin()) {
            throw new ForbiddenException("Access denied");
        }

        driver.setAvailable(available);
        driver = driverRepository.save(driver);

        return driverMapper.toResponse(driver);
    }
}

