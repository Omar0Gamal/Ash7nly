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
public class MerchantShipmentsResponse {

    private List<ShipmentListResponse> shipments;
    private long totalCount;
}

