import { useState } from "react";
import { NavLink, Outlet, useNavigate } from "react-router-dom";

const NAV_GROUPS = {
  ADMIN: [
    { label: "Overview", links: [{ to: "/dashboard", label: "Dashboard" }] },
    {
      label: "People",
      links: [
        { to: "/members", label: "Members" },
        { to: "/employees", label: "Staff" },
      ],
    },
    {
      label: "Business",
      links: [
        { to: "/memberships", label: "Memberships" },
        { to: "/plans", label: "Plans" },
        { to: "/payments", label: "Payments" },
        { to: "/invoices", label: "Invoices" },
        { to: "/coupons", label: "Coupons" },
        { to: "/branches", label: "Branches" },
      ],
    },
    { label: "Support", links: [{ to: "/tickets", label: "Support Tickets" }] },
    { label: "Account", links: [{ to: "/profile", label: "Profile" }] },
  ],
  SUPPORT: [
    { label: "Overview", links: [{ to: "/support-dashboard", label: "Support Dashboard" }] },
    { label: "Support", links: [{ to: "/tickets", label: "Tickets Queue" }] },
    { label: "People", links: [{ to: "/members", label: "Members" }] },
    { label: "Business", links: [{ to: "/plans", label: "Plans" }] },
    { label: "Account", links: [{ to: "/profile", label: "Profile" }] },
  ],
  MEMBER: [
    { label: "Overview", links: [{ to: "/dashboard", label: "Overview" }] },
    {
      label: "Membership",
      links: [
        { to: "/plans", label: "Plans" },
        { to: "/purchase", label: "Subscribe" },
        { to: "/my-membership", label: "My Membership" },
      ],
    },
    {
      label: "Billing",
      links: [
        { to: "/payments", label: "Payments" },
        { to: "/invoices", label: "Invoices" },
      ],
    },
    { label: "Support", links: [{ to: "/my-tickets", label: "Help & Tickets" }] },
    { label: "Locations", links: [{ to: "/branches", label: "Branches" }] },
    { label: "Account", links: [{ to: "/profile", label: "Profile" }] },
  ],
};

const AppLayout = () => {
  const navigate = useNavigate();
  const role = localStorage.getItem("role");
  const name = localStorage.getItem("name") || "User";
  const [open, setOpen] = useState(false);

  const groups = NAV_GROUPS[role] || NAV_GROUPS.MEMBER;

  const handleLogout = () => {
    localStorage.clear();
    // Always drop the user back on the public home page after logout.
    navigate("/", { replace: true });
  };

  return (
    <div className="app-shell">
      <div className="app-topbar">
        <button
          type="button"
          className="sidebar-toggle"
          aria-label="Toggle navigation"
          aria-expanded={open}
          onClick={() => setOpen((prev) => !prev)}
        >
          ☰
        </button>
        <span className="app-topbar-brand" onClick={() => navigate("/dashboard")}>
          GYM MANAGER
        </span>
      </div>

      <div
        className={open ? "sidebar-backdrop open" : "sidebar-backdrop"}
        onClick={() => setOpen(false)}
      />

      <aside className={open ? "sidebar open" : "sidebar"}>
        <div className="sidebar-brand" onClick={() => navigate("/dashboard")}>
          GYM MANAGER
        </div>

        <nav className="sidebar-nav">
          {groups.map((group) => (
            <div key={group.label}>
              <div className="sidebar-group-label">{group.label}</div>
              {group.links.map((link) => (
                <NavLink
                  key={link.to}
                  to={link.to}
                  end={link.to === "/dashboard"}
                  className={({ isActive }) => (isActive ? "sidebar-link active" : "sidebar-link")}
                  onClick={() => setOpen(false)}
                >
                  {link.label}
                </NavLink>
              ))}
            </div>
          ))}
        </nav>

        <div className="sidebar-footer">
          <div className="sidebar-user">
            <span className="sidebar-user-name">{name}</span>
            <span className="role-pill">{role}</span>
          </div>
          <button onClick={handleLogout} className="btn btn-danger btn-sm btn-block">
            Logout
          </button>
        </div>
      </aside>

      <main className="app-main">
        <Outlet />
      </main>
    </div>
  );
};

export default AppLayout;
