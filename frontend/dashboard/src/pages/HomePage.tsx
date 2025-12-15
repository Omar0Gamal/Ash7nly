import { useEffect } from "react";
import { useNavigate } from "react-router";
import { useAuthStore } from "@/stores/auth";

const HomePage = () => {
  const navigate = useNavigate();
  const user = useAuthStore((state) => state.user);

  useEffect(() => {
    if (user?.role === "ADMIN") {
      navigate("/admin", { replace: true });
    } else {
      navigate("/merchant", { replace: true });
    }
  }, [user?.role, navigate]);

  return null;
};

export default HomePage;
