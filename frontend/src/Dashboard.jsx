import { useState, useEffect } from "react";
import { useNavigate } from "react-router-dom";
import api from "./Api.jsx";
import { dashboardApi } from "./services/apiServices.js";
import StatusBadge from "./StatusBadge.jsx";

const Dashboard = () => {
  const [stats, setStats] = useState({
    totalMembers: 0,
    activeMemberships: 0,
    expiringMemberships: 0,
    expiredMemberships: 0,
    totalPlans: 0,
    activePlans: 0,
    totalPayments: 0,
    paidPayments: 0,
    totalRevenue: 0,
    totalCoupons: 0,
    activeCoupons: 0,
    totalBranches: 0,
    activeBranches: 0,
    totalTickets: 0,
    openTickets: 0,
  });
  const [expiringMembers, setExpiringMembers] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState("");
  const [refresh, setRefresh] = useState(0);
  const navigate = useNavigate();

  useEffect(() => {
    let isMounted = true;

    const fetchData = async () => {
      try {
        const [statsRes, membersRes] = await Promise.all([
          dashboardApi.getAdminStats(),
          api.get("/members?status=EXPIRING_SOON"),
        ]);
        if (isMounted) {
          setStats(statsRes.data);
          setExpiringMembers(membersRes.data);
          setError("");
        }
      } catch (err) {
        if (isMounted) {
          const message =
            err.response && err.response.data && err.response.data.message
              ? err.response.data.message
              : "Failed to load dashboard data.";
          setError(message);
        }
      } finally {
        if (isMounted) {
          setLoading(false);
        }
      }
    };

    fetchData();

    return () => {
      isMounted = false;
    };
  }, [refresh]);

  const handleRenew = async (id) => {
    try {
      await api.post(`/members/${id}/renew`);
      setRefresh((prev) => prev + 1);
    } catch (err) {
      const message =
        err.response && err.response.data && err.response.data.message
          ? err.response.data.message
          : "Failed to renew membership.";
      alert(message);
    }
  };

  const name = localStorage.getItem("name") || "Admin";

  const statSections = [
    {
      title: "Members",
      cards: [
        { title: "Total Members", count: stats.totalMembers, path: "/members", color: "var(--accent)" },
        { title: "Active Memberships", count: stats.activeMemberships, path: "/memberships", color: "var(--success)" },
        { title: "Expiring (7 Days)", count: stats.expiringMemberships, path: "/members?status=EXPIRING_SOON", color: "var(--warning)" },
        { title: "Expired", count: stats.expiredMemberships, path: "/members?status=EXPIRED", color: "var(--danger)" },
      ],
    },
    {
      title: "Revenue & Plans",
      cards: [
        { title: "Total Revenue", count: `Rs. ${stats.totalRevenue || 0}`, path: "/payments", color: "var(--success)" },
        { title: "Payments", count: `${stats.paidPayments} Paid`, path: "/payments", color: "var(--info)" },
        { title: "Plans", count: `${stats.activePlans} / ${stats.totalPlans} Active`, path: "/plans", color: "var(--accent)" },
        { title: "Coupons", count: `${stats.activeCoupons} / ${stats.totalCoupons} Active`, path: "/coupons", color: "var(--warning)" },
      ],
    },
    {
      title: "Operations",
      cards: [
        { title: "Branches", count: `${stats.activeBranches} / ${stats.totalBranches} Active`, path: "/branches", color: "var(--accent)" },
        { title: "Support Tickets", count: `${stats.openTickets} Open`, path: "/tickets", color: stats.openTickets > 0 ? "var(--danger)" : "var(--success)" },
      ],
    },
  ];

  if (loading) {
    return <div className="dashboard-loading">Loading dashboard...</div>;
  }

  return (
    <div>
      <div className="dashboard-header">
        <div>
          <h2 className="page-title">Admin Operations Dashboard</h2>
          <p className="dashboard-subtitle">
            Welcome back, {name}. Here is the current state of your gym.
          </p>
        </div>
        <button
          className="btn btn-gray btn-sm"
          onClick={() => setRefresh((prev) => prev + 1)}
        >
          Refresh
        </button>
      </div>

      {error && <div className="error-text">{error}</div>}

      {statSections.map((section) => (
        <div key={section.title}>
          <div className="section-heading">{section.title}</div>
          <div className="stat-grid" style={{ gridTemplateColumns: "repeat(auto-fit, minmax(200px, 1fr))" }}>
            {section.cards.map((card) => (
              <div
                key={card.title}
                className="stat-card"
                style={{ borderLeftColor: card.color }}
                onClick={() => navigate(card.path)}
              >
                <div className="stat-number" style={{ color: card.color, fontSize: "26px" }}>
                  {card.count}
                </div>
                <div className="stat-label">{card.title}</div>
              </div>
            ))}
          </div>
        </div>
      ))}

      <div className="card">
        <div className="card-header">
          <h3>Memberships Expiring Soon</h3>
          <span className="card-header-count">
            {expiringMembers.length} {expiringMembers.length === 1 ? "member" : "members"}
          </span>
        </div>

        <div className="table-wrapper">
          <table>
            <thead>
              <tr>
                <th>Name</th>
                <th>Phone</th>
                <th>Plan</th>
                <th>Expiry Date</th>
                <th>Status</th>
                <th style={{ textAlign: "right" }}>Action</th>
              </tr>
            </thead>
            <tbody>
              {expiringMembers.length === 0 ? (
                <tr>
                  <td colSpan="6" className="empty-state">
                    No memberships expiring within the next 7 days
                  </td>
                </tr>
              ) : (
                expiringMembers.map((member) => (
                  <tr key={member.id}>
                    <td style={{ fontWeight: "bold" }}>{member.fullName}</td>
                    <td>{member.phone}</td>
                    <td>{member.planName || "—"}</td>
                    <td>{member.expiryDate}</td>
                    <td>
                      <StatusBadge status={member.status} />
                    </td>
                    <td style={{ textAlign: "right" }}>
                      <button
                        onClick={() => handleRenew(member.id)}
                        className="btn btn-sm"
                      >
                        Renew
                      </button>
                    </td>
                  </tr>
                ))
              )}
            </tbody>
          </table>
        </div>
      </div>
    </div>
  );
};

export default Dashboard;
