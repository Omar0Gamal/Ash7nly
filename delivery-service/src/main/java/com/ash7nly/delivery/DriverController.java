package com.ash7nly.delivery;

import com.ash7nly.delivery.Entity.Driver;
import com.ash7nly.delivery.Services.DriverService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/drivers")
@RequiredArgsConstructor
public class DriverController {
    private final DriverService driverService;


    @GetMapping("/all")
    public List<Driver> all() {
        return driverService.getAllDrivers();
    }


    @GetMapping("/{id}")
    public Driver get(@PathVariable Long id) {
        return driverService.getDriver(id);
    }


    @PostMapping("/create")
    public ResponseEntity<Driver> create(@RequestBody Driver driver) {
        return ResponseEntity.ok(driverService.createDriver(driver));
    }


    @PutMapping("/update/{id}")
    public Driver update(@PathVariable Long id, @RequestBody Driver updated) {
        return driverService.updateDriver(id, updated);
    }


    @GetMapping("/{id}/deliveries")
    public ResponseEntity<?> deliveries(@PathVariable Long id) {
        Driver d = driverService.getDriver(id);
        return ResponseEntity.ok(d.getDeliveries());
    }


    @PutMapping("/{id}/availability")
    public Driver setAvailability(@PathVariable Long id, @RequestParam boolean available) {
        return driverService.updateAvailability(id, available);

}}
