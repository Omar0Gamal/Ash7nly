import { useState } from "react";
import AdminLayout from "@/Layouts/AdminLayout";
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card";
import { Input } from "@/components/ui/input";
import { useGetMyShipments } from "@/hooks/shipment";
import { Search, Eye, MapPin } from "lucide-react";
import { Button } from "@/components/ui/button";

const AdminShipmentsPage = () => {
  const { data: shipmentsData, isLoading } = useGetMyShipments();
  const [searchTerm, setSearchTerm] = useState("");

  const shipments = shipmentsData?.data?.shipments || [];
  const filteredShipments = shipments.filter(
    (shipment) =>
      shipment.trackingNumber
        .toLowerCase()
        .includes(searchTerm.toLowerCase()) ||
      shipment.customerName.toLowerCase().includes(searchTerm.toLowerCase()) ||
      shipment.deliveryAddress.toLowerCase().includes(searchTerm.toLowerCase())
  );

  const statusColorMap: Record<string, { badge: string; text: string }> = {
    CREATED: { badge: "text-red-600 bg-red-50", text: "Created" },
    ASSIGNED: { badge: "text-blue-600 bg-blue-50", text: "Assigned" },
    PICKED_UP: { badge: "text-purple-600 bg-purple-50", text: "Picked Up" },
    IN_TRANSIT: { badge: "text-cyan-600 bg-cyan-50", text: "In Transit" },
    DELIVERED: { badge: "text-green-600 bg-green-50", text: "Delivered" },
    FAILED: { badge: "text-red-600 bg-red-50", text: "Failed" },
    CANCELLED: { badge: "text-orange-600 bg-orange-50", text: "Cancelled" },
  };

  return (
    <AdminLayout>
      <div className="space-y-6 p-8">
        {/* Header */}
        <div>
          <h1 className="text-3xl font-bold text-gray-900">Shipments</h1>
          <p className="text-gray-500 mt-1">
            Monitor all shipments in the system
          </p>
        </div>

        {/* Search */}
        <Card className="bg-white rounded-2xl border-0 shadow-sm">
          <CardContent className="pt-6">
            <div className="relative">
              <Search className="absolute left-3 top-3 w-5 h-5 text-gray-400" />
              <Input
                placeholder="Search by tracking number, customer, or address..."
                value={searchTerm}
                onChange={(e) => setSearchTerm(e.target.value)}
                className="pl-10"
              />
            </div>
          </CardContent>
        </Card>

        {/* Shipments Table */}
        <Card className="bg-white rounded-2xl border-0 shadow-sm">
          <CardHeader>
            <CardTitle className="text-xl font-semibold text-gray-900">
              All Shipments
            </CardTitle>
          </CardHeader>
          <CardContent>
            {isLoading ? (
              <p className="text-gray-500">Loading shipments...</p>
            ) : filteredShipments.length === 0 ? (
              <p className="text-gray-500 text-center py-8">
                No shipments found
              </p>
            ) : (
              <div className="overflow-x-auto">
                <table className="w-full">
                  <thead>
                    <tr className="border-b border-gray-200">
                      <th className="text-left py-3 px-4 font-semibold text-gray-900 text-sm">
                        Tracking #
                      </th>
                      <th className="text-left py-3 px-4 font-semibold text-gray-900 text-sm">
                        Customer
                      </th>
                      <th className="text-left py-3 px-4 font-semibold text-gray-900 text-sm">
                        Destination
                      </th>
                      <th className="text-left py-3 px-4 font-semibold text-gray-900 text-sm">
                        Cost
                      </th>
                      <th className="text-left py-3 px-4 font-semibold text-gray-900 text-sm">
                        Status
                      </th>
                      <th className="text-center py-3 px-4 font-semibold text-gray-900 text-sm">
                        Action
                      </th>
                    </tr>
                  </thead>
                  <tbody className="divide-y divide-gray-100">
                    {filteredShipments.map((shipment) => {
                      const colors =
                        statusColorMap[shipment.status] ||
                        statusColorMap.CREATED;
                      return (
                        <tr
                          key={shipment.shipmentId}
                          className="hover:bg-gray-50"
                        >
                          <td className="py-3 px-4">
                            <span className="font-semibold text-gray-900">
                              {shipment.trackingNumber}
                            </span>
                          </td>
                          <td className="py-3 px-4">
                            <div>
                              <p className="font-medium text-gray-900">
                                {shipment.customerName}
                              </p>
                              <p className="text-sm text-gray-500">
                                {shipment.customerEmail}
                              </p>
                            </div>
                          </td>
                          <td className="py-3 px-4">
                            <div className="flex items-start gap-2">
                              <MapPin className="w-4 h-4 text-gray-400 mt-0.5" />
                              <span className="text-sm text-gray-600">
                                {shipment.deliveryAddress}
                              </span>
                            </div>
                          </td>
                          <td className="py-3 px-4">
                            <span className="font-semibold text-gray-900">
                              ${shipment.cost.toFixed(2)}
                            </span>
                          </td>
                          <td className="py-3 px-4">
                            <span
                              className={`text-xs px-3 py-1 rounded-full font-medium ${colors.badge}`}
                            >
                              {colors.text}
                            </span>
                          </td>
                          <td className="py-3 px-4 text-center">
                            <Button
                              variant="ghost"
                              size="sm"
                              className="text-[#ef4444] hover:bg-red-50"
                            >
                              <Eye className="w-4 h-4" />
                            </Button>
                          </td>
                        </tr>
                      );
                    })}
                  </tbody>
                </table>
              </div>
            )}
          </CardContent>
        </Card>
      </div>
    </AdminLayout>
  );
};

export default AdminShipmentsPage;
