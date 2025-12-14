package com.ash7nly.monolith.repository;

import com.ash7nly.monolith.entity.Payment;
import com.ash7nly.monolith.entity.ShipmentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {
    Optional<Payment> findByShipment(ShipmentEntity shipment);
    Optional<Payment> findByShipment_ShipmentId(Long shipmentId);
}

