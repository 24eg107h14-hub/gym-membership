import { useState, useEffect } from "react";
import { Link, useNavigate } from "react-router-dom";
import api from "./Api.jsx";
import StatusBadge from "./StatusBadge.jsx";

const Members = () => {
  const [members, setMembers] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState("");
  const [statusFilter, setStatusFilter] = useState("");
  const [planFilter, setPlanFilter] = useState("");
  const [search, setSearch] = useState("");
  const [plans, setPlans] = useState([]);

  const role = localStorage.getItem("role");
  const isAdmin = role === "ADMIN";
  const navigate = useNavigate();

  const fetchMembers = async () => {
    setLoading(true);
    try {
      const params = {};
      if (statusFilter) params.status = statusFilter;
      if (planFilter) params.plan = planFilter;
      if (search) params.search = search;

      const [memRes, planRes] = await Promise.all([
        api.get("/members", { params }),
        api.get("/plans?all=true"),
      ]);

      setMembers(memRes.data);
      setPlans(planRes.data);
      setError("");
    } catch (err) {
      setError("Failed to load members directory.");
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchMembers();
  }, [statusFilter, planFilter]);

  const handleSearch = (e) => {
    e.preventDefault();
    fetchMembers();
  };

  const handleDelete = async (id) => {
    if (!window.confirm("Delete this member record permanently?")) return;
    try {
      await api.delete(`/members/${id}`);
      fetchMembers();
    } catch (err) {
      alert("Failed to delete member");
    }
  };

  if (loading) {
    return <div style={{ color: "var(--text-muted)", padding: "20px 0" }}>Loading members...</div>;
  }

  return (
    <div>
      <div style={{ display: "flex", justifyContent: "space-between", alignItems: "center", marginBottom: "20px" }}>
        <h2 className="page-title" style={{ margin: 0 }}>Member Directory</h2>
        <Link to="/members/add" className="btn">
          + Add New Member
        </Link>
      </div>

      {error && <div className="error-text">{error}</div>}

      {/* Search & Filter Toolbar */}
      <div className="card" style={{ padding: "14px", marginBottom: "16px" }}>
        <form onSubmit={handleSearch} style={{ display: "flex", gap: "12px", flexWrap: "wrap", alignItems: "center" }}>
          <input
            type="text"
            placeholder="Search by name, email, or phone..."
            value={search}
            onChange={(e) => setSearch(e.target.value)}
            style={{ width: "260px" }}
          />

          <select value={statusFilter} onChange={(e) => setStatusFilter(e.target.value)} style={{ width: "160px" }}>
            <option value="">All Statuses</option>
            <option value="ACTIVE">ACTIVE</option>
            <option value="EXPIRING_SOON">EXPIRING_SOON</option>
            <option value="EXPIRED">EXPIRED</option>
            <option value="FROZEN">FROZEN</option>
          </select>

          <select value={planFilter} onChange={(e) => setPlanFilter(e.target.value)} style={{ width: "160px" }}>
            <option value="">All Plans</option>
            {plans.map((p) => (
              <option key={p.id} value={p.name}>
                {p.name}
              </option>
            ))}
          </select>

          <button type="submit" className="btn btn-sm">Search</button>
          <button type="button" onClick={() => { setSearch(""); setStatusFilter(""); setPlanFilter(""); }} className="btn btn-gray btn-sm">
            Reset
          </button>
        </form>
      </div>

      <div className="card">
        <div className="table-wrapper">
          <table>
            <thead>
              <tr>
                <th>Member Name</th>
                <th>Email</th>
                <th>Phone</th>
                <th>Plan</th>
                <th>Status</th>
                <th>Expiry Date</th>
                <th style={{ textAlign: "right" }}>Actions</th>
              </tr>
            </thead>
            <tbody>
              {members.length === 0 ? (
                <tr>
                  <td colSpan="7" className="empty-state">
                    No members found matching your search.
                  </td>
                </tr>
              ) : (
                members.map((m) => (
                  <tr key={m.id}>
                    <td style={{ fontWeight: "bold" }}>{m.fullName}</td>
                    <td>{m.email || "—"}</td>
                    <td>{m.phone}</td>
                    <td>{m.planName || "—"}</td>
                    <td>
                      <StatusBadge status={m.status} />
                    </td>
                    <td>{m.expiryDate || "—"}</td>
                    <td style={{ textAlign: "right" }}>
                      <div className="table-actions">
                        <button onClick={() => navigate(`/members/edit/${m.id}`)} className="btn btn-gray btn-sm">
                          Edit
                        </button>
                        {isAdmin && (
                          <button onClick={() => handleDelete(m.id)} className="btn btn-danger btn-sm">
                            Delete
                          </button>
                        )}
                      </div>
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

export default Members;
