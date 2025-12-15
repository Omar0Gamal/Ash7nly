import { Outlet, Navigate } from "react-router";
import { useAuthStore } from "@/stores/auth";

const AuthRouter = () => {
  const isAuthenticated = useAuthStore((state) => state.isAuthenticated);
  const user = useAuthStore((state) => state.user);
  console.log("AuthRouter - isAuthenticated:", isAuthenticated, "user:", user);

  if (!isAuthenticated) {
    return <Navigate to="/login" replace />;
  }

  return <Outlet />;
};

export default AuthRouter;
