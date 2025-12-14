package com.ash7nly.monolith.dto.response;

import com.ash7nly.monolith.enums.ShipmentStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TrackingHistoryResponse {

    private ShipmentStatus status;
    private LocalDateTime timestamp;
}

