package com.ash7nly.delivery.Services;

import com.ash7nly.delivery.Entity.Driver;
import com.ash7nly.delivery.repository.DriverRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DriverService {
    private final DriverRepository _driverRepository;


    public List<Driver> getAllDrivers() {
        return _driverRepository.findAll();
    }


    public Driver getDriver(Long id) {
        return _driverRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Driver not found: " + id));
    }


    public Driver createDriver(Driver driver) {
        driver.setAvailable(true);
        return _driverRepository.save(driver);
    }


    public Driver updateDriver(Long id, Driver updated) {
        Driver d = getDriver(id);
        d.setVehicleType(updated.getVehicleType());
        d.setVehicleNumber(updated.getVehicleNumber());
        d.setLicenseNumber(updated.getLicenseNumber());
        d.setServiceArea(updated.getServiceArea());
        d.setAvailable(updated.isAvailable());
        return _driverRepository.save(d);
    }


    public Driver updateAvailability(Long id, boolean available) {
        Driver d = getDriver(id);
        d.setAvailable(available);
        return _driverRepository.save(d);
    }
}

