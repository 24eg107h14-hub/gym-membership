import { useState, useEffect } from "react";
import { useNavigate } from "react-router-dom";
import api from "./Api.jsx";
import StatusBadge from "./StatusBadge.jsx";

const MyMembership = () => {
  const [membership, setMembership] = useState(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState("");
  const [actionLoading, setActionLoading] = useState(false);
  const [refresh, setRefresh] = useState(0);

  const navigate = useNavigate();

  useEffect(() => {
    let isMounted = true;

    const fetchMembership = async () => {
      setLoading(true);
      try {
        const res = await api.get("/memberships/my-membership");
        if (isMounted) {
          setMembership(res.data && res.data.id ? res.data : null);
          setError("");
        }
      } catch (err) {
        if (isMounted) {
          setError("Failed to load membership details.");
        }
      } finally {
        if (isMounted) {
          setLoading(false);
        }
      }
    };

    fetchMembership();

    return () => {
      isMounted = false;
    };
  }, [refresh]);

  const handleFreeze = async () => {
    if (!membership) return;
    if (!window.confirm("Are you sure you want to freeze your membership?")) return;

    setActionLoading(true);
    try {
      await api.post(`/memberships/${membership.id}/freeze`);
      setRefresh((prev) => prev + 1);
    } catch (err) {
      alert(err.response?.data?.message || "Failed to freeze membership.");
    } finally {
      setActionLoading(false);
    }
  };

  const handleCancel = async () => {
    if (!membership) return;
    if (!window.confirm("Are you sure you want to cancel your membership?")) return;

    setActionLoading(true);
    try {
      await api.post(`/memberships/${membership.id}/cancel`);
      setRefresh((prev) => prev + 1);
    } catch (err) {
      alert(err.response?.data?.message || "Failed to cancel membership.");
    } finally {
      setActionLoading(false);
    }
  };

  const handleRenew = async () => {
    if (!membership) return;
    if (!window.confirm("Do you want to renew your current membership plan?")) return;

    setActionLoading(true);
    try {
      await api.post(`/memberships/${membership.id}/renew`);
      setRefresh((prev) => prev + 1);
    } catch (err) {
      alert(err.response?.data?.message || "Failed to renew membership.");
    } finally {
      setActionLoading(false);
    }
  };

  const handleUpgrade = () => {
    if (!membership) return;
    navigate(`/plans?upgrade=true&membershipId=${membership.id}`);
  };

  if (loading) {
    return <div style={{ color: "var(--text-muted)", padding: "20px 0" }}>Loading membership...</div>;
  }

  return (
    <div>
      <h2 className="page-title">My Membership</h2>

      {error && <div className="error-text" style={{ maxWidth: "550px", margin: "0 auto 16px" }}>{error}</div>}

      {!membership ? (
        <div className="card" style={{ maxWidth: "550px", margin: "0 auto", textAlign: "center", padding: "32px 20px" }}>
          <h3 style={{ color: "#ffffff", marginBottom: "8px" }}>No Active Membership</h3>
          <p style={{ color: "var(--text-muted)", marginBottom: "20px", fontSize: "15px" }}>
            You do not have an active membership at the moment. Browse our plans to get started.
          </p>
          <button
            onClick={() => navigate("/plans")}
            className="btn"
          >
            View Membership Plans
          </button>
        </div>
      ) : (
        <div className="card" style={{ maxWidth: "550px", margin: "0 auto" }}>
          <div
            style={{
              display: "flex",
              justifyContent: "space-between",
              alignItems: "center",
              marginBottom: "16px",
              borderBottom: "1px solid var(--border)",
              paddingBottom: "12px",
            }}
          >
            <div>
              <h3 style={{ margin: 0, fontSize: "20px", color: "#ffffff" }}>
                {membership.planName}
              </h3>
              <span style={{ fontSize: "13px", color: "var(--text-muted)" }}>
                {membership.memberName} ({membership.memberEmail})
              </span>
            </div>
            <StatusBadge status={membership.status} />
          </div>

          <div style={{ display: "grid", gridTemplateColumns: "1fr 1fr", gap: "16px", marginBottom: "16px" }}>
            <div>
              <div style={{ fontSize: "13px", color: "var(--text-muted)", marginBottom: "4px" }}>
                Start Date
              </div>
              <div style={{ fontSize: "15px", color: "var(--text)" }}>
                {membership.startDate}
              </div>
            </div>

            <div>
              <div style={{ fontSize: "13px", color: "var(--text-muted)", marginBottom: "4px" }}>
                Expiry Date
              </div>
              <div style={{ fontSize: "15px", color: "var(--text)" }}>
                {membership.endDate}
              </div>
            </div>

            <div>
              <div style={{ fontSize: "13px", color: "var(--text-muted)", marginBottom: "4px" }}>
                Plan Duration
              </div>
              <div style={{ fontSize: "15px", color: "var(--text)" }}>
                {membership.durationMonths} Month(s)
              </div>
            </div>

            <div>
              <div style={{ fontSize: "13px", color: "var(--text-muted)", marginBottom: "4px" }}>
                Price
              </div>
              <div style={{ fontSize: "15px", color: "var(--accent)", fontWeight: "bold" }}>
                Rs. {membership.finalAmount || membership.price}
              </div>
            </div>
          </div>

          <div
            style={{
              borderTop: "1px solid var(--border)",
              paddingTop: "16px",
              textAlign: "center",
              marginBottom: "20px",
            }}
          >
            <div
              style={{
                fontSize: "22px",
                fontWeight: "bold",
                color: membership.status === "ACTIVE" ? "var(--accent)" : "var(--text-muted)",
              }}
            >
              {membership.status === "FROZEN"
                ? "Membership is currently Frozen"
                : membership.status === "CANCELLED"
                ? "Membership Cancelled"
                : membership.daysRemaining < 0
                ? "0 days remaining (Expired)"
                : `${membership.daysRemaining} days remaining`}
            </div>
          </div>

          {/* Action Buttons for Lifecycle */}
          <div
            style={{
              display: "grid",
              gridTemplateColumns: "repeat(auto-fit, minmax(100px, 1fr))",
              gap: "10px",
              borderTop: "1px solid var(--border)",
              paddingTop: "16px",
            }}
          >
            {membership.status === "ACTIVE" && (
              <button
                onClick={handleFreeze}
                disabled={actionLoading}
                className="btn btn-gray btn-sm"
              >
                Freeze
              </button>
            )}

            {(membership.status === "ACTIVE" || membership.status === "FROZEN") && (
              <button
                onClick={handleCancel}
                disabled={actionLoading}
                className="btn btn-danger btn-sm"
              >
                Cancel
              </button>
            )}

            <button
              onClick={handleRenew}
              disabled={actionLoading}
              className="btn btn-sm"
            >
              Renew
            </button>

            <button
              onClick={handleUpgrade}
              disabled={actionLoading}
              className="btn btn-sm"
            >
              Upgrade
            </button>
          </div>
        </div>
      )}
    </div>
  );
};

export default MyMembership;
