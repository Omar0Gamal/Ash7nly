package com.ash7nly.monolith.repository;

import com.ash7nly.monolith.entity.Shipment;
import com.ash7nly.monolith.enums.DeliveryArea;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ShipmentRepository extends JpaRepository<Shipment, Long> {

    Optional<Shipment> findByTrackingNumber(@Param("trackingNumber") String trackingNumber);

    Optional<Shipment> findByShipmentId(@Param("shipmentId") long shipmentId);

    List<Shipment> findByDeliveryAddress(@Param("deliveryAddress") DeliveryArea deliveryArea);

    List<Shipment> findByMerchant_Id(@Param("merchantId") Long merchantId);

    Long countByMerchant_Id(@Param("merchantId") Long merchantId);
}
