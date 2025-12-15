import { admin_API } from "@/api/admin";
import { useQuery } from "@tanstack/react-query";

// User Hooks
export const useGetUser = (id: number | null) => {
  return useQuery({
    queryKey: ["admin-user", id],
    queryFn: () => admin_API.getUser(id!),
    enabled: id !== null,
  });
};

// Driver Hooks
export const useGetAllDrivers = () => {
  return useQuery({
    queryKey: ["admin-drivers"],
    queryFn: () => admin_API.getAllDrivers(),
  });
};

export const useGetDriver = (id: number | null) => {
  return useQuery({
    queryKey: ["admin-driver", id],
    queryFn: () => admin_API.getDriver(id!),
    enabled: id !== null,
  });
};

export const useGetAssignedDeliveries = (driverId: number | null) => {
  return useQuery({
    queryKey: ["admin-assigned-deliveries", driverId],
    queryFn: () => admin_API.getAssignedDeliveries(driverId!),
    enabled: driverId !== null,
  });
};

// Delivery Hooks
export const useGetDelivery = (id: number | null) => {
  return useQuery({
    queryKey: ["admin-delivery", id],
    queryFn: () => admin_API.getDelivery(id!),
    enabled: id !== null,
  });
};

// Shipment Hooks
export const useGetShipment = (id: number | null) => {
  return useQuery({
    queryKey: ["admin-shipment", id],
    queryFn: () => admin_API.getShipment(id!),
    enabled: id !== null,
  });
};
