package com.ash7nly.monolith.service;

import com.ash7nly.monolith.dto.request.CreateShipmentRequest;
import com.ash7nly.monolith.enums.DeliveryArea;
import com.ash7nly.monolith.exception.BadRequestException;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class PricingService {

    private static final double MAX_WEIGHT_KG = 60.0;
    private static final Map<DeliveryArea, Double> AREA_COST_MAP = Map.of(
            DeliveryArea.HELWAN, 30.0,
            DeliveryArea.FISAL, 40.0,
            DeliveryArea.HARAM, 50.0,
            DeliveryArea.MAADI, 60.0,
            DeliveryArea.DOKKI, 70.0,
            DeliveryArea.ZAMALEK, 80.0,
            DeliveryArea.IMBABA, 120.0,
            DeliveryArea.ROD_ELFARAG, 90.0,
            DeliveryArea.NASR_CITY, 100.0
    );

    public double calculateShippingCost(CreateShipmentRequest request) {
        double weight = parseWeight(request.getPackageWeight());
        validateWeight(weight);
        double areaCost = getAreaCost(request.getDeliveryAddress());
        return request.getCost() + areaCost;
    }

    public double calculateShippingCost(String packageWeight, DeliveryArea deliveryArea, double baseCost) {
        double weight = parseWeight(packageWeight);
        validateWeight(weight);
        double areaCost = getAreaCost(deliveryArea);
        return baseCost + areaCost;
    }

    private double parseWeight(String packageWeight) {
        return Double.parseDouble(packageWeight.replace("kg", "").trim());
    }

    private void validateWeight(double weight) {
        if (weight > MAX_WEIGHT_KG) {
            throw new BadRequestException("Weight exceeds maximum allowed: " + MAX_WEIGHT_KG + "kg");
        }
    }

    private double getAreaCost(DeliveryArea area) {
        Double areaCost = AREA_COST_MAP.get(area);
        if (areaCost == null) {
            throw new BadRequestException("Unsupported delivery area: " + area);
        }
        return areaCost;
    }
}

