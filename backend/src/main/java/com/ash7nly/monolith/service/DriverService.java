package com.ash7nly.monolith.service;

import com.ash7nly.monolith.dto.request.UpdateDriverRequest;
import com.ash7nly.monolith.dto.response.DriverResponse;
import com.ash7nly.monolith.entity.Driver;
import com.ash7nly.monolith.exception.BadRequestException;
import com.ash7nly.monolith.exception.ForbiddenException;
import com.ash7nly.monolith.exception.NotFoundException;
import com.ash7nly.monolith.mapper.DriverMapper;
import com.ash7nly.monolith.repository.DriverRepository;
import com.ash7nly.monolith.security.CurrentUserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class DriverService {

    private final DriverRepository driverRepository;
    private final DriverMapper driverMapper;
    private final CurrentUserService currentUserService;

    @Transactional(readOnly = true)
    public List<DriverResponse> getAllDrivers() {
        if (!currentUserService.isAdmin()) {
            throw new ForbiddenException("Only admins can view all drivers");
        }
        return driverRepository.findAll().stream()
                .map(driverMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public DriverResponse getDriver(Long id) {
        Driver driver = driverRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Driver not found with id: " + id));
        return driverMapper.toResponse(driver);
    }

    @Transactional(readOnly = true)
    public DriverResponse getMyDriverProfile() {
        Long userId = currentUserService.getCurrentUserId();
        Driver driver = driverRepository.findByUserId(userId)
                .orElseThrow(() -> new NotFoundException("Driver profile not found for user id: " + userId));
        return driverMapper.toResponse(driver);
    }

    @Transactional
    public DriverResponse updateDriver(Long id, UpdateDriverRequest request) {
        Driver driver = driverRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Driver not found with id: " + id));

        Long currentUserId = currentUserService.getCurrentUserId();
        if (driver.getUser() == null) {
            throw new BadRequestException("Driver has no associated user");
        }

        if (!currentUserService.isAdmin() && !driver.getUser().getId().equals(currentUserId)) {
            throw new ForbiddenException("Access denied: cannot update other drivers");
        }

        driverMapper.updateEntityFromRequest(driver, request);
        driver = driverRepository.save(driver);

        return driverMapper.toResponse(driver);
    }

    @Transactional
    public DriverResponse updateAvailability(Long id, boolean available) {
        Driver driver = driverRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Driver not found with id: " + id));

        Long currentUserId = currentUserService.getCurrentUserId();
        if (driver.getUser() == null) {
            throw new BadRequestException("Driver has no associated user");
        }

        if (!driver.getUser().getId().equals(currentUserId) && !currentUserService.isAdmin()) {
            throw new ForbiddenException("Access denied: cannot update other drivers");
        }

        driver.setAvailable(available);
        driver = driverRepository.save(driver);

        log.info("Driver {} availability updated to {}", id, available);
        return driverMapper.toResponse(driver);
    }
}

