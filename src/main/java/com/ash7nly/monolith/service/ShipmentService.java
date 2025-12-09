package com.ash7nly.monolith.service;

import com.ash7nly.monolith.dto.request.CancelShipmentRequest;
import com.ash7nly.monolith.dto.request.CreateShipmentRequest;
import com.ash7nly.monolith.dto.response.CancelShipmentResponse;
import com.ash7nly.monolith.dto.response.ShipmentResponse;
import com.ash7nly.monolith.entity.Shipment;
import com.ash7nly.monolith.entity.User;
import com.ash7nly.monolith.enums.ShipmentStatus;
import com.ash7nly.monolith.exception.BadRequestException;
import com.ash7nly.monolith.exception.ForbiddenException;
import com.ash7nly.monolith.exception.NotFoundException;
import com.ash7nly.monolith.mapper.ShipmentMapper;
import com.ash7nly.monolith.repository.ShipmentRepository;
import com.ash7nly.monolith.repository.UserRepository;
import com.ash7nly.monolith.security.CurrentUserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ShipmentService {

    private final ShipmentRepository shipmentRepository;
    private final UserRepository userRepository;
    private final ShipmentMapper shipmentMapper;
    private final CurrentUserService currentUserService;

    public ShipmentService(ShipmentRepository shipmentRepository, UserRepository userRepository,
                           ShipmentMapper shipmentMapper, CurrentUserService currentUserService) {
        this.shipmentRepository = shipmentRepository;
        this.userRepository = userRepository;
        this.shipmentMapper = shipmentMapper;
        this.currentUserService = currentUserService;
    }

    @Transactional
    public ShipmentResponse createShipment(CreateShipmentRequest request) {
        if (!currentUserService.isMerchant()) {
            throw new ForbiddenException("Only merchants can create shipments");
        }

        Long merchantId = currentUserService.getCurrentUserId();
        User merchant = userRepository.findById(merchantId)
                .orElseThrow(() -> new NotFoundException("Merchant not found"));

        Shipment shipment = shipmentMapper.toEntity(request, merchant);
        shipment = shipmentRepository.save(shipment);

        return shipmentMapper.toResponse(shipment);
    }

    public ShipmentResponse trackShipment(String trackingNumber) {
        Shipment shipment = shipmentRepository.findByTrackingNumber(trackingNumber)
                .orElseThrow(() -> new NotFoundException("Shipment not found with tracking number: " + trackingNumber));

        return shipmentMapper.toResponse(shipment);
    }

    @Transactional
    public CancelShipmentResponse cancelShipment(CancelShipmentRequest request) {
        Shipment shipment = shipmentRepository.findByTrackingNumber(request.getTrackingNumber())
                .orElseThrow(() -> new NotFoundException("Shipment not found"));

        // Check ownership if not admin
        if (!currentUserService.isAdmin()) {
            Long currentUserId = currentUserService.getCurrentUserId();
            if (shipment.getMerchant() == null || !shipment.getMerchant().getId().equals(currentUserId)) {
                throw new ForbiddenException("You can only cancel your own shipments");
            }
        }

        // Check that shipment is eligible for cancellation
        if (shipment.getStatus() != ShipmentStatus.ASSIGNED &&
                shipment.getStatus() != ShipmentStatus.CREATED) {
            throw new BadRequestException("Shipment cannot be cancelled after pickup");
        }

        shipment.setStatus(ShipmentStatus.CANCELLED);
        shipment.setCancellationReason(request.getCancellationReason());
        shipment.setActive(false);

        shipmentRepository.save(shipment);

        return shipmentMapper.toCancelResponse(shipment);
    }

    public List<ShipmentResponse> getMyShipments() {
        Long merchantId = currentUserService.getCurrentUserId();
        List<Shipment> shipments = shipmentRepository.findByMerchantId(merchantId);
        return shipments.stream()
                .map(shipmentMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public ShipmentResponse updateShipmentStatus(Long shipmentId, ShipmentStatus status) {
        Shipment shipment = shipmentRepository.findById(shipmentId)
                .orElseThrow(() -> new NotFoundException("Shipment not found"));

        shipment.setStatus(status);
        shipment = shipmentRepository.save(shipment);

        return shipmentMapper.toResponse(shipment);
    }
}

