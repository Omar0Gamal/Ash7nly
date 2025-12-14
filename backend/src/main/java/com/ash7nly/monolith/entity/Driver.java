package com.ash7nly.monolith.entity;

import com.ash7nly.monolith.entity.base.BaseEntity;
import com.ash7nly.monolith.enums.DeliveryArea;
import com.ash7nly.monolith.enums.VehicleType;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "drivers")
public class Driver extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Enumerated(EnumType.STRING)
    private VehicleType vehicleType;

    private String vehicleNumber;
    private String licenseNumber;

    @Enumerated(EnumType.STRING)
    private DeliveryArea serviceArea;

    @Builder.Default
    private boolean isAvailable = true;

    @OneToMany(mappedBy = "driver", cascade = CascadeType.ALL)
    private List<Delivery> deliveries;
}

