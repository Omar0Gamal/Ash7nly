package com.ash7nly.monolith.entity;

import com.ash7nly.monolith.entity.base.BaseEntity;
import com.ash7nly.monolith.enums.DeliveryArea;
import com.ash7nly.monolith.enums.ShipmentStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "shipment")
public class Shipment extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long shipmentId;

    @OneToOne(mappedBy = "shipment", fetch = FetchType.LAZY)
    private Delivery delivery;

    @Column(unique = true, nullable = false)
    private String trackingNumber;

    @Column(nullable = false)
    private String pickupAddress;

    @Enumerated(EnumType.STRING)
    private DeliveryArea deliveryAddress;

    private String customerName;
    private String customerPhone;
    private String packageWeight;
    private String packageDimension;

    @Email
    private String customerEmail;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "merchant_id", nullable = false)
    private User merchant;

    @Column(length = 1000)
    private String packageDescription;

    @Enumerated(EnumType.STRING)
    private ShipmentStatus status;

    @Column(nullable = false)
    private double cost;

    @Builder.Default
    private boolean isActive = true;

    private String cancellationReason;

    @Builder.Default
    @OneToMany(mappedBy = "shipment", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TrackingHistory> trackingHistory = new ArrayList<>();

    public boolean isCancellable() {
        return this.status == ShipmentStatus.CREATED || this.status == ShipmentStatus.ASSIGNED;
    }
}

