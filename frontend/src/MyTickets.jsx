import { useState, useEffect } from "react";
import { supportApi } from "./services/apiServices.js";
import StatusBadge from "./StatusBadge.jsx";

const MyTickets = () => {
  const [tickets, setTickets] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState("");
  const [showModal, setShowModal] = useState(false);
  const [selectedTicket, setSelectedTicket] = useState(null);

  const [formData, setFormData] = useState({
    subject: "",
    category: "GENERAL",
    priority: "MEDIUM",
    description: "",
  });

  const fetchTickets = async () => {
    setLoading(true);
    try {
      const res = await supportApi.getMyTickets();
      setTickets(res.data);
      setError("");
    } catch (err) {
      setError("Failed to load your support tickets.");
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchTickets();
  }, []);

  const handleCreateTicket = async (e) => {
    e.preventDefault();
    try {
      await supportApi.create(formData);
      setShowModal(false);
      setFormData({
        subject: "",
        category: "GENERAL",
        priority: "MEDIUM",
        description: "",
      });
      fetchTickets();
    } catch (err) {
      alert(err.response?.data?.message || "Failed to create support ticket");
    }
  };

  if (loading) {
    return <div style={{ color: "var(--text-muted)", padding: "20px 0" }}>Loading support tickets...</div>;
  }

  return (
    <div>
      <div style={{ display: "flex", justifyContent: "space-between", alignItems: "center", marginBottom: "20px" }}>
        <h2 className="page-title" style={{ margin: 0 }}>Help & Support Tickets</h2>
        <button onClick={() => setShowModal(true)} className="btn">
          + Open New Ticket
        </button>
      </div>

      {error && <div className="error-text">{error}</div>}

      <div className="card">
        <div className="table-wrapper">
          <table>
            <thead>
              <tr>
                <th>Ticket #</th>
                <th>Subject</th>
                <th>Category</th>
                <th>Priority</th>
                <th>Status</th>
                <th>Created Date</th>
                <th style={{ textAlign: "right" }}>Action</th>
              </tr>
            </thead>
            <tbody>
              {tickets.length === 0 ? (
                <tr>
                  <td colSpan="7" className="empty-state">
                    You have no active support tickets. Click "+ Open New Ticket" if you need any assistance.
                  </td>
                </tr>
              ) : (
                tickets.map((t) => (
                  <tr key={t.id}>
                    <td style={{ fontWeight: "bold", color: "var(--accent)" }}>{t.ticketNumber}</td>
                    <td style={{ fontWeight: "bold" }}>{t.subject}</td>
                    <td>{t.category}</td>
                    <td>{t.priority}</td>
                    <td>
                      <StatusBadge status={t.status} />
                    </td>
                    <td>{t.createdAt ? new Date(t.createdAt).toLocaleDateString() : "—"}</td>
                    <td style={{ textAlign: "right" }}>
                      <button
                        onClick={() => setSelectedTicket(t)}
                        className="btn btn-gray btn-sm"
                      >
                        View Ticket
                      </button>
                    </td>
                  </tr>
                ))
              )}
            </tbody>
          </table>
        </div>
      </div>

      {/* View Ticket Details Modal */}
      {selectedTicket && (
        <div className="modal-backdrop">
          <div className="card" style={{ width: "500px", maxWidth: "90%" }}>
            <div style={{ display: "flex", justifyContent: "space-between", alignItems: "center", borderBottom: "1px solid var(--border)", paddingBottom: "12px", marginBottom: "16px" }}>
              <h3 style={{ margin: 0, color: "var(--accent)" }}>{selectedTicket.ticketNumber}</h3>
              <StatusBadge status={selectedTicket.status} />
            </div>

            <div style={{ marginBottom: "16px" }}>
              <div style={{ fontSize: "16px", fontWeight: "bold", marginBottom: "4px" }}>{selectedTicket.subject}</div>
              <div style={{ fontSize: "12px", color: "var(--text-muted)" }}>
                Category: {selectedTicket.category} • Priority: {selectedTicket.priority} • Opened on: {new Date(selectedTicket.createdAt).toLocaleString()}
              </div>
            </div>

            <div style={{ background: "var(--surface-2)", padding: "12px", borderRadius: "6px", marginBottom: "16px" }}>
              <div style={{ fontSize: "12px", fontWeight: "bold", color: "var(--text-muted)", marginBottom: "4px" }}>Your Message:</div>
              <div style={{ fontSize: "14px", whiteSpace: "pre-wrap" }}>{selectedTicket.description}</div>
            </div>

            {selectedTicket.response ? (
              <div style={{ background: "rgba(34, 197, 94, 0.1)", border: "1px solid rgba(34, 197, 94, 0.3)", padding: "12px", borderRadius: "6px", marginBottom: "16px" }}>
                <div style={{ fontSize: "12px", fontWeight: "bold", color: "var(--success)", marginBottom: "4px" }}>Staff Resolution Response:</div>
                <div style={{ fontSize: "14px", whiteSpace: "pre-wrap" }}>{selectedTicket.response}</div>
              </div>
            ) : (
              <div style={{ fontSize: "13px", color: "var(--text-muted)", fontStyle: "italic", marginBottom: "16px" }}>
                Our gym support team is currently reviewing your ticket.
              </div>
            )}

            <div style={{ display: "flex", justifyContent: "flex-end" }}>
              <button onClick={() => setSelectedTicket(null)} className="btn btn-gray btn-sm">
                Close
              </button>
            </div>
          </div>
        </div>
      )}

      {/* Modal for Creating New Ticket */}
      {showModal && (
        <div className="modal-backdrop">
          <div className="card" style={{ width: "450px", maxWidth: "90%" }}>
            <h3 style={{ color: "#ffffff", marginBottom: "16px" }}>Submit Support Request</h3>
            <form onSubmit={handleCreateTicket}>
              <div style={{ marginBottom: "12px" }}>
                <label>Subject</label>
                <input
                  type="text"
                  required
                  value={formData.subject}
                  onChange={(e) => setFormData({ ...formData, subject: e.target.value })}
                  placeholder="e.g. Locker room inquiry or billing question"
                />
              </div>

              <div style={{ display: "grid", gridTemplateColumns: "1fr 1fr", gap: "12px", marginBottom: "12px" }}>
                <div>
                  <label>Category</label>
                  <select
                    value={formData.category}
                    onChange={(e) => setFormData({ ...formData, category: e.target.value })}
                  >
                    <option value="GENERAL">GENERAL</option>
                    <option value="BILLING">BILLING</option>
                    <option value="FACILITY">FACILITY</option>
                    <option value="TRAINER">TRAINER</option>
                    <option value="MEMBERSHIP">MEMBERSHIP</option>
                  </select>
                </div>

                <div>
                  <label>Priority</label>
                  <select
                    value={formData.priority}
                    onChange={(e) => setFormData({ ...formData, priority: e.target.value })}
                  >
                    <option value="LOW">LOW</option>
                    <option value="MEDIUM">MEDIUM</option>
                    <option value="HIGH">HIGH</option>
                    <option value="URGENT">URGENT</option>
                  </select>
                </div>
              </div>

              <div style={{ marginBottom: "16px" }}>
                <label>Description / Details</label>
                <textarea
                  rows="4"
                  required
                  value={formData.description}
                  onChange={(e) => setFormData({ ...formData, description: e.target.value })}
                  placeholder="Explain your inquiry or issue in detail..."
                />
              </div>

              <div style={{ display: "flex", justifyContent: "flex-end", gap: "10px" }}>
                <button type="button" onClick={() => setShowModal(false)} className="btn btn-gray">
                  Cancel
                </button>
                <button type="submit" className="btn">
                  Submit Ticket
                </button>
              </div>
            </form>
          </div>
        </div>
      )}
    </div>
  );
};

export default MyTickets;
