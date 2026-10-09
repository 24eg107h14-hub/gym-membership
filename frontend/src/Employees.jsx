import { useState, useEffect } from "react";
import api from "./Api.jsx";
import StatusBadge from "./StatusBadge.jsx";

const Employees = () => {
  const [employees, setEmployees] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState("");
  const [showModal, setShowModal] = useState(false);

  const [formData, setFormData] = useState({
    name: "",
    email: "",
    password: "",
    role: "SUPPORT",
  });

  const fetchEmployees = async () => {
    setLoading(true);
    try {
      const res = await api.get("/employees");
      setEmployees(res.data);
      setError("");
    } catch (err) {
      setError("Failed to load gym staff.");
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchEmployees();
  }, []);

  const handleCreate = async (e) => {
    e.preventDefault();
    try {
      await api.post("/employees", formData);
      setShowModal(false);
      setFormData({ name: "", email: "", password: "", role: "SUPPORT" });
      fetchEmployees();
    } catch (err) {
      alert(err.response?.data?.message || "Failed to create staff account");
    }
  };

  const handleDelete = async (id) => {
    if (!window.confirm("Remove this staff member?")) return;
    try {
      await api.delete(`/employees/${id}`);
      fetchEmployees();
    } catch (err) {
      alert("Failed to delete staff member");
    }
  };

  if (loading) {
    return <div style={{ color: "var(--text-muted)", padding: "20px 0" }}>Loading staff directory...</div>;
  }

  return (
    <div>
      <div style={{ display: "flex", justifyContent: "space-between", alignItems: "center", marginBottom: "20px" }}>
        <h2 className="page-title" style={{ margin: 0 }}>Staff & Employee Management</h2>
        <button onClick={() => setShowModal(true)} className="btn">
          + Add Staff Member
        </button>
      </div>

      {error && <div className="error-text">{error}</div>}

      <div className="card">
        <div className="table-wrapper">
          <table>
            <thead>
              <tr>
                <th>Name</th>
                <th>Email</th>
                <th>Role</th>
                <th style={{ textAlign: "right" }}>Actions</th>
              </tr>
            </thead>
            <tbody>
              {employees.length === 0 ? (
                <tr>
                  <td colSpan="4" className="empty-state">No staff accounts found.</td>
                </tr>
              ) : (
                employees.map((emp) => (
                  <tr key={emp.id}>
                    <td style={{ fontWeight: "bold" }}>{emp.name}</td>
                    <td>{emp.email}</td>
                    <td>
                      <StatusBadge status={emp.role} />
                    </td>
                    <td style={{ textAlign: "right" }}>
                      <button onClick={() => handleDelete(emp.id)} className="btn btn-danger btn-sm">
                        Delete
                      </button>
                    </td>
                  </tr>
                ))
              )}
            </tbody>
          </table>
        </div>
      </div>

      {/* Modal for adding employee */}
      {showModal && (
        <div className="modal-backdrop">
          <div className="card" style={{ width: "420px", maxWidth: "90%" }}>
            <h3 style={{ color: "#ffffff", marginBottom: "16px" }}>Add Staff Member</h3>
            <form onSubmit={handleCreate}>
              <div style={{ marginBottom: "12px" }}>
                <label>Staff Full Name</label>
                <input
                  type="text"
                  required
                  value={formData.name}
                  onChange={(e) => setFormData({ ...formData, name: e.target.value })}
                  placeholder="e.g. Sarah Connor"
                />
              </div>

              <div style={{ marginBottom: "12px" }}>
                <label>Email</label>
                <input
                  type="email"
                  required
                  value={formData.email}
                  onChange={(e) => setFormData({ ...formData, email: e.target.value })}
                  placeholder="sarah@gym.com"
                />
              </div>

              <div style={{ marginBottom: "12px" }}>
                <label>Password (min 6)</label>
                <input
                  type="password"
                  required
                  minLength={6}
                  value={formData.password}
                  onChange={(e) => setFormData({ ...formData, password: e.target.value })}
                  placeholder="••••••••"
                />
              </div>

              <div style={{ marginBottom: "16px" }}>
                <label>Role</label>
                <select
                  value={formData.role}
                  onChange={(e) => setFormData({ ...formData, role: e.target.value })}
                >
                  <option value="SUPPORT">SUPPORT</option>
                  <option value="ADMIN">ADMIN</option>
                </select>
              </div>

              <div style={{ display: "flex", justifyContent: "flex-end", gap: "10px" }}>
                <button type="button" onClick={() => setShowModal(false)} className="btn btn-gray">
                  Cancel
                </button>
                <button type="submit" className="btn">
                  Create Staff
                </button>
              </div>
            </form>
          </div>
        </div>
      )}
    </div>
  );
};

export default Employees;
