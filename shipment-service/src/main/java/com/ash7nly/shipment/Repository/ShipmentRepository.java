package com.ash7nly.shipment.Repository;

import com.ash7nly.shipment.Entity.ShipmentEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ShipmentRepository extends JpaRepository<ShipmentEntity,Long> {

}