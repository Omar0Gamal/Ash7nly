package com.ash7nly.shipment.Entity;

import com.ash7nly.common.enums.ShipmentStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name ="Shipment")
@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ShipmentEntity {

}
