import "./App.css";
import { BrowserRouter, Routes, Route, Navigate } from "react-router-dom";
import ProtectedRoute from "./ProtectedRoute.jsx";
import LandingPage from "./LandingPage.jsx";
import Login from "./Login.jsx";
import Register from "./Register.jsx";
import Dashboard from "./Dashboard.jsx";
import MemberDashboard from "./MemberDashboard.jsx";
import SupportDashboard from "./SupportDashboard.jsx";
import Members from "./Members.jsx";
import MemberForm from "./MemberForm.jsx";
import Memberships from "./Memberships.jsx";
import Plans from "./Plans.jsx";
import PurchaseMembership from "./PurchaseMembership.jsx";
import MyMembership from "./MyMembership.jsx";
import Payments from "./Payments.jsx";
import Invoices from "./Invoices.jsx";
import Coupons from "./Coupons.jsx";
import Branches from "./Branches.jsx";
import MyTickets from "./MyTickets.jsx";
import Tickets from "./Tickets.jsx";
import Employees from "./Employees.jsx";
import Profile from "./Profile.jsx";

const DashboardSwitcher = () => {
  const role = localStorage.getItem("role");
  if (role === "ADMIN") return <Dashboard />;
  if (role === "SUPPORT") return <SupportDashboard />;
  return <MemberDashboard />;
};

const RootRoute = () => {
  const token = localStorage.getItem("token");
  if (!token) {
    return <LandingPage />;
  }
  return <Navigate to="/dashboard" replace />;
};

const App = () => {
  return (
    <BrowserRouter
      future={{
        v7_startTransition: true,
        v7_relativeSplatPath: true,
      }}
    >
      <Routes>
        {/* Public Routes */}
        <Route path="/" element={<RootRoute />} />
        <Route path="/landing" element={<LandingPage />} />
        <Route path="/login" element={<Login />} />
        <Route path="/register" element={<Register />} />

        {/* Authenticated Routes with Sidebar Layout */}
        <Route element={<ProtectedRoute roles={["ADMIN", "SUPPORT", "MEMBER", "USER"]} />}>
          <Route path="/dashboard" element={<DashboardSwitcher />} />
          <Route path="/profile" element={<Profile />} />
          <Route path="/plans" element={<Plans />} />
          <Route path="/branches" element={<Branches />} />
          <Route path="/payments" element={<Payments />} />
          <Route path="/invoices" element={<Invoices />} />
          <Route path="/my-membership" element={<MyMembership />} />
          <Route path="/purchase" element={<PurchaseMembership />} />
          <Route path="/my-tickets" element={<MyTickets />} />
        </Route>

        {/* Support & Admin Management Routes */}
        <Route element={<ProtectedRoute roles={["ADMIN", "SUPPORT"]} />}>
          <Route path="/support-dashboard" element={<SupportDashboard />} />
          <Route path="/tickets" element={<Tickets />} />
          <Route path="/members" element={<Members />} />
          <Route path="/members/add" element={<MemberForm />} />
          <Route path="/members/edit/:id" element={<MemberForm />} />
        </Route>

        {/* Admin Only Operations */}
        <Route element={<ProtectedRoute roles={["ADMIN"]} />}>
          <Route path="/admin-dashboard" element={<Dashboard />} />
          <Route path="/memberships" element={<Memberships />} />
          <Route path="/coupons" element={<Coupons />} />
          <Route path="/employees" element={<Employees />} />
        </Route>

        {/* Fallback */}
        <Route path="*" element={<Navigate to="/" replace />} />
      </Routes>
    </BrowserRouter>
  );
};

export default App;