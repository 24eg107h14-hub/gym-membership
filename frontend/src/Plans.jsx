import { useState, useEffect } from "react";
import { useNavigate, useSearchParams } from "react-router-dom";
import api from "./Api.jsx";
import { planApi } from "./services/apiServices.js";
import StatusBadge from "./StatusBadge.jsx";

const Plans = () => {
  const [plans, setPlans] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState("");
  const [showModal, setShowModal] = useState(false);
  const [modalMode, setModalMode] = useState("create");
  const [selectedPlan, setSelectedPlan] = useState(null);

  const [formData, setFormData] = useState({
    name: "",
    durationMonths: 1,
    price: "",
    description: "",
    benefits: "",
    status: "ACTIVE",
  });

  const navigate = useNavigate();
  const [searchParams] = useSearchParams();
  const isUpgradeMode = searchParams.get("upgrade") === "true";
  const existingMembershipId = searchParams.get("membershipId");
  const role = localStorage.getItem("role");
  const isAdmin = role === "ADMIN";

  const fetchPlans = async () => {
    setLoading(true);
    try {
      const res = await planApi.getAll(isAdmin);
      setPlans(res.data);
      setError("");
    } catch (err) {
      setError("Failed to load membership plans.");
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchPlans();
  }, []);

  const handleOpenCreate = () => {
    setFormData({
      name: "",
      durationMonths: 1,
      price: "",
      description: "",
      benefits: "",
      status: "ACTIVE",
    });
    setModalMode("create");
    setShowModal(true);
  };

  const handleOpenEdit = (plan) => {
    setSelectedPlan(plan);
    setFormData({
      name: plan.name,
      durationMonths: plan.durationMonths,
      price: plan.price,
      description: plan.description || "",
      benefits: plan.benefits || "",
      status: plan.status || "ACTIVE",
    });
    setModalMode("edit");
    setShowModal(true);
  };

  const handleSave = async (e) => {
    e.preventDefault();
    try {
      if (modalMode === "create") {
        await planApi.create(formData);
      } else {
        await planApi.update(selectedPlan.id, formData);
      }
      setShowModal(false);
      fetchPlans();
    } catch (err) {
      alert(err.response?.data?.message || "Failed to save plan");
    }
  };

  const handleToggleStatus = async (id) => {
    try {
      await planApi.toggleStatus(id);
      fetchPlans();
    } catch (err) {
      alert("Failed to toggle status");
    }
  };

  const handleDelete = async (id) => {
    if (!window.confirm("Are you sure you want to delete this plan?")) return;
    try {
      await planApi.delete(id);
      fetchPlans();
    } catch (err) {
      alert("Failed to delete plan");
    }
  };

  const handleSelectPlan = (planId) => {
    if (isUpgradeMode && existingMembershipId) {
      navigate(`/my-membership?upgradePlanId=${planId}`);
    } else {
      navigate(`/purchase?planId=${planId}`);
    }
  };

  if (loading) {
    return <div style={{ color: "var(--text-muted)", padding: "20px 0" }}>Loading plans...</div>;
  }

  return (
    <div>
      <div style={{ display: "flex", justifyContent: "space-between", alignItems: "center", marginBottom: "20px" }}>
        <h2 className="page-title" style={{ margin: 0 }}>
          {isUpgradeMode ? "Select Upgrade Plan" : "Membership Plans"}
        </h2>
        {isAdmin && (
          <button onClick={handleOpenCreate} className="btn">
            + Create New Plan
          </button>
        )}
      </div>

      {error && <div className="error-text">{error}</div>}

      <div style={{ display: "grid", gridTemplateColumns: "repeat(auto-fit, minmax(300px, 1fr))", gap: "20px" }}>
        {plans.map((p) => (
          <div key={p.id} className="card" style={{ display: "flex", flexDirection: "column", justifyContent: "space-between" }}>
            <div>
              <div style={{ display: "flex", justifyContent: "space-between", alignItems: "center", marginBottom: "8px" }}>
                <h3 style={{ margin: 0, fontSize: "20px", color: "var(--accent)" }}>{p.name}</h3>
                <StatusBadge status={p.status} />
              </div>
              <p style={{ color: "var(--text-muted)", fontSize: "14px", minHeight: "36px" }}>
                {p.description}
              </p>
              <div style={{ fontSize: "28px", fontWeight: "bold", color: "#ffffff", margin: "14px 0" }}>
                Rs. {p.price} <span style={{ fontSize: "14px", color: "var(--text-muted)" }}>/ {p.durationMonths} Month(s)</span>
              </div>
              <div style={{ borderTop: "1px solid var(--border)", paddingTop: "12px", marginBottom: "16px" }}>
                <div style={{ fontSize: "12px", fontWeight: "bold", color: "var(--text-muted)", textTransform: "uppercase", marginBottom: "8px" }}>
                  Benefits:
                </div>
                <div style={{ fontSize: "14px", color: "var(--text)" }}>
                  {p.benefits || "All standard gym privileges"}
                </div>
              </div>
            </div>

            <div style={{ display: "flex", gap: "8px", flexWrap: "wrap", borderTop: "1px solid var(--border)", paddingTop: "12px" }}>
              {!isAdmin && p.status === "ACTIVE" && (
                <button
                  onClick={() => handleSelectPlan(p.id)}
                  className="btn"
                  style={{ width: "100%" }}
                >
                  {isUpgradeMode ? "Upgrade to this Plan" : "Purchase Plan"}
                </button>
              )}

              {isAdmin && (
                <>
                  <button onClick={() => handleOpenEdit(p)} className="btn btn-gray btn-sm">Edit</button>
                  <button onClick={() => handleToggleStatus(p.id)} className="btn btn-gray btn-sm">
                    {p.status === "ACTIVE" ? "Deactivate" : "Activate"}
                  </button>
                  <button onClick={() => handleDelete(p.id)} className="btn btn-danger btn-sm">Delete</button>
                </>
              )}
            </div>
          </div>
        ))}
      </div>

      {/* Modal for Admin Create/Edit */}
      {showModal && (
        <div className="modal-backdrop">
          <div className="card" style={{ width: "450px", maxWidth: "90%", maxHeight: "90vh", overflowY: "auto" }}>
            <h3 style={{ color: "#ffffff", marginBottom: "16px" }}>
              {modalMode === "create" ? "Create Membership Plan" : "Edit Membership Plan"}
            </h3>
            <form onSubmit={handleSave}>
              <div style={{ marginBottom: "12px" }}>
                <label>Plan Name</label>
                <input
                  type="text"
                  required
                  value={formData.name}
                  onChange={(e) => setFormData({ ...formData, name: e.target.value })}
                  placeholder="e.g. Platinum Annual"
                />
              </div>

              <div style={{ display: "grid", gridTemplateColumns: "1fr 1fr", gap: "12px", marginBottom: "12px" }}>
                <div>
                  <label>Duration (Months)</label>
                  <input
                    type="number"
                    min="1"
                    required
                    value={formData.durationMonths}
                    onChange={(e) => setFormData({ ...formData, durationMonths: parseInt(e.target.value) })}
                  />
                </div>
                <div>
                  <label>Price (Rs.)</label>
                  <input
                    type="number"
                    step="0.01"
                    required
                    value={formData.price}
                    onChange={(e) => setFormData({ ...formData, price: e.target.value })}
                  />
                </div>
              </div>

              <div style={{ marginBottom: "12px" }}>
                <label>Description</label>
                <textarea
                  rows="2"
                  value={formData.description}
                  onChange={(e) => setFormData({ ...formData, description: e.target.value })}
                />
              </div>

              <div style={{ marginBottom: "12px" }}>
                <label>Benefits (comma separated)</label>
                <textarea
                  rows="2"
                  value={formData.benefits}
                  onChange={(e) => setFormData({ ...formData, benefits: e.target.value })}
                  placeholder="24/7 Access, Sauna, 1 Free Training Session"
                />
              </div>

              <div style={{ marginBottom: "16px" }}>
                <label>Status</label>
                <select
                  value={formData.status}
                  onChange={(e) => setFormData({ ...formData, status: e.target.value })}
                >
                  <option value="ACTIVE">ACTIVE</option>
                  <option value="INACTIVE">INACTIVE</option>
                </select>
              </div>

              <div style={{ display: "flex", justifyContent: "flex-end", gap: "10px" }}>
                <button type="button" onClick={() => setShowModal(false)} className="btn btn-gray">
                  Cancel
                </button>
                <button type="submit" className="btn">
                  Save Plan
                </button>
              </div>
            </form>
          </div>
        </div>
      )}
    </div>
  );
};

export default Plans;
