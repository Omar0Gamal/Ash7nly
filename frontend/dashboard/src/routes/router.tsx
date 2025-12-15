import LoginPage from "@/pages/(auth)/LoginPage";
import RegisterPage from "@/pages/(auth)/RegisterPage";
import MerchantDashboard from "@/pages/(merchant)/page";
import TrackingPage from "@/pages/(merchant)/tracking/[trackingNumber]/page";
import TrackingSearchPage from "@/pages/(merchant)/tracking/page";
import ShipmentsPage from "@/pages/(merchant)/shipment/page";
import CreateShipmentPage from "@/pages/(merchant)/create-shipment/page";
import PaymentPage from "@/pages/(merchant)/payment/[shipmentId]/page";
import AdminDashboard from "@/pages/(admin)/page";
import AdminDriversPage from "@/pages/(admin)/drivers/page";
import AdminShipmentsPage from "@/pages/(admin)/shipments/page";
import AdminDeliveriesPage from "@/pages/(admin)/deliveries/page";
import { createBrowserRouter, Navigate } from "react-router";
import AuthRouter from "./AuthRouter";
import { useAuthStore } from "@/stores/auth";

// Component to redirect based on user role
const RoleBasedRedirect = () => {
  const user = useAuthStore((state) => state.user);

  if (user?.role === "ADMIN") {
    return <Navigate to="/admin" replace />;
  }
  return <Navigate to="/merchant" replace />;
};

export const router = createBrowserRouter([
  {
    path: "/",
    element: <AuthRouter />,
    children: [
      { path: "", element: <RoleBasedRedirect /> },
      { path: "dashboard", element: <MerchantDashboard /> },
      { path: "merchant", element: <MerchantDashboard /> },
      { path: "merchant/shipments", element: <ShipmentsPage /> },
      { path: "tracking", element: <TrackingSearchPage /> },
      { path: "create-shipment", element: <CreateShipmentPage /> },
      { path: "payment/:shipmentId", element: <PaymentPage /> },
      { path: "tracking/:trackingNumber", element: <TrackingPage /> },
      // Admin routes
      { path: "admin", element: <AdminDashboard /> },
      { path: "admin/drivers", element: <AdminDriversPage /> },
      { path: "admin/shipments", element: <AdminShipmentsPage /> },
      { path: "admin/deliveries", element: <AdminDeliveriesPage /> },
    ],
  },
  { path: "/login", element: <LoginPage /> },
  { path: "/register", element: <RegisterPage /> },
]);
