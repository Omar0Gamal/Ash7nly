package com.ash7nly.delivery;

import com.ash7nly.common.response.ApiResponse;
import com.ash7nly.common.security.UserContext;
import com.ash7nly.delivery.Entity.Delivery;
import com.ash7nly.delivery.Entity.Driver;
import com.ash7nly.delivery.Services.DeliveryService;

import com.ash7nly.delivery.dto.AssignedShipmentDTO;
import com.ash7nly.delivery.dto.CreateDeliveryRequest;
import com.ash7nly.delivery.dto.DeliveryResponse;
import com.ash7nly.delivery.dto.FailDeliveryRequest;
import com.ash7nly.delivery.mapper.DeliveryMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/deliveries")
public class DeliveryController {

    private final DeliveryService deliveryService;
    public DeliveryController(DeliveryService deliveryService) {
        this.deliveryService = deliveryService;
    }


    // Called by shipment-service to create a delivery record when a shipment is created
    @PostMapping("/create")
    public ResponseEntity<ApiResponse<DeliveryResponse>> createFromShipment(@RequestBody CreateDeliveryRequest request) {
        DeliveryResponse resp = deliveryService.createDeliveryFromShipment(request);
        return ResponseEntity.ok(ApiResponse.success(resp));
    }

    // Driver requests available shipments (by service area)
    @GetMapping("/available")
    public ResponseEntity<ApiResponse<List<AssignedShipmentDTO>>> getAvailableForDriver() {
        Long userId = Long.valueOf(UserContext.getUserId());
        List<AssignedShipmentDTO> shipments = deliveryService.getAvailableShipmentsForDriver(userId);
        return ResponseEntity.ok(ApiResponse.success(shipments));
    }

    // Driver accepts a shipment (assigns the delivery to himself)
    @PostMapping("/{shipmentId}/accept")
    public ResponseEntity<ApiResponse<DeliveryResponse>> acceptShipment(@PathVariable Long shipmentId) {
        Long userId = Long.valueOf(UserContext.getUserId());
        DeliveryResponse resp = deliveryService.acceptShipment(shipmentId, userId);
        return ResponseEntity.ok(ApiResponse.success(resp));
    }

    // Get deliveries assigned to logged-in driver
    @GetMapping("/assigned")
    public ResponseEntity<ApiResponse<List<DeliveryResponse>>> getAssigned() {
        Long userId = Long.valueOf(UserContext.getUserId());
        List<DeliveryResponse> resp = deliveryService.getAssignedDeliveriesForDriver(userId);
        return ResponseEntity.ok(ApiResponse.success(resp));
    }

    // Update status (delegated to shipment service)
    @PutMapping("/{id}/status")
    public ResponseEntity<ApiResponse<DeliveryResponse>> updateStatus(@PathVariable Long id, @RequestParam String status) {
        DeliveryResponse resp = deliveryService.updateDeliveryStatus(id, status);
        return ResponseEntity.ok(ApiResponse.success(resp));
    }

    // Report failed
    @PostMapping("/{id}/failed")
    public ResponseEntity<ApiResponse<DeliveryResponse>> reportFailed(@PathVariable Long id, @RequestBody FailDeliveryRequest request) {
        DeliveryResponse resp = deliveryService.reportFailed(id, request.getReason());
        return ResponseEntity.ok(ApiResponse.success(resp));
    }
}

