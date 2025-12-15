import { useState } from "react";
import { useNavigate } from "react-router";
import { useAuthStore } from "@/stores/auth";
import { Button } from "@/components/ui/button";
import {
  Users,
  Package,
  FileText,
  LogOut,
  Menu,
  X,
  BarChart3,
} from "lucide-react";
import { ProfileModal } from "@/components/ProfileModal";

interface AdminLayoutProps {
  children: React.ReactNode;
}

const AdminLayout = ({ children }: AdminLayoutProps) => {
  const navigate = useNavigate();
  const { user, clearAuthState } = useAuthStore();
  const [sidebarOpen, setSidebarOpen] = useState(true);
  const [profileModalOpen, setProfileModalOpen] = useState(false);

  const handleLogout = () => {
    clearAuthState();
    navigate("/login");
  };

  const menuItems = [
    {
      icon: BarChart3,
      label: "Dashboard",
      href: "/admin",
      activePattern: /^\/admin\/?$/,
    },
    {
      icon: Users,
      label: "Drivers",
      href: "/admin/drivers",
      activePattern: /^\/admin\/drivers/,
    },
    {
      icon: Package,
      label: "Shipments",
      href: "/admin/shipments",
      activePattern: /^\/admin\/shipments/,
    },
    {
      icon: FileText,
      label: "Deliveries",
      href: "/admin/deliveries",
      activePattern: /^\/admin\/deliveries/,
    },
  ];

  const isActive = (pattern: RegExp) => pattern.test(location.pathname);

  return (
    <div className="flex h-screen bg-gray-50">
      {/* Sidebar */}
      <div
        className={`${
          sidebarOpen ? "w-64" : "w-20"
        } bg-white border-r border-gray-200 transition-all duration-300 flex flex-col`}
      >
        {/* Logo */}
        <div className="h-16 flex items-center justify-between px-4 border-b border-gray-200">
          {sidebarOpen && (
            <h1 className="text-xl font-bold text-[#ef4444]">Ash7nly</h1>
          )}
          <Button
            variant="ghost"
            size="sm"
            onClick={() => setSidebarOpen(!sidebarOpen)}
            className="ml-auto"
          >
            {sidebarOpen ? (
              <X className="w-5 h-5" />
            ) : (
              <Menu className="w-5 h-5" />
            )}
          </Button>
        </div>

        {/* Menu Items */}
        <nav className="flex-1 px-4 py-6 space-y-2">
          {menuItems.map((item) => (
            <Button
              key={item.href}
              variant={isActive(item.activePattern) ? "default" : "ghost"}
              className={`w-full justify-start gap-3 ${
                isActive(item.activePattern)
                  ? "bg-[#ef4444] text-white hover:bg-[#dc2626]"
                  : "text-gray-700 hover:bg-gray-100"
              }`}
              onClick={() => navigate(item.href)}
            >
              <item.icon className="w-5 h-5 shrink-0" />
              {sidebarOpen && <span>{item.label}</span>}
            </Button>
          ))}
        </nav>

        {/* User Section */}
        <div className="px-4 py-4 border-t border-gray-200 space-y-2">
          <Button
            variant="ghost"
            className="w-full justify-start gap-3 text-gray-700 hover:bg-gray-100"
            onClick={() => setProfileModalOpen(true)}
          >
            <div className="w-5 h-5 rounded-full bg-[#ef4444] flex items-center justify-center text-white text-xs font-bold shrink-0">
              {user?.fullName.charAt(0).toUpperCase()}
            </div>
            {sidebarOpen && (
              <span className="text-sm font-medium truncate">
                {user?.fullName}
              </span>
            )}
          </Button>
          <Button
            variant="ghost"
            className="w-full justify-start gap-3 text-red-600 hover:bg-red-50"
            onClick={handleLogout}
          >
            <LogOut className="w-5 h-5 shrink-0" />
            {sidebarOpen && <span>Logout</span>}
          </Button>
        </div>
      </div>

      {/* Main Content */}
      <div className="flex-1 flex flex-col overflow-hidden">
        {/* Top Bar */}
        <div className="h-16 bg-white border-b border-gray-200 flex items-center justify-between px-8">
          <h2 className="text-xl font-semibold text-gray-900">
            Admin Dashboard
          </h2>
          <div className="flex items-center gap-4">
            <span className="text-sm text-gray-600">{user?.email}</span>
            <div
              className="w-10 h-10 rounded-full bg-[#ef4444] flex items-center justify-center text-white font-bold cursor-pointer hover:bg-[#dc2626]"
              onClick={() => setProfileModalOpen(true)}
            >
              {user?.fullName.charAt(0).toUpperCase()}
            </div>
          </div>
        </div>

        {/* Content */}
        <div className="flex-1 overflow-auto">{children}</div>
      </div>

      {/* Profile Modal */}
      <ProfileModal
        open={profileModalOpen}
        onOpenChange={setProfileModalOpen}
        profile={
          user
            ? {
                id: user.id,
                email: user.email,
                fullName: user.fullName,
                phoneNumber: user.phoneNumber || null,
                role: user.role,
              }
            : null
        }
      />
    </div>
  );
};

export default AdminLayout;
