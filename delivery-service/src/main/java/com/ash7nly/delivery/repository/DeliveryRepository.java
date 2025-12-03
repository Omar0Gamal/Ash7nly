package com.ash7nly.delivery.repository;

import com.ash7nly.delivery.Entity.Delivery;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface DeliveryRepository extends JpaRepository<Delivery,Long> {
    List<Delivery> findByDriverId(Long driverId);
    List<Delivery> findByDriverIdAndDeliveredAtIsNull(Long driverId);
}

