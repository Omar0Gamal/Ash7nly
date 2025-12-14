package com.ash7nly.monolith.repository;

import com.ash7nly.monolith.entity.Delivery;
import com.ash7nly.monolith.entity.Driver;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DriverRepository extends JpaRepository<Driver, Long> {

    Optional<Driver> findByUserId(@Param("userId") Long userId);

    @Query("SELECT d FROM Delivery d " +
            "JOIN FETCH d.shipment s " +
            "WHERE s.deliveryAddress = (SELECT dr.serviceArea FROM Driver dr WHERE dr.id = :driverId) " +
            "AND s.isActive = true " +
            "AND d.driver IS NULL " +
            "ORDER BY s.createdAt ASC")
    List<Delivery> findAvailableDeliveriesForDriver(@Param("driverId") Long driverId);
}
