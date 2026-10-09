import { useState, useEffect } from "react";
import { couponApi } from "./services/apiServices.js";
import StatusBadge from "./StatusBadge.jsx";

const Coupons = () => {
  const [coupons, setCoupons] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState("");
  const [showModal, setShowModal] = useState(false);
  const [modalMode, setModalMode] = useState("create");
  const [selectedCoupon, setSelectedCoupon] = useState(null);

  const [formData, setFormData] = useState({
    code: "",
    description: "",
    discountType: "PERCENTAGE",
    discountValue: "",
    active: true,
    startDate: new Date().toISOString().split("T")[0],
    endDate: new Date(Date.now() + 90 * 24 * 60 * 60 * 1000).toISOString().split("T")[0],
  });

  const fetchCoupons = async () => {
    setLoading(true);
    try {
      const res = await couponApi.getAll();
      setCoupons(res.data);
      setError("");
    } catch (err) {
      setError("Failed to load coupons.");
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchCoupons();
  }, []);

  const handleOpenCreate = () => {
    setFormData({
      code: "",
      description: "",
      discountType: "PERCENTAGE",
      discountValue: "",
      active: true,
      startDate: new Date().toISOString().split("T")[0],
      endDate: new Date(Date.now() + 90 * 24 * 60 * 60 * 1000).toISOString().split("T")[0],
    });
    setModalMode("create");
    setShowModal(true);
  };

  const handleOpenEdit = (c) => {
    setSelectedCoupon(c);
    setFormData({
      code: c.code,
      description: c.description || "",
      discountType: c.discountType,
      discountValue: c.discountValue,
      active: c.active !== undefined ? c.active : true,
      startDate: c.startDate || new Date().toISOString().split("T")[0],
      endDate: c.endDate || new Date(Date.now() + 90 * 24 * 60 * 60 * 1000).toISOString().split("T")[0],
    });
    setModalMode("edit");
    setShowModal(true);
  };

  const handleSave = async (e) => {
    e.preventDefault();
    try {
      if (modalMode === "create") {
        await couponApi.create(formData);
      } else {
        await couponApi.update(selectedCoupon.id, formData);
      }
      setShowModal(false);
      fetchCoupons();
    } catch (err) {
      alert(err.response?.data?.message || "Failed to save coupon");
    }
  };

  const handleToggleStatus = async (id) => {
    try {
      await couponApi.toggleStatus(id);
      fetchCoupons();
    } catch (err) {
      alert("Failed to toggle coupon status");
    }
  };

  const handleDelete = async (id) => {
    if (!window.confirm("Delete this promotional coupon?")) return;
    try {
      await couponApi.delete(id);
      fetchCoupons();
    } catch (err) {
      alert("Failed to delete coupon");
    }
  };

  if (loading) {
    return <div style={{ color: "var(--text-muted)", padding: "20px 0" }}>Loading coupons...</div>;
  }

  return (
    <div>
      <div style={{ display: "flex", justifyContent: "space-between", alignItems: "center", marginBottom: "20px" }}>
        <h2 className="page-title" style={{ margin: 0 }}>Discount Coupons</h2>
        <button onClick={handleOpenCreate} className="btn">
          + Create Coupon
        </button>
      </div>

      {error && <div className="error-text">{error}</div>}

      <div className="card">
        <div className="table-wrapper">
          <table>
            <thead>
              <tr>
                <th>Code</th>
                <th>Description</th>
                <th>Discount</th>
                <th>Validity</th>
                <th>Status</th>
                <th style={{ textAlign: "right" }}>Actions</th>
              </tr>
            </thead>
            <tbody>
              {coupons.length === 0 ? (
                <tr>
                  <td colSpan="6" className="empty-state">
                    No promo coupons created yet.
                  </td>
                </tr>
              ) : (
                coupons.map((c) => (
                  <tr key={c.id}>
                    <td style={{ fontWeight: "bold", color: "var(--accent)" }}>{c.code}</td>
                    <td>{c.description}</td>
                    <td style={{ fontWeight: "bold" }}>
                      {c.discountType === "PERCENTAGE" ? `${c.discountValue}% OFF` : `Rs. ${c.discountValue} FLAT`}
                    </td>
                    <td>{c.startDate} to {c.endDate}</td>
                    <td>
                      <StatusBadge status={c.active ? "ACTIVE" : "INACTIVE"} />
                    </td>
                    <td style={{ textAlign: "right" }}>
                      <div className="table-actions">
                        <button onClick={() => handleOpenEdit(c)} className="btn btn-gray btn-sm">Edit</button>
                        <button onClick={() => handleToggleStatus(c.id)} className="btn btn-gray btn-sm">
                          {c.active ? "Deactivate" : "Activate"}
                        </button>
                        <button onClick={() => handleDelete(c.id)} className="btn btn-danger btn-sm">Delete</button>
                      </div>
                    </td>
                  </tr>
                ))
              )}
            </tbody>
          </table>
        </div>
      </div>

      {/* Modal for Coupon Form */}
      {showModal && (
        <div className="modal-backdrop">
          <div className="card" style={{ width: "450px", maxWidth: "90%" }}>
            <h3 style={{ color: "#ffffff", marginBottom: "16px" }}>
              {modalMode === "create" ? "Create Discount Coupon" : "Edit Discount Coupon"}
            </h3>
            <form onSubmit={handleSave}>
              <div style={{ marginBottom: "12px" }}>
                <label>Coupon Code (Upper Case)</label>
                <input
                  type="text"
                  required
                  value={formData.code}
                  onChange={(e) => setFormData({ ...formData, code: e.target.value.toUpperCase() })}
                  placeholder="e.g. SUMMER25"
                />
              </div>

              <div style={{ marginBottom: "12px" }}>
                <label>Description</label>
                <input
                  type="text"
                  value={formData.description}
                  onChange={(e) => setFormData({ ...formData, description: e.target.value })}
                  placeholder="e.g. 25% discount on quarterly plans"
                />
              </div>

              <div style={{ display: "grid", gridTemplateColumns: "1fr 1fr", gap: "12px", marginBottom: "12px" }}>
                <div>
                  <label>Discount Type</label>
                  <select
                    value={formData.discountType}
                    onChange={(e) => setFormData({ ...formData, discountType: e.target.value })}
                  >
                    <option value="PERCENTAGE">PERCENTAGE (%)</option>
                    <option value="FIXED">FIXED AMOUNT (Rs.)</option>
                  </select>
                </div>
                <div>
                  <label>Discount Value</label>
                  <input
                    type="number"
                    step="0.01"
                    required
                    value={formData.discountValue}
                    onChange={(e) => setFormData({ ...formData, discountValue: e.target.value })}
                  />
                </div>
              </div>

              <div style={{ display: "grid", gridTemplateColumns: "1fr 1fr", gap: "12px", marginBottom: "16px" }}>
                <div>
                  <label>Start Date</label>
                  <input
                    type="date"
                    required
                    value={formData.startDate}
                    onChange={(e) => setFormData({ ...formData, startDate: e.target.value })}
                  />
                </div>
                <div>
                  <label>Expiry Date</label>
                  <input
                    type="date"
                    required
                    value={formData.endDate}
                    onChange={(e) => setFormData({ ...formData, endDate: e.target.value })}
                  />
                </div>
              </div>

              <div style={{ display: "flex", justifyContent: "flex-end", gap: "10px" }}>
                <button type="button" onClick={() => setShowModal(false)} className="btn btn-gray">
                  Cancel
                </button>
                <button type="submit" className="btn">
                  Save Coupon
                </button>
              </div>
            </form>
          </div>
        </div>
      )}
    </div>
  );
};

export default Coupons;
