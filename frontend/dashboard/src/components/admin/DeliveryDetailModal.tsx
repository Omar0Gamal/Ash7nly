import { useGetDelivery } from "@/hooks/admin";
import {
  Dialog,
  DialogContent,
  DialogHeader,
  DialogTitle,
} from "@/components/ui/dialog";
import { MapPin, Package, User, Calendar } from "lucide-react";
import { Card, CardContent } from "@/components/ui/card";
import { format } from "date-fns";

interface DeliveryDetailModalProps {
  open: boolean;
  onOpenChange: (open: boolean) => void;
  deliveryId: number | null;
}

const DeliveryDetailModal = ({
  open,
  onOpenChange,
  deliveryId,
}: DeliveryDetailModalProps) => {
  const { data: deliveryData, isLoading } = useGetDelivery(deliveryId);
  const delivery = deliveryData?.data;

  const statusColorMap: Record<string, string> = {
    CREATED: "text-red-600 bg-red-50",
    ASSIGNED: "text-blue-600 bg-blue-50",
    PICKED_UP: "text-purple-600 bg-purple-50",
    IN_TRANSIT: "text-cyan-600 bg-cyan-50",
    DELIVERED: "text-green-600 bg-green-50",
    FAILED: "text-red-600 bg-red-50",
    CANCELLED: "text-orange-600 bg-orange-50",
  };

  return (
    <Dialog open={open} onOpenChange={onOpenChange}>
      <DialogContent className="max-w-2xl max-h-[90vh] overflow-y-auto">
        <DialogHeader>
          <DialogTitle className="text-2xl">Delivery Details</DialogTitle>
        </DialogHeader>

        {isLoading ? (
          <p className="text-gray-500">Loading delivery details...</p>
        ) : !delivery ? (
          <p className="text-gray-500">Delivery not found</p>
        ) : (
          <div className="space-y-6">
            {/* Status */}
            <Card className="border border-gray-200">
              <CardContent className="pt-6">
                <div className="flex justify-between items-center">
                  <h3 className="font-semibold text-gray-900">
                    Shipment: {delivery.shipment.trackingNumber}
                  </h3>
                  <span
                    className={`text-xs px-3 py-1 rounded-full font-medium ${
                      statusColorMap[delivery.status]
                    }`}
                  >
                    {delivery.status}
                  </span>
                </div>
              </CardContent>
            </Card>

            {/* Shipment Info */}
            <Card className="border border-gray-200">
              <CardContent className="pt-6 space-y-4">
                <h4 className="font-semibold text-gray-900">
                  Shipment Information
                </h4>
                <div className="grid grid-cols-1 gap-4">
                  <div className="flex items-start gap-3">
                    <User className="w-5 h-5 text-gray-400 mt-0.5" />
                    <div className="flex-1">
                      <p className="text-xs text-gray-500">Customer</p>
                      <p className="text-sm font-medium text-gray-900">
                        {delivery.shipment.customerName}
                      </p>
                      <p className="text-xs text-gray-500 mt-1">
                        {delivery.shipment.customerEmail}
                      </p>
                    </div>
                  </div>
                  <div className="flex items-start gap-3">
                    <Package className="w-5 h-5 text-gray-400 mt-0.5" />
                    <div className="flex-1">
                      <p className="text-xs text-gray-500">Package</p>
                      <p className="text-sm font-medium text-gray-900">
                        {delivery.shipment.packageDescription}
                      </p>
                      <p className="text-xs text-gray-500 mt-1">
                        Weight: {delivery.shipment.packageWeight} | Dimensions:{" "}
                        {delivery.shipment.packageDimension}
                      </p>
                    </div>
                  </div>
                  <div className="flex items-start gap-3">
                    <MapPin className="w-5 h-5 text-gray-400 mt-0.5" />
                    <div className="flex-1">
                      <p className="text-xs text-gray-500">Pickup Address</p>
                      <p className="text-sm font-medium text-gray-900">
                        {delivery.shipment.pickupAddress}
                      </p>
                    </div>
                  </div>
                  <div className="flex items-start gap-3">
                    <MapPin className="w-5 h-5 text-[#ef4444] mt-0.5" />
                    <div className="flex-1">
                      <p className="text-xs text-gray-500">Delivery Address</p>
                      <p className="text-sm font-medium text-gray-900">
                        {delivery.shipment.deliveryAddress}
                      </p>
                    </div>
                  </div>
                  <div className="flex items-center gap-3">
                    <div className="w-5 h-5 text-gray-400 flex items-center justify-center">
                      $
                    </div>
                    <div className="flex-1">
                      <p className="text-xs text-gray-500">Cost</p>
                      <p className="text-sm font-medium text-gray-900">
                        ${delivery.shipment.cost.toFixed(2)}
                      </p>
                    </div>
                  </div>
                </div>
              </CardContent>
            </Card>

            {/* Driver Info */}
            <Card className="border border-gray-200">
              <CardContent className="pt-6 space-y-4">
                <h4 className="font-semibold text-gray-900">
                  Driver Information
                </h4>
                <div className="flex items-center gap-4">
                  <div className="w-12 h-12 bg-[#ef4444] rounded-full flex items-center justify-center text-white font-bold">
                    {delivery.driver.user.fullName.charAt(0).toUpperCase()}
                  </div>
                  <div>
                    <p className="font-medium text-gray-900">
                      {delivery.driver.user.fullName}
                    </p>
                    <p className="text-sm text-gray-500">
                      {delivery.driver.vehicleNumber}
                    </p>
                  </div>
                </div>
              </CardContent>
            </Card>

            {/* Timeline */}
            <Card className="border border-gray-200">
              <CardContent className="pt-6 space-y-4">
                <h4 className="font-semibold text-gray-900">Timeline</h4>
                <div className="space-y-3">
                  {delivery.assignedAt && (
                    <div className="flex items-center gap-3">
                      <Calendar className="w-5 h-5 text-gray-400" />
                      <div>
                        <p className="text-xs text-gray-500">Assigned At</p>
                        <p className="text-sm font-medium text-gray-900">
                          {format(new Date(delivery.assignedAt), "PPpp")}
                        </p>
                      </div>
                    </div>
                  )}
                  {delivery.acceptedAt && (
                    <div className="flex items-center gap-3">
                      <Calendar className="w-5 h-5 text-blue-400" />
                      <div>
                        <p className="text-xs text-gray-500">Accepted At</p>
                        <p className="text-sm font-medium text-gray-900">
                          {format(new Date(delivery.acceptedAt), "PPpp")}
                        </p>
                      </div>
                    </div>
                  )}
                  {delivery.pickedUpAt && (
                    <div className="flex items-center gap-3">
                      <Calendar className="w-5 h-5 text-purple-400" />
                      <div>
                        <p className="text-xs text-gray-500">Picked Up At</p>
                        <p className="text-sm font-medium text-gray-900">
                          {format(new Date(delivery.pickedUpAt), "PPpp")}
                        </p>
                      </div>
                    </div>
                  )}
                  {delivery.deliveredAt && (
                    <div className="flex items-center gap-3">
                      <Calendar className="w-5 h-5 text-green-400" />
                      <div>
                        <p className="text-xs text-gray-500">Delivered At</p>
                        <p className="text-sm font-medium text-gray-900">
                          {format(new Date(delivery.deliveredAt), "PPpp")}
                        </p>
                      </div>
                    </div>
                  )}
                </div>
              </CardContent>
            </Card>

            {/* Notes */}
            {delivery.deliveryNotes && (
              <Card className="border border-gray-200">
                <CardContent className="pt-6">
                  <h4 className="font-semibold text-gray-900 mb-2">
                    Delivery Notes
                  </h4>
                  <p className="text-sm text-gray-600 p-3 bg-gray-50 rounded">
                    {delivery.deliveryNotes}
                  </p>
                </CardContent>
              </Card>
            )}
          </div>
        )}
      </DialogContent>
    </Dialog>
  );
};

export default DeliveryDetailModal;
