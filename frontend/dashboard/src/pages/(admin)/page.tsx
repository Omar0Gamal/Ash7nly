import AdminLayout from "@/Layouts/AdminLayout";
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card";
import { useGetAllDrivers } from "@/hooks/admin";
import { BarChart3, Users, Package, Truck } from "lucide-react";

const AdminDashboard = () => {
  const { data: driversData, isLoading: driversLoading } = useGetAllDrivers();

  const drivers = driversData?.data || [];
  const activeDrivers = drivers.filter((d) => d.isAvailable).length;

  const statsData = [
    {
      title: "Total Drivers",
      value: drivers.length.toString(),
      icon: Truck,
      color: "text-blue-600",
      bg: "bg-blue-50",
    },
    {
      title: "Active Drivers",
      value: activeDrivers.toString(),
      icon: Users,
      color: "text-green-600",
      bg: "bg-green-50",
    },
    {
      title: "Vehicles",
      value: drivers.length.toString(),
      icon: Package,
      color: "text-purple-600",
      bg: "bg-purple-50",
    },
    {
      title: "Analytics",
      value: "View",
      icon: BarChart3,
      color: "text-orange-600",
      bg: "bg-orange-50",
    },
  ];

  return (
    <AdminLayout>
      <div className="space-y-6 p-8">
        {/* Header */}
        <div>
          <h1 className="text-3xl font-bold text-gray-900">Admin Dashboard</h1>
          <p className="text-gray-500 mt-1">
            Welcome to the admin control panel
          </p>
        </div>

        {/* Stats Cards */}
        <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-6">
          {statsData.map((stat) => (
            <Card
              key={stat.title}
              className="bg-white rounded-2xl border-0 shadow-sm"
            >
              <CardHeader className="flex flex-row items-center justify-between pb-2">
                <CardTitle className="text-sm font-medium text-gray-500">
                  {stat.title}
                </CardTitle>
                <stat.icon className={`w-5 h-5 ${stat.color}`} />
              </CardHeader>
              <CardContent>
                <div className="text-2xl font-bold text-gray-900">
                  {stat.value}
                </div>
                <p className={`text-xs mt-1 ${stat.color}`}>{stat.title}</p>
              </CardContent>
            </Card>
          ))}
        </div>

        {/* Drivers Overview */}
        <Card className="bg-white rounded-2xl border-0 shadow-sm">
          <CardHeader>
            <CardTitle className="text-xl font-semibold text-gray-900">
              Drivers Overview
            </CardTitle>
          </CardHeader>
          <CardContent>
            {driversLoading ? (
              <p className="text-gray-500">Loading drivers...</p>
            ) : drivers.length === 0 ? (
              <p className="text-gray-500">No drivers found</p>
            ) : (
              <div className="space-y-4">
                {drivers.slice(0, 5).map((driver) => (
                  <div
                    key={driver.id}
                    className="flex items-center justify-between p-4 border border-gray-100 rounded-lg hover:border-gray-200 transition-colors"
                  >
                    <div className="flex items-center gap-4">
                      <div className="w-10 h-10 bg-blue-100 rounded-full flex items-center justify-center">
                        <Truck className="w-5 h-5 text-blue-600" />
                      </div>
                      <div>
                        <p className="font-semibold text-gray-900">
                          {driver.user.fullName}
                        </p>
                        <p className="text-sm text-gray-500">
                          {driver.vehicleNumber}
                        </p>
                      </div>
                    </div>
                    <div className="text-right">
                      <span
                        className={`text-xs px-3 py-1 rounded-full font-medium ${
                          driver.isAvailable
                            ? "text-green-600 bg-green-50"
                            : "text-gray-600 bg-gray-50"
                        }`}
                      >
                        {driver.isAvailable ? "Available" : "Unavailable"}
                      </span>
                    </div>
                  </div>
                ))}
              </div>
            )}
          </CardContent>
        </Card>
      </div>
    </AdminLayout>
  );
};

export default AdminDashboard;
