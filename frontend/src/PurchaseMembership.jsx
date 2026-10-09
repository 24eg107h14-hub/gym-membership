import { useState, useEffect } from "react";
import { Link, useNavigate, useSearchParams } from "react-router-dom";
import { planApi, couponApi, membershipApi } from "./services/apiServices.js";

const PurchaseMembership = () => {
  const [plans, setPlans] = useState([]);
  const [selectedPlanId, setSelectedPlanId] = useState("");
  const [couponCode, setCouponCode] = useState("");
  const [paymentMethod, setPaymentMethod] = useState("UPI");
  const [startDate, setStartDate] = useState(new Date().toISOString().split("T")[0]);

  const [appliedCoupon, setAppliedCoupon] = useState(null);
  const [couponError, setCouponError] = useState("");
  const [loading, setLoading] = useState(true);
  const [submitting, setSubmitting] = useState(false);
  const [error, setError] = useState("");

  const navigate = useNavigate();
  const [searchParams] = useSearchParams();
  const preSelectedPlanId = searchParams.get("planId");

  useEffect(() => {
    const fetchData = async () => {
      setLoading(true);
      try {
        const planRes = await planApi.getAll(false);
        setPlans(planRes.data.filter((p) => p.status === "ACTIVE"));

        if (preSelectedPlanId) {
          setSelectedPlanId(preSelectedPlanId);
        } else if (planRes.data.length > 0) {
          setSelectedPlanId(planRes.data[0].id.toString());
        }
      } catch (err) {
        setError("Failed to load membership plans.");
      } finally {
        setLoading(false);
      }
    };

    fetchData();
  }, [preSelectedPlanId]);

  const selectedPlan = plans.find((p) => p.id.toString() === selectedPlanId.toString());
  const planPrice = selectedPlan ? parseFloat(selectedPlan.price) : 0;
  const discountAmount = appliedCoupon ? parseFloat(appliedCoupon.discountAmount) : 0;
  const finalAmount = Math.max(0, planPrice - discountAmount);

  const handleApplyCoupon = async () => {
    if (!couponCode.trim()) return;
    setCouponError("");
    try {
      const res = await couponApi.validate(couponCode.trim(), planPrice);
      if (res.data.valid) {
        setAppliedCoupon(res.data);
      } else {
        setCouponError(res.data.message || "Invalid coupon");
        setAppliedCoupon(null);
      }
    } catch (err) {
      setCouponError(err.response?.data?.message || "Failed to validate coupon");
      setAppliedCoupon(null);
    }
  };

  const handleRemoveCoupon = () => {
    setAppliedCoupon(null);
    setCouponCode("");
    setCouponError("");
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    if (!selectedPlanId) {
      alert("Please select a membership plan");
      return;
    }

    setSubmitting(true);
    setError("");

    try {
      await membershipApi.purchase({
        planId: parseInt(selectedPlanId),
        couponCode: appliedCoupon ? appliedCoupon.couponCode : null,
        startDate: startDate,
        paymentMethod: paymentMethod,
      });

      alert("Membership successfully purchased and activated!");
      navigate("/my-membership");
    } catch (err) {
      setError(err.response?.data?.message || "Purchase failed. Please try again.");
    } finally {
      setSubmitting(false);
    }
  };

  if (loading) {
    return <div style={{ color: "var(--text-muted)", padding: "20px 0" }}>Loading checkout...</div>;
  }

  return (
    <div style={{ maxWidth: "600px", margin: "0 auto" }}>
      <h2 className="page-title">Purchase Gym Membership</h2>

      {error && <div className="error-text">{error}</div>}

      <div className="card">
        <form onSubmit={handleSubmit}>
          {/* Plan Selection */}
          <div style={{ marginBottom: "16px" }}>
            <label>Select Membership Plan</label>
            <select
              value={selectedPlanId}
              onChange={(e) => {
                setSelectedPlanId(e.target.value);
                setAppliedCoupon(null); // Reset coupon on plan change
              }}
              required
            >
              {plans.map((p) => (
                <option key={p.id} value={p.id}>
                  {p.name} — Rs. {p.price} ({p.durationMonths} Month(s))
                </option>
              ))}
            </select>
          </div>

          {/* No branch to pick: one membership, every location. */}
          <p className="field-hint" style={{ marginBottom: "16px" }}>
            No branch to choose — this membership works at every gym location.{" "}
            <Link to="/branches">Find the gym nearest to you →</Link>
          </p>

          {/* Start Date */}
          <div style={{ marginBottom: "16px" }}>
            <label>Membership Start Date</label>
            <input
              type="date"
              value={startDate}
              min={new Date().toISOString().split("T")[0]}
              onChange={(e) => setStartDate(e.target.value)}
              required
            />
          </div>

          {/* Coupon Code Box */}
          <div style={{ marginBottom: "16px" }}>
            <label>Apply Discount Coupon</label>
            <div style={{ display: "flex", gap: "8px" }}>
              <input
                type="text"
                value={couponCode}
                onChange={(e) => setCouponCode(e.target.value.toUpperCase())}
                placeholder="e.g. WELCOME10"
                disabled={!!appliedCoupon}
              />
              {appliedCoupon ? (
                <button type="button" onClick={handleRemoveCoupon} className="btn btn-danger btn-sm">
                  Remove
                </button>
              ) : (
                <button type="button" onClick={handleApplyCoupon} className="btn btn-gray btn-sm">
                  Apply
                </button>
              )}
            </div>
            {couponError && <div style={{ color: "var(--danger)", fontSize: "13px", marginTop: "4px" }}>{couponError}</div>}
            {appliedCoupon && (
              <div style={{ color: "var(--success)", fontSize: "13px", marginTop: "4px" }}>
                ✓ Coupon Applied: {appliedCoupon.message} (-Rs. {discountAmount})
              </div>
            )}
          </div>

          {/* Payment Method */}
          <div style={{ marginBottom: "20px" }}>
            <label>Payment Method</label>
            <select
              value={paymentMethod}
              onChange={(e) => setPaymentMethod(e.target.value)}
            >
              <option value="UPI">UPI (Google Pay / PhonePe / Paytm)</option>
              <option value="CARD">Credit / Debit Card</option>
              <option value="NET_BANKING">Net Banking</option>
              <option value="CASH">Cash at Desk</option>
            </select>
          </div>

          {/* Price Summary Breakdown */}
          <div style={{ background: "var(--surface-2)", padding: "16px", borderRadius: "6px", marginBottom: "20px" }}>
            <div style={{ display: "flex", justifyContent: "space-between", marginBottom: "8px", fontSize: "14px" }}>
              <span>Plan Price:</span>
              <span>Rs. {planPrice.toFixed(2)}</span>
            </div>
            <div style={{ display: "flex", justifyContent: "space-between", marginBottom: "8px", fontSize: "14px" }}>
              <span>Branch Access:</span>
              <span>All branches</span>
            </div>
            {appliedCoupon && (
              <div style={{ display: "flex", justifyContent: "space-between", marginBottom: "8px", fontSize: "14px", color: "var(--success)" }}>
                <span>Discount ({appliedCoupon.couponCode}):</span>
                <span>- Rs. {discountAmount.toFixed(2)}</span>
              </div>
            )}
            <div style={{ display: "flex", justifyContent: "space-between", borderTop: "1px solid var(--border)", paddingTop: "8px", fontWeight: "bold", fontSize: "18px", color: "var(--accent)" }}>
              <span>Final Total:</span>
              <span>Rs. {finalAmount.toFixed(2)}</span>
            </div>
          </div>

          <button
            type="submit"
            disabled={submitting}
            className="btn"
            style={{ width: "100%", padding: "12px", fontSize: "16px" }}
          >
            {submitting ? "Processing Activation..." : `Confirm & Pay Rs. ${finalAmount.toFixed(2)}`}
          </button>
        </form>
      </div>
    </div>
  );
};

export default PurchaseMembership;
