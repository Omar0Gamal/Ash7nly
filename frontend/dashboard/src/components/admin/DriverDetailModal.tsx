import { useGetDriver, useGetAssignedDeliveries } from "@/hooks/admin";
import {
  Dialog,
  DialogContent,
  DialogHeader,
  DialogTitle,
} from "@/components/ui/dialog";
import { Mail, Phone, Truck, MapPin, User } from "lucide-react";
import { Card, CardContent } from "@/components/ui/card";

interface DriverDetailModalProps {
  open: boolean;
  onOpenChange: (open: boolean) => void;
  driverId: number | null;
}

const DriverDetailModal = ({
  open,
  onOpenChange,
  driverId,
}: DriverDetailModalProps) => {
  const { data: driverData, isLoading: driverLoading } = useGetDriver(driverId);
  const { data: deliveriesData, isLoading: deliveriesLoading } =
    useGetAssignedDeliveries(driverId);

  const driver = driverData?.data;
  const deliveries = deliveriesData?.data || [];

  return (
    <Dialog open={open} onOpenChange={onOpenChange}>
      <DialogContent className="max-w-2xl max-h-[90vh] overflow-y-auto">
        <DialogHeader>
          <DialogTitle className="text-2xl">Driver Details</DialogTitle>
        </DialogHeader>

        {driverLoading ? (
          <p className="text-gray-500">Loading driver details...</p>
        ) : !driver ? (
          <p className="text-gray-500">Driver not found</p>
        ) : (
          <div className="space-y-6">
            {/* Personal Info */}
            <Card className="border border-gray-200">
              <CardContent className="pt-6 space-y-4">
                <div className="flex items-center gap-4">
                  <div className="w-16 h-16 bg-[#ef4444] rounded-full flex items-center justify-center text-white text-2xl font-bold">
                    {driver.user.fullName.charAt(0).toUpperCase()}
                  </div>
                  <div>
                    <h3 className="text-xl font-semibold text-gray-900">
                      {driver.user.fullName}
                    </h3>
                    <p className="text-gray-500">
                      {driver.isAvailable ? "🟢 Available" : "🔴 Unavailable"}
                    </p>
                  </div>
                </div>

                <div className="grid grid-cols-2 gap-4 pt-4 border-t border-gray-200">
                  <div className="flex items-center gap-3">
                    <Mail className="w-5 h-5 text-gray-400" />
                    <div>
                      <p className="text-xs text-gray-500">Email</p>
                      <p className="text-sm font-medium text-gray-900">
                        {driver.user.email}
                      </p>
                    </div>
                  </div>
                  <div className="flex items-center gap-3">
                    <Phone className="w-5 h-5 text-gray-400" />
                    <div>
                      <p className="text-xs text-gray-500">Phone</p>
                      <p className="text-sm font-medium text-gray-900">
                        {driver.user.phoneNumber || "N/A"}
                      </p>
                    </div>
                  </div>
                </div>
              </CardContent>
            </Card>

            {/* Vehicle Info */}
            <Card className="border border-gray-200">
              <CardContent className="pt-6 space-y-4">
                <h4 className="font-semibold text-gray-900">
                  Vehicle Information
                </h4>
                <div className="grid grid-cols-2 gap-4">
                  <div className="flex items-center gap-3">
                    <Truck className="w-5 h-5 text-gray-400" />
                    <div>
                      <p className="text-xs text-gray-500">Vehicle Type</p>
                      <p className="text-sm font-medium text-gray-900">
                        {driver.vehicleType}
                      </p>
                    </div>
                  </div>
                  <div className="flex items-center gap-3">
                    <Truck className="w-5 h-5 text-gray-400" />
                    <div>
                      <p className="text-xs text-gray-500">Vehicle Number</p>
                      <p className="text-sm font-medium text-gray-900">
                        {driver.vehicleNumber}
                      </p>
                    </div>
                  </div>
                  <div className="col-span-2 flex items-center gap-3">
                    <MapPin className="w-5 h-5 text-gray-400" />
                    <div>
                      <p className="text-xs text-gray-500">Service Area</p>
                      <p className="text-sm font-medium text-gray-900">
                        {driver.serviceArea}
                      </p>
                    </div>
                  </div>
                  <div className="col-span-2 flex items-center gap-3">
                    <User className="w-5 h-5 text-gray-400" />
                    <div>
                      <p className="text-xs text-gray-500">License Number</p>
                      <p className="text-sm font-medium text-gray-900">
                        {driver.licenseNumber}
                      </p>
                    </div>
                  </div>
                </div>
              </CardContent>
            </Card>

            {/* Assigned Deliveries */}
            <Card className="border border-gray-200">
              <CardContent className="pt-6 space-y-4">
                <h4 className="font-semibold text-gray-900">
                  Assigned Deliveries ({deliveries.length})
                </h4>
                {deliveriesLoading ? (
                  <p className="text-sm text-gray-500">Loading deliveries...</p>
                ) : deliveries.length === 0 ? (
                  <p className="text-sm text-gray-500">
                    No deliveries assigned
                  </p>
                ) : (
                  <div className="space-y-3">
                    {deliveries.map((delivery) => (
                      <div
                        key={delivery.id}
                        className="p-3 bg-gray-50 rounded-lg"
                      >
                        <div className="flex justify-between items-start">
                          <div>
                            <p className="font-medium text-gray-900">
                              {delivery.shipment.trackingNumber}
                            </p>
                            <p className="text-sm text-gray-500 mt-1">
                              {delivery.shipment.customerName}
                            </p>
                          </div>
                          <span className="text-xs px-2 py-1 rounded-full bg-blue-50 text-blue-600 font-medium">
                            {delivery.status}
                          </span>
                        </div>
                      </div>
                    ))}
                  </div>
                )}
              </CardContent>
            </Card>
          </div>
        )}
      </DialogContent>
    </Dialog>
  );
};

export default DriverDetailModal;
