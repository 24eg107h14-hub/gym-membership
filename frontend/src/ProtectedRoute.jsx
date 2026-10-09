import { Navigate } from "react-router-dom";
import AppLayout from "./AppLayout.jsx";

const ProtectedRoute = ({ roles }) => {
  const token = localStorage.getItem("token");
  const role = localStorage.getItem("role");

  if (!token) {
    return <Navigate to="/login" replace />;
  }

  if (roles && !roles.includes(role)) {
    return <Navigate to="/dashboard" replace />;
  }

  return <AppLayout />;
};

export default ProtectedRoute;
