package com.ash7nly.monolith.repository;

import com.ash7nly.monolith.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {
    Optional<Payment> findByShipment_ShipmentId(@Param("shipmentId") Long shipmentId);
}

