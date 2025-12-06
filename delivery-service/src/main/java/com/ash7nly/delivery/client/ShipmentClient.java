package com.ash7nly.delivery.client;


import com.ash7nly.common.enums.DeliveryArea;
import com.ash7nly.common.response.ApiResponse;
import com.ash7nly.delivery.dto.ShipmentListDTO;
import com.ash7nly.delivery.dto.UpdateShipmentDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient(value = "shipment-service", url = "http://localhost:8082")
public interface ShipmentClient {
    @GetMapping("/api/shipments/area/{serviceArea}")
    ResponseEntity<ApiResponse<List<ShipmentListDTO>>> getByServiceArea(@PathVariable("serviceArea") DeliveryArea serviceArea);

    @PutMapping("/api/shipments/status")
    ResponseEntity<ApiResponse<UpdateShipmentDTO>> updateStatus(@RequestBody UpdateShipmentDTO request);

    @GetMapping("/api/shipments/{shipmentId}")
    ResponseEntity<ApiResponse<ShipmentListDTO>> getShipmentById(@PathVariable("shipmentId") Long shipmentId);}
