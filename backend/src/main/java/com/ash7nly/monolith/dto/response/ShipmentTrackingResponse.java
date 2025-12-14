package com.ash7nly.monolith.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ShipmentTrackingResponse {

    private List<TrackingHistoryResponse> trackingHistoryList;
    private String driverPhone;
    private String driverName;
    private String driverEmail;
}

