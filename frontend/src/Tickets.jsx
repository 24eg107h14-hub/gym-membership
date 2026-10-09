import { useState, useEffect } from "react";
import { supportApi } from "./services/apiServices.js";
import StatusBadge from "./StatusBadge.jsx";

const Tickets = () => {
  const [tickets, setTickets] = useState([]);
  const [statusFilter, setStatusFilter] = useState("");
  const [categoryFilter, setCategoryFilter] = useState("");
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState("");
  const [selectedTicket, setSelectedTicket] = useState(null);
  const [responseText, setResponseText] = useState("");
  const [newStatus, setNewStatus] = useState("IN_PROGRESS");
  const [submitting, setSubmitting] = useState(false);

  const fetchTickets = async () => {
    setLoading(true);
    try {
      const res = await supportApi.getAll(statusFilter || null, categoryFilter || null);
      setTickets(res.data);
      setError("");
    } catch (err) {
      setError("Failed to load tickets queue.");
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchTickets();
  }, [statusFilter, categoryFilter]);

  const handleOpenRespond = (t) => {
    setSelectedTicket(t);
    setResponseText(t.response || "");
    setNewStatus(t.status === "OPEN" ? "RESOLVED" : t.status);
  };

  const handleSaveResponse = async (e) => {
    e.preventDefault();
    if (!selectedTicket) return;
    setSubmitting(true);
    try {
      if (responseText.trim()) {
        await supportApi.respond(selectedTicket.id, responseText.trim());
      }
      if (newStatus !== selectedTicket.status) {
        await supportApi.updateStatus(selectedTicket.id, newStatus);
      }
      setSelectedTicket(null);
      fetchTickets();
    } catch (err) {
      alert(err.response?.data?.message || "Failed to update ticket");
    } finally {
      setSubmitting(false);
    }
  };

  const handleDelete = async (id) => {
    if (!window.confirm("Delete this ticket permanently?")) return;
    try {
      await supportApi.delete(id);
      fetchTickets();
    } catch (err) {
      alert("Failed to delete ticket");
    }
  };

  if (loading) {
    return <div style={{ color: "var(--text-muted)", padding: "20px 0" }}>Loading tickets queue...</div>;
  }

  return (
    <div>
      <div style={{ display: "flex", justifyContent: "space-between", alignItems: "center", marginBottom: "20px" }}>
        <h2 className="page-title" style={{ margin: 0 }}>Support Tickets Management</h2>
      </div>

      {error && <div className="error-text">{error}</div>}

      {/* Filter Bar */}
      <div className="card" style={{ display: "flex", gap: "16px", flexWrap: "wrap", padding: "14px", alignItems: "center" }}>
        <div>
          <label style={{ marginBottom: "4px", fontSize: "12px" }}>Status Filter:</label>
          <select value={statusFilter} onChange={(e) => setStatusFilter(e.target.value)} style={{ width: "180px" }}>
            <option value="">All Statuses</option>
            <option value="OPEN">OPEN</option>
            <option value="IN_PROGRESS">IN_PROGRESS</option>
            <option value="RESOLVED">RESOLVED</option>
            <option value="CLOSED">CLOSED</option>
          </select>
        </div>

        <div>
          <label style={{ marginBottom: "4px", fontSize: "12px" }}>Category Filter:</label>
          <select value={categoryFilter} onChange={(e) => setCategoryFilter(e.target.value)} style={{ width: "180px" }}>
            <option value="">All Categories</option>
            <option value="GENERAL">GENERAL</option>
            <option value="BILLING">BILLING</option>
            <option value="FACILITY">FACILITY</option>
            <option value="TRAINER">TRAINER</option>
            <option value="MEMBERSHIP">MEMBERSHIP</option>
          </select>
        </div>
      </div>

      <div className="card">
        <div className="table-wrapper">
          <table>
            <thead>
              <tr>
                <th>Ticket #</th>
                <th>Member</th>
                <th>Subject</th>
                <th>Category</th>
                <th>Priority</th>
                <th>Status</th>
                <th>Created Date</th>
                <th style={{ textAlign: "right" }}>Actions</th>
              </tr>
            </thead>
            <tbody>
              {tickets.length === 0 ? (
                <tr>
                  <td colSpan="8" className="empty-state">
                    No tickets found matching the selected filters.
                  </td>
                </tr>
              ) : (
                tickets.map((t) => (
                  <tr key={t.id}>
                    <td style={{ fontWeight: "bold", color: "var(--accent)" }}>{t.ticketNumber}</td>
                    <td>{t.memberName} ({t.memberEmail})</td>
                    <td>{t.subject}</td>
                    <td>{t.category}</td>
                    <td>{t.priority}</td>
                    <td>
                      <StatusBadge status={t.status} />
                    </td>
                    <td>{t.createdAt ? new Date(t.createdAt).toLocaleDateString() : "—"}</td>
                    <td style={{ textAlign: "right" }}>
                      <div className="table-actions">
                        <button
                          onClick={() => handleOpenRespond(t)}
                          className="btn btn-sm"
                        >
                          Respond
                        </button>
                        <button
                          onClick={() => handleDelete(t.id)}
                          className="btn btn-danger btn-sm"
                        >
                          Delete
                        </button>
                      </div>
                    </td>
                  </tr>
                ))
              )}
            </tbody>
          </table>
        </div>
      </div>

      {/* Response & Status Dialog */}
      {selectedTicket && (
        <div className="modal-backdrop">
          <div className="card" style={{ width: "550px", maxWidth: "95%" }}>
            <div style={{ display: "flex", justifyContent: "space-between", alignItems: "center", borderBottom: "1px solid var(--border)", paddingBottom: "12px", marginBottom: "16px" }}>
              <h3 style={{ margin: 0, color: "var(--accent)" }}>Ticket {selectedTicket.ticketNumber}</h3>
              <StatusBadge status={selectedTicket.status} />
            </div>

            <div style={{ marginBottom: "16px" }}>
              <div style={{ fontWeight: "bold", fontSize: "16px" }}>{selectedTicket.subject}</div>
              <div style={{ color: "var(--text-muted)", fontSize: "13px" }}>
                From: {selectedTicket.memberName} ({selectedTicket.memberEmail}) • Category: {selectedTicket.category}
              </div>
            </div>

            <div style={{ background: "var(--surface-2)", padding: "12px", borderRadius: "6px", marginBottom: "16px" }}>
              <div style={{ fontSize: "12px", fontWeight: "bold", color: "var(--text-muted)", marginBottom: "4px" }}>Member Issue Description:</div>
              <div style={{ fontSize: "14px", whiteSpace: "pre-wrap" }}>{selectedTicket.description}</div>
            </div>

            <form onSubmit={handleSaveResponse}>
              <div style={{ marginBottom: "12px" }}>
                <label>Update Status</label>
                <select value={newStatus} onChange={(e) => setNewStatus(e.target.value)}>
                  <option value="OPEN">OPEN</option>
                  <option value="IN_PROGRESS">IN_PROGRESS</option>
                  <option value="RESOLVED">RESOLVED</option>
                  <option value="CLOSED">CLOSED</option>
                </select>
              </div>

              <div style={{ marginBottom: "16px" }}>
                <label>Resolution / Response Message</label>
                <textarea
                  rows="4"
                  value={responseText}
                  onChange={(e) => setResponseText(e.target.value)}
                  placeholder="Type resolution notes or reply sent to member..."
                />
              </div>

              <div style={{ display: "flex", justifyContent: "flex-end", gap: "10px" }}>
                <button type="button" onClick={() => setSelectedTicket(null)} className="btn btn-gray">
                  Cancel
                </button>
                <button type="submit" disabled={submitting} className="btn">
                  {submitting ? "Saving..." : "Save & Update"}
                </button>
              </div>
            </form>
          </div>
        </div>
      )}
    </div>
  );
};

export default Tickets;
