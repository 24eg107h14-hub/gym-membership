import { useState, useEffect } from "react";
import { useNavigate, useParams } from "react-router-dom";
import api from "./Api.jsx";

const MemberForm = () => {
  const { id } = useParams();
  const isEdit = Boolean(id);
  const navigate = useNavigate();

  const [formData, setFormData] = useState({
    fullName: "",
    email: "",
    phone: "",
    address: "",
    gender: "Male",
    dob: "",
    status: "ACTIVE",
    planId: "",
  });

  const [plans, setPlans] = useState([]);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState("");

  useEffect(() => {
    const loadData = async () => {
      try {
        const planRes = await api.get("/plans?all=true");
        setPlans(planRes.data);

        if (isEdit) {
          setLoading(true);
          const memRes = await api.get(`/members/${id}`);
          setFormData({
            fullName: memRes.data.fullName || "",
            email: memRes.data.email || "",
            phone: memRes.data.phone || "",
            address: memRes.data.address || "",
            gender: memRes.data.gender || "Male",
            dob: memRes.data.dob || "",
            status: memRes.data.status || "ACTIVE",
            planId: memRes.data.planId ? memRes.data.planId.toString() : "",
          });
        }
      } catch (err) {
        setError("Failed to load member details.");
      } finally {
        setLoading(false);
      }
    };

    loadData();
  }, [id, isEdit]);

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError("");

    try {
      if (isEdit) {
        await api.put(`/members/${id}`, formData);
      } else {
        await api.post("/members", formData);
      }
      navigate("/members");
    } catch (err) {
      setError(err.response?.data?.message || "Failed to save member.");
    }
  };

  if (loading) {
    return <div style={{ color: "var(--text-muted)", padding: "20px 0" }}>Loading form...</div>;
  }

  return (
    <div style={{ maxWidth: "600px", margin: "0 auto" }}>
      <h2 className="page-title">{isEdit ? "Edit Member" : "Add New Member"}</h2>

      {error && <div className="error-text">{error}</div>}

      <div className="card">
        <form onSubmit={handleSubmit}>
          <div style={{ marginBottom: "14px" }}>
            <label>Full Name</label>
            <input
              type="text"
              required
              value={formData.fullName}
              onChange={(e) => setFormData({ ...formData, fullName: e.target.value })}
              placeholder="e.g. Michael Scott"
            />
          </div>

          <div style={{ display: "grid", gridTemplateColumns: "1fr 1fr", gap: "12px", marginBottom: "14px" }}>
            <div>
              <label>Email</label>
              <input
                type="email"
                required
                value={formData.email}
                onChange={(e) => setFormData({ ...formData, email: e.target.value })}
                placeholder="michael@dundermifflin.com"
              />
            </div>
            <div>
              <label>Phone Number</label>
              <input
                type="tel"
                required
                value={formData.phone}
                onChange={(e) => setFormData({ ...formData, phone: e.target.value })}
                placeholder="9876543210"
              />
            </div>
          </div>

          <div style={{ display: "grid", gridTemplateColumns: "1fr 1fr", gap: "12px", marginBottom: "14px" }}>
            <div>
              <label>Gender</label>
              <select
                value={formData.gender}
                onChange={(e) => setFormData({ ...formData, gender: e.target.value })}
              >
                <option value="Male">Male</option>
                <option value="Female">Female</option>
                <option value="Other">Other</option>
              </select>
            </div>
            <div>
              <label>Date of Birth</label>
              <input
                type="date"
                value={formData.dob}
                onChange={(e) => setFormData({ ...formData, dob: e.target.value })}
              />
            </div>
          </div>

          <div style={{ marginBottom: "14px" }}>
            <label>Address</label>
            <input
              type="text"
              value={formData.address}
              onChange={(e) => setFormData({ ...formData, address: e.target.value })}
              placeholder="Scranton, PA"
            />
          </div>

          <div style={{ display: "grid", gridTemplateColumns: "1fr 1fr", gap: "12px", marginBottom: "20px" }}>
            <div>
              <label>Membership Plan</label>
              <select
                value={formData.planId}
                onChange={(e) => setFormData({ ...formData, planId: e.target.value })}
              >
                <option value="">No Plan Assigned</option>
                {plans.map((p) => (
                  <option key={p.id} value={p.id}>
                    {p.name}
                  </option>
                ))}
              </select>
            </div>
            <div>
              <label>Status</label>
              <select
                value={formData.status}
                onChange={(e) => setFormData({ ...formData, status: e.target.value })}
              >
                <option value="ACTIVE">ACTIVE</option>
                <option value="EXPIRED">EXPIRED</option>
                <option value="FROZEN">FROZEN</option>
                <option value="CANCELLED">CANCELLED</option>
              </select>
            </div>
          </div>

          <div style={{ display: "flex", justifyContent: "flex-end", gap: "10px" }}>
            <button type="button" onClick={() => navigate("/members")} className="btn btn-gray">
              Cancel
            </button>
            <button type="submit" className="btn">
              {isEdit ? "Update Member" : "Create Member"}
            </button>
          </div>
        </form>
      </div>
    </div>
  );
};

export default MemberForm;
