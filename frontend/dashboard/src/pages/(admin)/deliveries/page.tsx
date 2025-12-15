import { useState } from "react";
import AdminLayout from "@/Layouts/AdminLayout";
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card";
import { Input } from "@/components/ui/input";
import { Button } from "@/components/ui/button";
import { useGetAllDrivers } from "@/hooks/admin";
import { Search, Eye, Truck } from "lucide-react";
import DeliveryDetailModal from "../../../components/admin/DeliveryDetailModal";

const AdminDeliveriesPage = () => {
  const { data: driversData, isLoading } = useGetAllDrivers();
  const [searchTerm, setSearchTerm] = useState("");
  const [selectedDeliveryId, setSelectedDeliveryId] = useState<number | null>(
    null
  );
  const [detailModalOpen, setDetailModalOpen] = useState(false);

  const drivers = driversData?.data || [];

  const filteredDrivers = drivers.filter(
    (driver) =>
      driver.user.fullName.toLowerCase().includes(searchTerm.toLowerCase()) ||
      driver.vehicleNumber.toLowerCase().includes(searchTerm.toLowerCase())
  );

  const handleViewDelivery = (deliveryId: number) => {
    setSelectedDeliveryId(deliveryId);
    setDetailModalOpen(true);
  };

  return (
    <AdminLayout>
      <div className="space-y-6 p-8">
        {/* Header */}
        <div>
          <h1 className="text-3xl font-bold text-gray-900">Deliveries</h1>
          <p className="text-gray-500 mt-1">
            Monitor all deliveries and driver assignments
          </p>
        </div>

        {/* Search */}
        <Card className="bg-white rounded-2xl border-0 shadow-sm">
          <CardContent className="pt-6">
            <div className="relative">
              <Search className="absolute left-3 top-3 w-5 h-5 text-gray-400" />
              <Input
                placeholder="Search by driver name or vehicle..."
                value={searchTerm}
                onChange={(e) => setSearchTerm(e.target.value)}
                className="pl-10"
              />
            </div>
          </CardContent>
        </Card>

        {/* Deliveries Table */}
        <Card className="bg-white rounded-2xl border-0 shadow-sm">
          <CardHeader>
            <CardTitle className="text-xl font-semibold text-gray-900">
              Active Deliveries
            </CardTitle>
          </CardHeader>
          <CardContent>
            {isLoading ? (
              <p className="text-gray-500">Loading deliveries...</p>
            ) : filteredDrivers.length === 0 ? (
              <p className="text-gray-500 text-center py-8">
                No deliveries found
              </p>
            ) : (
              <div className="space-y-3">
                {filteredDrivers.map((driver) => (
                  <div
                    key={driver.id}
                    className="flex items-center justify-between p-4 border border-gray-100 rounded-lg hover:border-gray-200 transition-colors"
                  >
                    <div className="flex items-center gap-4 flex-1">
                      <div className="w-10 h-10 bg-[#ef4444] rounded-full flex items-center justify-center text-white font-bold">
                        <Truck className="w-5 h-5" />
                      </div>
                      <div className="flex-1">
                        <p className="font-semibold text-gray-900">
                          {driver.user.fullName}
                        </p>
                        <p className="text-sm text-gray-500">
                          Vehicle: {driver.vehicleNumber} | Area:{" "}
                          {driver.serviceArea}
                        </p>
                      </div>
                    </div>
                    <div className="flex items-center gap-3">
                      <span
                        className={`text-xs px-3 py-1 rounded-full font-medium ${
                          driver.isAvailable
                            ? "text-green-600 bg-green-50"
                            : "text-gray-600 bg-gray-50"
                        }`}
                      >
                        {driver.isAvailable ? "Available" : "In Delivery"}
                      </span>
                      <Button
                        variant="ghost"
                        size="sm"
                        onClick={() => handleViewDelivery(driver.id)}
                        className="text-[#ef4444] hover:bg-red-50"
                      >
                        <Eye className="w-4 h-4" />
                      </Button>
                    </div>
                  </div>
                ))}
              </div>
            )}
          </CardContent>
        </Card>
      </div>

      {/* Delivery Detail Modal */}
      <DeliveryDetailModal
        open={detailModalOpen}
        onOpenChange={setDetailModalOpen}
        deliveryId={selectedDeliveryId}
      />
    </AdminLayout>
  );
};

export default AdminDeliveriesPage;
