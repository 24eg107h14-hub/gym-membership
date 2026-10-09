import { useState, useEffect } from "react";
import { branchApi } from "./services/apiServices.js";
import BranchLocator from "./BranchLocator.jsx";
import { STATE_NAMES, getCitiesForState } from "./data/indiaLocations.js";

const EMPTY_FORM = {
  name: "",
  address: "",
  city: "",
  state: "",
  phone: "",
  email: "",
  active: true,
};

const Branches = () => {
  const [branches, setBranches] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState("");
  const [showModal, setShowModal] = useState(false);
  const [modalMode, setModalMode] = useState("create");
  const [selectedBranch, setSelectedBranch] = useState(null);

  const [formData, setFormData] = useState(EMPTY_FORM);

  const role = localStorage.getItem("role");
  const isAdmin = role === "ADMIN";

  // Keep the values already stored on a branch selectable even if they are not
  // part of the India list (e.g. data created before this change).
  const stateOptions = Array.from(new Set([...STATE_NAMES, formData.state].filter(Boolean))).sort((a, b) =>
    a.localeCompare(b)
  );
  const cityOptions = Array.from(
    new Set([...getCitiesForState(formData.state), formData.city].filter(Boolean))
  ).sort((a, b) => a.localeCompare(b));

  const fetchBranches = async () => {
    setLoading(true);
    try {
      const res = await branchApi.getAll();
      setBranches(res.data);
      setError("");
    } catch (err) {
      setError("Failed to load branches.");
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchBranches();
  }, []);

  const handleOpenCreate = () => {
    setFormData(EMPTY_FORM);
    setModalMode("create");
    setShowModal(true);
  };

  const handleOpenEdit = (branch) => {
    setSelectedBranch(branch);
    setFormData({
      name: branch.name,
      address: branch.address || "",
      city: branch.city || "",
      state: branch.state || "",
      phone: branch.phone || "",
      email: branch.email || "",
      active: branch.active !== undefined ? branch.active : true,
    });
    setModalMode("edit");
    setShowModal(true);
  };

  const handleSave = async (e) => {
    e.preventDefault();
    try {
      if (modalMode === "create") {
        await branchApi.create(formData);
      } else {
        await branchApi.update(selectedBranch.id, formData);
      }
      setShowModal(false);
      fetchBranches();
    } catch (err) {
      alert(err.response?.data?.message || "Failed to save branch");
    }
  };

  const handleToggleStatus = async (id) => {
    try {
      await branchApi.toggleStatus(id);
      fetchBranches();
    } catch (err) {
      alert("Failed to toggle branch status");
    }
  };

  const handleDelete = async (id) => {
    if (!window.confirm("Are you sure you want to delete this branch?")) return;
    try {
      await branchApi.delete(id);
      fetchBranches();
    } catch (err) {
      alert("Failed to delete branch");
    }
  };

  if (loading) {
    return <div style={{ color: "var(--text-muted)", padding: "20px 0" }}>Loading branches...</div>;
  }

  return (
    <div>
      <div style={{ display: "flex", justifyContent: "space-between", alignItems: "center", marginBottom: "20px" }}>
        <h2 className="page-title" style={{ margin: 0 }}>Gym Branches</h2>
        {isAdmin && (
          <button onClick={handleOpenCreate} className="btn">
            + Add New Branch
          </button>
        )}
      </div>

      {error && <div className="error-text">{error}</div>}

      <div className="card" style={{ marginBottom: "20px" }}>
        <BranchLocator
          branches={branches}
          emptyMessage="No branches match this location yet."
          renderItem={(b) => (
            <>
              <div className="branch-option-top">
                <h3 style={{ margin: 0, fontSize: "18px", color: "var(--accent)" }}>{b.name}</h3>
                <span className={b.active ? "branch-active-pill" : "branch-inactive-pill"}>
                  {b.active ? "Active" : "Inactive"}
                </span>
              </div>
              <p style={{ color: "var(--text)", fontSize: "14px", margin: "6px 0" }}>
                📍 {[b.address, b.city, b.state].filter(Boolean).join(", ")}
              </p>
              <div style={{ fontSize: "13px", color: "var(--text-muted)", marginTop: "8px" }}>
                <div>📞 {b.phone || "N/A"}</div>
                <div>✉️ {b.email || "N/A"}</div>
              </div>

              {isAdmin && (
                <div
                  style={{
                    display: "flex",
                    gap: "8px",
                    borderTop: "1px solid var(--border)",
                    paddingTop: "12px",
                    marginTop: "16px",
                  }}
                >
                  <button onClick={() => handleOpenEdit(b)} className="btn btn-gray btn-sm">Edit</button>
                  <button onClick={() => handleToggleStatus(b.id)} className="btn btn-gray btn-sm">
                    {b.active ? "Deactivate" : "Activate"}
                  </button>
                  <button onClick={() => handleDelete(b.id)} className="btn btn-danger btn-sm">Delete</button>
                </div>
              )}
            </>
          )}
        />
      </div>

      {/* Modal for Admin Create/Edit Branch */}
      {showModal && (
        <div className="modal-backdrop">
          <div className="card" style={{ width: "450px", maxWidth: "90%" }}>
            <h3 style={{ color: "#ffffff", marginBottom: "16px" }}>
              {modalMode === "create" ? "Add Gym Branch" : "Edit Gym Branch"}
            </h3>
            <form onSubmit={handleSave}>
              <div style={{ marginBottom: "12px" }}>
                <label>Branch Name</label>
                <input
                  type="text"
                  required
                  value={formData.name}
                  onChange={(e) => setFormData({ ...formData, name: e.target.value })}
                  placeholder="e.g. Bandra West Fitness Hub"
                />
              </div>

              <div style={{ marginBottom: "12px" }}>
                <label>Address</label>
                <input
                  type="text"
                  required
                  value={formData.address}
                  onChange={(e) => setFormData({ ...formData, address: e.target.value })}
                  placeholder="e.g. Shop 12, Linking Road"
                />
              </div>

              <div style={{ display: "grid", gridTemplateColumns: "1fr 1fr", gap: "12px", marginBottom: "12px" }}>
                <div>
                  <label>State</label>
                  <select
                    required
                    value={formData.state}
                    onChange={(e) => setFormData({ ...formData, state: e.target.value, city: "" })}
                  >
                    <option value="">Select state</option>
                    {stateOptions.map((stateName) => (
                      <option key={stateName} value={stateName}>
                        {stateName}
                      </option>
                    ))}
                  </select>
                </div>
                <div>
                  <label>City</label>
                  <select
                    required
                    disabled={!formData.state}
                    value={formData.city}
                    onChange={(e) => setFormData({ ...formData, city: e.target.value })}
                  >
                    <option value="">{formData.state ? "Select city" : "Select a state first"}</option>
                    {cityOptions.map((cityName) => (
                      <option key={cityName} value={cityName}>
                        {cityName}
                      </option>
                    ))}
                  </select>
                </div>
              </div>

              <div style={{ display: "grid", gridTemplateColumns: "1fr 1fr", gap: "12px", marginBottom: "16px" }}>
                <div>
                  <label>Contact Phone</label>
                  <input
                    type="tel"
                    value={formData.phone}
                    onChange={(e) => setFormData({ ...formData, phone: e.target.value })}
                    placeholder="+91 98765 43210"
                  />
                </div>
                <div>
                  <label>Branch Email</label>
                  <input
                    type="email"
                    value={formData.email}
                    onChange={(e) => setFormData({ ...formData, email: e.target.value })}
                  />
                </div>
              </div>

              <div style={{ display: "flex", justifyContent: "flex-end", gap: "10px" }}>
                <button type="button" onClick={() => setShowModal(false)} className="btn btn-gray">
                  Cancel
                </button>
                <button type="submit" className="btn">
                  Save Branch
                </button>
              </div>
            </form>
          </div>
        </div>
      )}
    </div>
  );
};

export default Branches;
