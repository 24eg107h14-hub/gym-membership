import { useState, useEffect } from "react";
import { useNavigate } from "react-router-dom";
import { membershipApi, paymentApi, invoiceApi, supportApi } from "./services/apiServices.js";
import StatusBadge from "./StatusBadge.jsx";

const MemberDashboard = () => {
  const [membership, setMembership] = useState(null);
  const [recentPayments, setRecentPayments] = useState([]);
  const [recentInvoices, setRecentInvoices] = useState([]);
  const [openTickets, setOpenTickets] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState("");
  const [actionLoading, setActionLoading] = useState(false);
  const [refresh, setRefresh] = useState(0);

  const navigate = useNavigate();
  const name = localStorage.getItem("name") || "Member";

  useEffect(() => {
    let isMounted = true;

    const fetchData = async () => {
      setLoading(true);
      try {
        const [memRes, payRes, invRes, tktRes] = await Promise.allSettled([
          membershipApi.getMyMembership(),
          paymentApi.getMyPayments(),
          invoiceApi.getMyInvoices(),
          supportApi.getMyTickets(),
        ]);
        if (isMounted) {
          if (memRes.status === "fulfilled" && memRes.value?.data && memRes.value.data.id) {
            setMembership(memRes.value.data);
          } else {
            setMembership(null);
          }

          if (payRes.status === "fulfilled" && Array.isArray(payRes.value?.data)) {
            setRecentPayments(payRes.value.data.slice(0, 3));
          } else {
            setRecentPayments([]);
          }

          if (invRes.status === "fulfilled" && Array.isArray(invRes.value?.data)) {
            setRecentInvoices(invRes.value.data.slice(0, 3));
          } else {
            setRecentInvoices([]);
          }

          if (tktRes.status === "fulfilled" && Array.isArray(tktRes.value?.data)) {
            setOpenTickets(tktRes.value.data.filter((t) => t.status === "OPEN" || t.status === "IN_PROGRESS").slice(0, 3));
          } else {
            setOpenTickets([]);
          }
          setError("");
        }
      } catch (err) {
        if (isMounted) {
          console.error("Dashboard load error", err);
          setError("Failed to load some dashboard details.");
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

  const handleFreeze = async () => {
    if (!membership) return;
    if (!window.confirm("Freeze your membership?")) return;
    setActionLoading(true);
    try {
      await membershipApi.freeze(membership.id);
      setRefresh((prev) => prev + 1);
    } catch (err) {
      alert(err.response?.data?.message || "Failed to freeze");
    } finally {
      setActionLoading(false);
    }
  };

  const handleCancel = async () => {
    if (!membership) return;
    if (!window.confirm("Cancel your membership?")) return;
    setActionLoading(true);
    try {
      await membershipApi.cancel(membership.id);
      setRefresh((prev) => prev + 1);
    } catch (err) {
      alert(err.response?.data?.message || "Failed to cancel");
    } finally {
      setActionLoading(false);
    }
  };

  const handleRenew = async () => {
    if (!membership) return;
    if (!window.confirm("Renew membership for another cycle?")) return;
    setActionLoading(true);
    try {
      await membershipApi.renew(membership.id);
      setRefresh((prev) => prev + 1);
    } catch (err) {
      alert(err.response?.data?.message || "Failed to renew");
    } finally {
      setActionLoading(false);
    }
  };

  if (loading) {
    return <div style={{ color: "var(--text-muted)", padding: "20px 0" }}>Loading member dashboard...</div>;
  }

  return (
    <div>
      <div style={{ display: "flex", justifyContent: "space-between", alignItems: "center", borderBottom: "3px solid var(--accent)", paddingBottom: "8px", marginBottom: "20px" }}>
        <h2 style={{ margin: 0, fontSize: "24px", color: "#ffffff" }}>
          Welcome back, {name}!
        </h2>
        <button onClick={() => navigate("/plans")} className="btn btn-sm">
          Browse Plans →
        </button>
      </div>

      {error && <div className="error-text">{error}</div>}

      {/* Main Membership Status Card */}
      <div className="card" style={{ marginBottom: "24px" }}>
        <h3 style={{ color: "#ffffff", marginBottom: "16px", fontSize: "18px" }}>
          Current Membership Status
        </h3>

        {!membership ? (
          <div style={{ textAlign: "center", padding: "20px 0" }}>
            <p style={{ color: "var(--text-muted)", marginBottom: "16px" }}>
              You do not have an active membership plan yet.
            </p>
            <button onClick={() => navigate("/purchase")} className="btn">
              Join Now & Purchase Plan
            </button>
          </div>
        ) : (
          <div>
            <div style={{ display: "grid", gridTemplateColumns: "repeat(auto-fit, minmax(200px, 1fr))", gap: "16px", marginBottom: "16px" }}>
              <div>
                <div style={{ fontSize: "12px", color: "var(--text-muted)", textTransform: "uppercase" }}>Plan</div>
                <div style={{ fontSize: "18px", fontWeight: "bold", color: "var(--accent)" }}>{membership.planName}</div>
              </div>

              <div>
                <div style={{ fontSize: "12px", color: "var(--text-muted)", textTransform: "uppercase" }}>Branch</div>
                <div style={{ fontSize: "16px", color: "var(--text)" }}>{membership.branchName || "All Branches"}</div>
              </div>

              <div>
                <div style={{ fontSize: "12px", color: "var(--text-muted)", textTransform: "uppercase" }}>Status</div>
                <div style={{ marginTop: "4px" }}><StatusBadge status={membership.status} /></div>
              </div>

              <div>
                <div style={{ fontSize: "12px", color: "var(--text-muted)", textTransform: "uppercase" }}>Valid Until</div>
                <div style={{ fontSize: "16px", color: "var(--text)" }}>{membership.endDate}</div>
              </div>

              <div>
                <div style={{ fontSize: "12px", color: "var(--text-muted)", textTransform: "uppercase" }}>Days Left</div>
                <div style={{ fontSize: "18px", fontWeight: "bold", color: membership.daysRemaining > 0 ? "var(--success)" : "var(--danger)" }}>
                  {membership.status === "ACTIVE" ? `${membership.daysRemaining} days` : membership.status}
                </div>
              </div>
            </div>

            {/* Action Buttons */}
            <div style={{ display: "flex", gap: "10px", flexWrap: "wrap", borderTop: "1px solid var(--border)", paddingTop: "14px" }}>
              <button onClick={handleRenew} disabled={actionLoading} className="btn btn-sm">
                Renew Plan
              </button>
              <button onClick={() => navigate(`/plans?upgrade=true&membershipId=${membership.id}`)} className="btn btn-sm">
                Upgrade Plan
              </button>
              {membership.status === "ACTIVE" && (
                <button onClick={handleFreeze} disabled={actionLoading} className="btn btn-gray btn-sm">
                  Freeze
                </button>
              )}
              {(membership.status === "ACTIVE" || membership.status === "FROZEN") && (
                <button onClick={handleCancel} disabled={actionLoading} className="btn btn-danger btn-sm">
                  Cancel
                </button>
              )}
              <button onClick={() => navigate("/my-membership")} className="btn btn-gray btn-sm">
                View Full Details
              </button>
            </div>
          </div>
        )}
      </div>

      {/* Quick Action Shortcuts */}
      <div style={{ display: "grid", gridTemplateColumns: "repeat(auto-fit, minmax(180px, 1fr))", gap: "12px", marginBottom: "24px" }}>
        <button onClick={() => navigate("/purchase")} className="btn btn-gray" style={{ padding: "14px", textAlign: "center" }}>
          💳 Purchase Plan
        </button>
        <button onClick={() => navigate("/payments")} className="btn btn-gray" style={{ padding: "14px", textAlign: "center" }}>
          📜 Payment History
        </button>
        <button onClick={() => navigate("/invoices")} className="btn btn-gray" style={{ padding: "14px", textAlign: "center" }}>
          🧾 My Invoices
        </button>
        <button onClick={() => navigate("/my-tickets")} className="btn btn-gray" style={{ padding: "14px", textAlign: "center" }}>
          🎧 Support Desk
        </button>
        <button onClick={() => navigate("/profile")} className="btn btn-gray" style={{ padding: "14px", textAlign: "center" }}>
          👤 My Profile
        </button>
      </div>

      {/* Two Column Grid for Invoices and Support */}
      <div style={{ display: "grid", gridTemplateColumns: "1fr 1fr", gap: "20px" }}>
        {/* Recent Invoices */}
        <div className="card">
          <div style={{ display: "flex", justifyContent: "space-between", alignItems: "center", marginBottom: "12px" }}>
            <h4 style={{ margin: 0, color: "#ffffff" }}>Recent Invoices</h4>
            <button onClick={() => navigate("/invoices")} className="btn btn-gray btn-sm">View All</button>
          </div>
          {recentInvoices.length === 0 ? (
            <div style={{ color: "var(--text-muted)", fontSize: "14px" }}>No invoices available yet.</div>
          ) : (
            <div style={{ display: "flex", flexDirection: "column", gap: "8px" }}>
              {recentInvoices.map((inv) => (
                <div key={inv.id} style={{ display: "flex", justifyContent: "space-between", alignItems: "center", background: "var(--surface-2)", padding: "8px 12px", borderRadius: "6px", fontSize: "13px" }}>
                  <div>
                    <span style={{ fontWeight: "bold", color: "var(--accent)" }}>{inv.invoiceNumber}</span>
                    <span style={{ marginLeft: "8px", color: "var(--text-muted)" }}>{inv.planName}</span>
                  </div>
                  <span style={{ fontWeight: "bold" }}>Rs. {inv.finalAmount}</span>
                </div>
              ))}
            </div>
          )}
        </div>

        {/* Open Support Tickets */}
        <div className="card">
          <div style={{ display: "flex", justifyContent: "space-between", alignItems: "center", marginBottom: "12px" }}>
            <h4 style={{ margin: 0, color: "#ffffff" }}>Open Support Tickets</h4>
            <button onClick={() => navigate("/my-tickets")} className="btn btn-gray btn-sm">View All</button>
          </div>
          {openTickets.length === 0 ? (
            <div style={{ color: "var(--text-muted)", fontSize: "14px" }}>No open support tickets.</div>
          ) : (
            <div style={{ display: "flex", flexDirection: "column", gap: "8px" }}>
              {openTickets.map((t) => (
                <div key={t.id} style={{ display: "flex", justifyContent: "space-between", alignItems: "center", background: "var(--surface-2)", padding: "8px 12px", borderRadius: "6px", fontSize: "13px" }}>
                  <div>
                    <span style={{ fontWeight: "bold" }}>{t.subject}</span>
                    <span style={{ marginLeft: "8px", color: "var(--text-muted)" }}>({t.category})</span>
                  </div>
                  <StatusBadge status={t.status} />
                </div>
              ))}
            </div>
          )}
        </div>
      </div>
    </div>
  );
};

export default MemberDashboard;
