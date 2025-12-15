import { useState } from "react";
import AdminLayout from "@/Layouts/AdminLayout";
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card";
import { Button } from "@/components/ui/button";
import { Input } from "@/components/ui/input";
import { useGetAllDrivers } from "@/hooks/admin";
import { Search, Eye, MapPin, Phone, Mail } from "lucide-react";
import DriverDetailModal from "@/components/admin/DriverDetailModal";

const AdminDriversPage = () => {
  const { data: driversData, isLoading } = useGetAllDrivers();
  const [searchTerm, setSearchTerm] = useState("");
  const [selectedDriverId, setSelectedDriverId] = useState<number | null>(null);
  const [detailModalOpen, setDetailModalOpen] = useState(false);

  const drivers = driversData?.data || [];
  const filteredDrivers = drivers.filter(
    (driver) =>
      driver.user.fullName.toLowerCase().includes(searchTerm.toLowerCase()) ||
      driver.vehicleNumber.toLowerCase().includes(searchTerm.toLowerCase()) ||
      driver.user.email.toLowerCase().includes(searchTerm.toLowerCase())
  );

  const handleViewDriver = (driverId: number) => {
    setSelectedDriverId(driverId);
    setDetailModalOpen(true);
  };

  return (
    <AdminLayout>
      <div className="space-y-6 p-8">
        {/* Header */}
        <div>
          <h1 className="text-3xl font-bold text-gray-900">Drivers</h1>
          <p className="text-gray-500 mt-1">
            Manage and monitor all registered drivers
          </p>
        </div>

        {/* Search */}
        <Card className="bg-white rounded-2xl border-0 shadow-sm">
          <CardContent className="pt-6">
            <div className="relative">
              <Search className="absolute left-3 top-3 w-5 h-5 text-gray-400" />
              <Input
                placeholder="Search by name, email, or vehicle number..."
                value={searchTerm}
                onChange={(e) => setSearchTerm(e.target.value)}
                className="pl-10"
              />
            </div>
          </CardContent>
        </Card>

        {/* Drivers Table */}
        <Card className="bg-white rounded-2xl border-0 shadow-sm">
          <CardHeader>
            <CardTitle className="text-xl font-semibold text-gray-900">
              All Drivers
            </CardTitle>
          </CardHeader>
          <CardContent>
            {isLoading ? (
              <p className="text-gray-500">Loading drivers...</p>
            ) : filteredDrivers.length === 0 ? (
              <p className="text-gray-500 text-center py-8">No drivers found</p>
            ) : (
              <div className="space-y-3">
                {filteredDrivers.map((driver) => (
                  <div
                    key={driver.id}
                    className="flex items-center justify-between p-4 border border-gray-100 rounded-lg hover:border-gray-200 transition-colors"
                  >
                    <div className="flex-1">
                      <div className="flex items-center gap-4">
                        <div className="w-12 h-12 bg-blue-100 rounded-full flex items-center justify-center">
                          <span className="text-sm font-bold text-blue-600">
                            {driver.user.fullName.charAt(0).toUpperCase()}
                          </span>
                        </div>
                        <div className="flex-1">
                          <p className="font-semibold text-gray-900">
                            {driver.user.fullName}
                          </p>
                          <div className="flex flex-wrap gap-4 text-sm text-gray-500 mt-1">
                            <div className="flex items-center gap-1">
                              <Mail className="w-4 h-4" />
                              {driver.user.email}
                            </div>
                            <div className="flex items-center gap-1">
                              <Phone className="w-4 h-4" />
                              {driver.user.phoneNumber || "N/A"}
                            </div>
                            <div className="flex items-center gap-1">
                              <MapPin className="w-4 h-4" />
                              {driver.serviceArea}
                            </div>
                          </div>
                        </div>
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
                        {driver.isAvailable ? "Available" : "Unavailable"}
                      </span>
                      <Button
                        variant="ghost"
                        size="sm"
                        onClick={() => handleViewDriver(driver.id)}
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

      {/* Driver Detail Modal */}
      <DriverDetailModal
        open={detailModalOpen}
        onOpenChange={setDetailModalOpen}
        driverId={selectedDriverId}
      />
    </AdminLayout>
  );
};

export default AdminDriversPage;
