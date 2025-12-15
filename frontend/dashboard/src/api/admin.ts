import { axiosClientWithAuth } from "@/axios/client";

// Admin User Management
export interface AdminUser {
  id: number;
  email: string;
  fullName: string;
  phoneNumber: string;
  role: string;
  isActive: boolean;
}

export interface AdminUsersResponse {
  success: boolean;
  message: string;
  data: AdminUser[];
  timestamp: string;
}

export interface AdminUserDetailResponse {
  success: boolean;
  message: string;
  data: AdminUser;
  timestamp: string;
}

export interface UpdateUserRequest {
  fullName?: string;
  phoneNumber?: string;
  email?: string;
}

// Admin Driver Management
export interface AdminDriver {
  id: number;
  vehicleType: string;
  vehicleNumber: string;
  licenseNumber: string;
  serviceArea: string;
  isAvailable: boolean;
  userId: number;
  user: AdminUser;
}

export interface AdminDriversResponse {
  success: boolean;
  message: string;
  data: AdminDriver[];
  timestamp: string;
}

export interface AdminDriverDetailResponse {
  success: boolean;
  message: string;
  data: AdminDriver;
  timestamp: string;
}

export interface UpdateDriverRequest {
  vehicleType?: string;
  vehicleNumber?: string;
  licenseNumber?: string;
  serviceArea?: string;
}

// Admin Shipment Management
export interface AdminShipment {
  shipmentId: number;
  trackingNumber: string;
  pickupAddress: string;
  deliveryAddress: string;
  customerName: string;
  customerEmail: string;
  customerPhone: string;
  packageWeight: string;
  packageDimension: string;
  packageDescription: string;
  status: string;
  cost: number;
  merchantId: number;
  active: boolean;
  createdAt: string;
}

export interface AdminShipmentsResponse {
  success: boolean;
  message: string;
  data: AdminShipment[];
  timestamp: string;
}

export interface AdminShipmentDetailResponse {
  success: boolean;
  message: string;
  data: AdminShipment;
  timestamp: string;
}

// Admin Delivery Management
export interface AdminDelivery {
  id: number;
  shipmentId: number;
  driverId: number;
  status: string;
  assignedAt: string;
  acceptedAt: string;
  pickedUpAt: string;
  deliveredAt: string;
  recipientName: string;
  deliveryNotes: string;
  shipment: AdminShipment;
  driver: AdminDriver;
}

export interface AdminDeliveriesResponse {
  success: boolean;
  message: string;
  data: AdminDelivery[];
  timestamp: string;
}

export interface AdminDeliveryDetailResponse {
  success: boolean;
  message: string;
  data: AdminDelivery;
  timestamp: string;
}

// Admin APIs
export const admin_API = {
  // User Management
  getUser: async (id: number): Promise<AdminUserDetailResponse> => {
    const response = await axiosClientWithAuth.get<AdminUserDetailResponse>(
      `/api/users/${id}`
    );
    return response.data;
  },

  updateUser: async (
    id: number,
    payload: UpdateUserRequest
  ): Promise<AdminUserDetailResponse> => {
    const response = await axiosClientWithAuth.put<AdminUserDetailResponse>(
      `/api/users/${id}`,
      payload
    );
    return response.data;
  },

  // Driver Management
  getAllDrivers: async (): Promise<AdminDriversResponse> => {
    const response = await axiosClientWithAuth.get<AdminDriversResponse>(
      `/api/drivers`
    );
    return response.data;
  },

  getDriver: async (id: number): Promise<AdminDriverDetailResponse> => {
    const response = await axiosClientWithAuth.get<AdminDriverDetailResponse>(
      `/api/drivers/${id}`
    );
    return response.data;
  },

  updateDriver: async (
    id: number,
    payload: UpdateDriverRequest
  ): Promise<AdminDriverDetailResponse> => {
    const response = await axiosClientWithAuth.put<AdminDriverDetailResponse>(
      `/api/drivers/${id}`,
      payload
    );
    return response.data;
  },

  getAssignedDeliveries: async (
    driverId: number
  ): Promise<AdminDeliveriesResponse> => {
    const response = await axiosClientWithAuth.get<AdminDeliveriesResponse>(
      `/api/deliveries/assigned`,
      { params: { driverId } }
    );
    return response.data;
  },

  // Delivery Management
  getDelivery: async (id: number): Promise<AdminDeliveryDetailResponse> => {
    const response = await axiosClientWithAuth.get<AdminDeliveryDetailResponse>(
      `/api/deliveries/${id}`
    );
    return response.data;
  },

  // Shipment Management
  getShipment: async (id: number): Promise<AdminShipmentDetailResponse> => {
    const response = await axiosClientWithAuth.get<AdminShipmentDetailResponse>(
      `/api/shipments/${id}`
    );
    return response.data;
  },
};
