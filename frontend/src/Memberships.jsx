import { useState, useEffect } from "react";
import { membershipApi } from "./services/apiServices.js";
import StatusBadge from "./StatusBadge.jsx";

const Memberships = () => {
  const [memberships, setMemberships] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState("");

  useEffect(() => {
    const fetchMemberships = async () => {
      setLoading(true);
      try {
        const res = await membershipApi.getAll();
        setMemberships(res.data);
        setError("");
      } catch (err) {
        setError("Failed to load gym memberships subscriptions.");
      } finally {
        setLoading(false);
      }
    };

    fetchMemberships();
  }, []);

  if (loading) {
    return <div style={{ color: "var(--text-muted)", padding: "20px 0" }}>Loading memberships...</div>;
  }

  return (
    <div>
      <h2 className="page-title">All Gym Subscriptions & Memberships</h2>

      {error && <div className="error-text">{error}</div>}

      <div className="card">
        <div className="table-wrapper">
          <table>
            <thead>
              <tr>
                <th>Member</th>
                <th>Plan</th>
                <th>Branch</th>
                <th>Start Date</th>
                <th>End Date</th>
                <th>Amount</th>
                <th>Status</th>
                <th>Days Remaining</th>
              </tr>
            </thead>
            <tbody>
              {memberships.length === 0 ? (
                <tr>
                  <td colSpan="8" className="empty-state">
                    No active or historical membership subscriptions found.
                  </td>
                </tr>
              ) : (
                memberships.map((m) => (
                  <tr key={m.id}>
                    <td style={{ fontWeight: "bold" }}>{m.memberName} ({m.memberEmail})</td>
                    <td style={{ color: "var(--accent)" }}>{m.planName}</td>
                    <td>{m.branchName || "All Branches"}</td>
                    <td>{m.startDate}</td>
                    <td>{m.endDate}</td>
                    <td style={{ fontWeight: "bold" }}>Rs. {m.finalAmount || m.price}</td>
                    <td>
                      <StatusBadge status={m.status} />
                    </td>
                    <td>{m.daysRemaining > 0 ? `${m.daysRemaining} days` : "0 days"}</td>
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

export default Memberships;
