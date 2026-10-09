import { useState, useEffect } from "react";
import { useNavigate } from "react-router-dom";
import { dashboardApi, supportApi } from "./services/apiServices.js";
import StatusBadge from "./StatusBadge.jsx";

const SupportDashboard = () => {
  const [stats, setStats] = useState({
    totalTickets: 0,
    openTickets: 0,
    inProgressTickets: 0,
    resolvedTickets: 0,
  });
  const [recentTickets, setRecentTickets] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState("");
  const navigate = useNavigate();

  useEffect(() => {
    let isMounted = true;

    const fetchData = async () => {
      try {
        const [statsRes, ticketsRes] = await Promise.all([
          dashboardApi.getSupportStats(),
          supportApi.getAll("OPEN", null),
        ]);
        if (isMounted) {
          setStats(statsRes.data);
          setRecentTickets(ticketsRes.data.slice(0, 10));
          setError("");
        }
      } catch (err) {
        if (isMounted) {
          setError("Failed to load support dashboard statistics.");
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
  }, []);

  if (loading) {
    return <div style={{ color: "var(--text-muted)", padding: "20px 0" }}>Loading support dashboard...</div>;
  }

  return (
    <div>
      <div style={{ display: "flex", justifyContent: "space-between", alignItems: "center", marginBottom: "20px" }}>
        <h2 className="page-title" style={{ margin: 0 }}>Support Desk Overview</h2>
        <button onClick={() => navigate("/tickets")} className="btn btn-sm">
          View All Tickets Queue →
        </button>
      </div>

      {error && <div className="error-text">{error}</div>}

      <div className="stat-grid">
        <div className="stat-card" onClick={() => navigate("/tickets?status=OPEN")}>
          <div className="stat-number" style={{ color: "var(--danger)" }}>{stats.openTickets}</div>
          <div className="stat-label">Open Tickets (Action Required)</div>
        </div>

        <div className="stat-card" onClick={() => navigate("/tickets?status=IN_PROGRESS")}>
          <div className="stat-number" style={{ color: "var(--warning)" }}>{stats.inProgressTickets}</div>
          <div className="stat-label">In Progress Tickets</div>
        </div>

        <div className="stat-card" onClick={() => navigate("/tickets?status=RESOLVED")}>
          <div className="stat-number" style={{ color: "var(--success)" }}>{stats.resolvedTickets}</div>
          <div className="stat-label">Resolved Tickets</div>
        </div>

        <div className="stat-card" onClick={() => navigate("/tickets")}>
          <div className="stat-number" style={{ color: "var(--accent)" }}>{stats.totalTickets}</div>
          <div className="stat-label">Total Lifetime Tickets</div>
        </div>
      </div>

      <div className="card">
        <h3 style={{ color: "#ffffff", marginBottom: "16px", fontSize: "18px" }}>
          Unresolved Tickets Queue
        </h3>

        <div className="table-wrapper">
          <table>
            <thead>
              <tr>
                <th>Ticket #</th>
                <th>Member</th>
                <th>Subject</th>
                <th>Category</th>
                <th>Status</th>
                <th>Created At</th>
                <th style={{ textAlign: "right" }}>Action</th>
              </tr>
            </thead>
            <tbody>
              {recentTickets.length === 0 ? (
                <tr>
                  <td colSpan="7" className="empty-state">
                    No open tickets waiting in queue. All caught up!
                  </td>
                </tr>
              ) : (
                recentTickets.map((ticket) => (
                  <tr key={ticket.id}>
                    <td style={{ fontWeight: "bold", color: "var(--accent)" }}>{ticket.ticketNumber}</td>
                    <td>{ticket.memberName}</td>
                    <td>{ticket.subject}</td>
                    <td>{ticket.category}</td>
                    <td>
                      <StatusBadge status={ticket.status} />
                    </td>
                    <td>{ticket.createdAt ? new Date(ticket.createdAt).toLocaleDateString() : "—"}</td>
                    <td style={{ textAlign: "right" }}>
                      <button
                        onClick={() => navigate("/tickets")}
                        className="btn btn-sm"
                      >
                        Respond
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

export default SupportDashboard;
