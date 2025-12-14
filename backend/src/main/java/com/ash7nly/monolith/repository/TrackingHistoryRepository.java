package com.ash7nly.monolith.repository;

import com.ash7nly.monolith.entity.Shipment;
import com.ash7nly.monolith.entity.TrackingHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TrackingHistoryRepository extends JpaRepository<TrackingHistory, Long> {
    List<TrackingHistory> findByShipmentOrderByTimestampAsc(@Param("shipment") Shipment shipment);
}