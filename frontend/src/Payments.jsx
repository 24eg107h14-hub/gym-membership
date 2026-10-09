import { useState, useEffect } from "react";
import { paymentApi } from "./services/apiServices.js";
import StatusBadge from "./StatusBadge.jsx";

const Payments = () => {
  const [payments, setPayments] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState("");
  const role = localStorage.getItem("role");
  const isAdmin = role === "ADMIN";

  useEffect(() => {
    const fetchPayments = async () => {
      setLoading(true);
      try {
        const res = isAdmin ? await paymentApi.getAll() : await paymentApi.getMyPayments();
        setPayments(res.data);
        setError("");
      } catch (err) {
        setError("Failed to load payment records.");
      } finally {
        setLoading(false);
      }
    };

    fetchPayments();
  }, [isAdmin]);

  if (loading) {
    return <div style={{ color: "var(--text-muted)", padding: "20px 0" }}>Loading payments...</div>;
  }

  return (
    <div>
      <h2 className="page-title">{isAdmin ? "All Gym Payment Records" : "My Payment History"}</h2>

      {error && <div className="error-text">{error}</div>}

      <div className="card">
        <div className="table-wrapper">
          <table>
            <thead>
              <tr>
                <th>Txn ID</th>
                {isAdmin && <th>Member</th>}
                <th>Plan</th>
                <th>Amount</th>
                <th>Payment Date</th>
                <th>Method</th>
                <th>Status</th>
              </tr>
            </thead>
            <tbody>
              {payments.length === 0 ? (
                <tr>
                  <td colSpan={isAdmin ? "7" : "6"} className="empty-state">
                    No payment records found.
                  </td>
                </tr>
              ) : (
                payments.map((p) => (
                  <tr key={p.id}>
                    <td style={{ fontWeight: "bold", color: "var(--accent)" }}>{p.transactionId}</td>
                    {isAdmin && <td>{p.memberName} ({p.memberEmail})</td>}
                    <td>{p.planName || "Standard Membership"}</td>
                    <td style={{ fontWeight: "bold" }}>Rs. {p.amount}</td>
                    <td>{p.paymentDate}</td>
                    <td>{p.paymentMethod}</td>
                    <td>
                      <StatusBadge status={p.paymentStatus} />
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

export default Payments;
