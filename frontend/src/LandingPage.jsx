import { useState, useEffect } from "react";
import { Link, useNavigate } from "react-router-dom";
import axios from "axios";

const API_BASE_URL =
  import.meta.env.VITE_API_BASE_URL || "http://localhost:8080/api";

const LandingPage = () => {
  const navigate = useNavigate();
  const token = localStorage.getItem("token");
  const role = localStorage.getItem("role");
  const name = localStorage.getItem("name");

  const [plans, setPlans] = useState([]);
  const [branches, setBranches] = useState([]);
  const [locQuery, setLocQuery] = useState("");
  const [locState, setLocState] = useState("");
  const [locCity, setLocCity] = useState("");

  useEffect(() => {
    const fetchPublicData = async () => {
      try {
        const res = await axios.get(`${API_BASE_URL}/plans`);
        if (Array.isArray(res.data) && res.data.length > 0) {
          setPlans(res.data.filter((p) => p.status === "ACTIVE"));
        }
      } catch (err) {
        setPlans([
          {
            id: 1,
            name: "Silver Monthly",
            price: 1499,
            durationMonths: 1,
            description: "Essential gym access for fitness enthusiasts looking for month-to-month flexibility.",
            benefits: "Full gym floor access, Locker room & showers, 1 complimentary trainer assessment",
          },
          {
            id: 2,
            name: "Gold Quarterly",
            price: 3999,
            durationMonths: 3,
            description: "Our most popular membership plan for dedicated fitness transformations.",
            benefits: "All Silver benefits, Free cardio & yoga group classes, Diet consultation, 1 Guest pass per month",
          },
          {
            id: 3,
            name: "Platinum Annual",
            price: 12999,
            durationMonths: 12,
            description: "VIP access across all gym branches with personalized personal trainer sessions.",
            benefits: "Multi-branch unlimited access, 12 Personal trainer sessions, Spa & sauna access, Free gym merchandise",
          },
        ]);
      }

      try {
        const resB = await axios.get(`${API_BASE_URL}/branches`);
        if (Array.isArray(resB.data) && resB.data.length > 0) {
          setBranches(resB.data.filter((b) => b.active !== false));
        }
      } catch (err) {
        setBranches([
          {
            id: 1,
            name: "Downtown Fitness Hub",
            address: "Shop 12, Linking Road, Bandra West",
            city: "Mumbai",
            state: "Maharashtra",
            phone: "+91 98200 11223",
          },
          {
            id: 2,
            name: "Westside Athletic Club",
            address: "4th Floor, Phoenix Marketcity, Viman Nagar",
            city: "Pune",
            state: "Maharashtra",
            phone: "+91 98200 11224",
          },
        ]);
      }
    };

    fetchPublicData();
  }, []);

  const locStates = Array.from(new Set(branches.map((b) => (b.state || "").trim()).filter(Boolean))).sort();
  const locCities = Array.from(
    new Set(
      branches
        .filter((b) => !locState || (b.state || "").trim() === locState)
        .map((b) => (b.city || "").trim())
        .filter(Boolean)
    )
  ).sort();
  const hasLocFilter = Boolean(locQuery.trim() || locState || locCity);
  const visibleBranches = branches.filter((b) => {
    if (locState && (b.state || "").trim() !== locState) return false;
    if (locCity && (b.city || "").trim() !== locCity) return false;
    const q = locQuery.trim().toLowerCase();
    if (!q) return true;
    return [b.name, b.address, b.city, b.state]
      .filter(Boolean)
      .some((value) => String(value).toLowerCase().includes(q));
  });
  const clearLocFilters = () => {
    setLocQuery("");
    setLocState("");
    setLocCity("");
  };

  return (
    <div style={{ backgroundColor: "#0a0a0a", color: "#f5f5f5", minHeight: "100vh", fontFamily: "Inter, system-ui, sans-serif" }}>
      {/* Top Header / Public Nav */}
      <header
        style={{
          display: "flex",
          justifyContent: "space-between",
          alignItems: "center",
          padding: "16px 32px",
          borderBottom: "2px solid #f97316",
          backgroundColor: "#000000",
          position: "sticky",
          top: 0,
          zIndex: 100,
        }}
      >
        <div style={{ display: "flex", alignItems: "center", gap: "10px", cursor: "pointer" }} onClick={() => navigate("/")}>
          <span style={{ fontSize: "24px" }}>🏋️‍♂️</span>
          <span style={{ color: "#f97316", fontSize: "22px", fontWeight: "900", letterSpacing: "1px" }}>
            GYM MANAGER
          </span>
        </div>

        <nav style={{ display: "flex", gap: "24px", alignItems: "center" }}>
          <a href="#features" style={{ color: "#f5f5f5", textDecoration: "none", fontSize: "14px", fontWeight: "500" }}>Features</a>
          <a href="#plans" style={{ color: "#f5f5f5", textDecoration: "none", fontSize: "14px", fontWeight: "500" }}>Plans</a>
          <a href="#branches" style={{ color: "#f5f5f5", textDecoration: "none", fontSize: "14px", fontWeight: "500" }}>Locations</a>
          <a href="#why-us" style={{ color: "#f5f5f5", textDecoration: "none", fontSize: "14px", fontWeight: "500" }}>About</a>
        </nav>

        <div style={{ display: "flex", gap: "12px", alignItems: "center" }}>
          {token ? (
            <button
              onClick={() => navigate("/dashboard")}
              style={{
                backgroundColor: "#f97316",
                color: "#000000",
                fontWeight: "bold",
                border: "none",
                borderRadius: "6px",
                padding: "8px 16px",
                cursor: "pointer",
                fontSize: "14px",
              }}
            >
              Go to Dashboard ({name || role}) →
            </button>
          ) : (
            <>
              <Link
                to="/login"
                style={{
                  color: "#f5f5f5",
                  border: "1px solid #404040",
                  padding: "8px 16px",
                  borderRadius: "6px",
                  textDecoration: "none",
                  fontSize: "14px",
                  fontWeight: "600",
                }}
              >
                Sign In
              </Link>
              <Link
                to="/register"
                style={{
                  backgroundColor: "#f97316",
                  color: "#000000",
                  padding: "8px 18px",
                  borderRadius: "6px",
                  textDecoration: "none",
                  fontSize: "14px",
                  fontWeight: "bold",
                }}
              >
                Join Now
              </Link>
            </>
          )}
        </div>
      </header>

      {/* Hero Section */}
      <section
        style={{
          padding: "80px 24px",
          textAlign: "center",
          maxWidth: "1000px",
          margin: "0 auto",
          position: "relative",
        }}
      >
        <div
          style={{
            display: "inline-block",
            backgroundColor: "rgba(249, 115, 22, 0.15)",
            color: "#f97316",
            padding: "6px 16px",
            borderRadius: "20px",
            fontSize: "13px",
            fontWeight: "700",
            letterSpacing: "1px",
            marginBottom: "20px",
            border: "1px solid rgba(249, 115, 22, 0.3)",
          }}
        >
          ⚡ NEXT GENERATION FITNESS PLATFORM
        </div>

        <h1
          style={{
            fontSize: "48px",
            lineHeight: "1.2",
            fontWeight: "900",
            marginBottom: "20px",
            color: "#ffffff",
            letterSpacing: "-0.5px",
          }}
        >
          TRANSFORM YOUR BODY, <br />
          <span style={{ color: "#f97316" }}>UNLEASH YOUR TRUE POTENTIAL</span>
        </h1>

        <p
          style={{
            fontSize: "18px",
            color: "#a3a3a3",
            lineHeight: "1.6",
            maxWidth: "750px",
            margin: "0 auto 36px",
          }}
        >
          Experience world-class gym facilities, certified fitness trainers, flexible membership plans, instant online enrollment, and 24/7 member support.
        </p>

        <div style={{ display: "flex", justifyContent: "center", gap: "16px", flexWrap: "wrap", marginBottom: "50px" }}>
          <Link
            to="/register"
            style={{
              backgroundColor: "#f97316",
              color: "#000000",
              fontWeight: "bold",
              fontSize: "16px",
              padding: "14px 32px",
              borderRadius: "8px",
              textDecoration: "none",
              boxShadow: "0 4px 14px rgba(249, 115, 22, 0.4)",
            }}
          >
            Start Your Fitness Journey →
          </Link>
          <a
            href="#plans"
            style={{
              backgroundColor: "#262626",
              color: "#ffffff",
              fontWeight: "600",
              fontSize: "16px",
              padding: "14px 28px",
              borderRadius: "8px",
              textDecoration: "none",
              border: "1px solid #404040",
            }}
          >
            Explore Membership Plans
          </a>
        </div>

        {/* Quick Metric Badges */}
        <div
          style={{
            display: "grid",
            gridTemplateColumns: "repeat(auto-fit, minmax(180px, 1fr))",
            gap: "16px",
            background: "#171717",
            padding: "24px",
            borderRadius: "12px",
            border: "1px solid #333333",
          }}
        >
          <div>
            <div style={{ fontSize: "28px", fontWeight: "bold", color: "#f97316" }}>5+</div>
            <div style={{ fontSize: "13px", color: "#a3a3a3" }}>Modern Gym Locations</div>
          </div>
          <div>
            <div style={{ fontSize: "28px", fontWeight: "bold", color: "#f97316" }}>50+</div>
            <div style={{ fontSize: "13px", color: "#a3a3a3" }}>Certified Fitness Coaches</div>
          </div>
          <div>
            <div style={{ fontSize: "28px", fontWeight: "bold", color: "#f97316" }}>10,000+</div>
            <div style={{ fontSize: "13px", color: "#a3a3a3" }}>Active Happy Members</div>
          </div>
          <div>
            <div style={{ fontSize: "28px", fontWeight: "bold", color: "#f97316" }}>24 / 7</div>
            <div style={{ fontSize: "13px", color: "#a3a3a3" }}>Online Support Desk</div>
          </div>
        </div>
      </section>

      {/* Features Section */}
      <section id="features" style={{ padding: "60px 24px", backgroundColor: "#111111" }}>
        <div style={{ maxWidth: "1100px", margin: "0 auto" }}>
          <div style={{ textAlign: "center", marginBottom: "48px" }}>
            <h2 style={{ fontSize: "32px", color: "#ffffff", marginBottom: "12px" }}>
              Why Train With <span style={{ color: "#f97316" }}>Gym Manager</span>?
            </h2>
            <p style={{ color: "#a3a3a3", fontSize: "16px" }}>Everything you need to build muscle, lose weight, and maintain peak wellness.</p>
          </div>

          <div style={{ display: "grid", gridTemplateColumns: "repeat(auto-fit, minmax(280px, 1fr))", gap: "24px" }}>
            <div style={{ background: "#1a1a1a", border: "1px solid #333333", borderRadius: "10px", padding: "28px" }}>
              <div style={{ fontSize: "32px", marginBottom: "16px" }}>💪</div>
              <h3 style={{ fontSize: "18px", color: "#ffffff", marginBottom: "10px" }}>State-of-the-Art Equipment</h3>
              <p style={{ color: "#a3a3a3", fontSize: "14px", lineHeight: "1.6" }}>
                Top-tier Olympic free weights, plate-loaded machines, functional rigs, and high-tech cardio setups.
              </p>
            </div>

            <div style={{ background: "#1a1a1a", border: "1px solid #333333", borderRadius: "10px", padding: "28px" }}>
              <div style={{ fontSize: "32px", marginBottom: "16px" }}>💳</div>
              <h3 style={{ fontSize: "18px", color: "#ffffff", marginBottom: "10px" }}>Instant Digital Memberships</h3>
              <p style={{ color: "#a3a3a3", fontSize: "14px", lineHeight: "1.6" }}>
                Choose your plan, find the gym branch closest to you, apply promo coupons, and get activated immediately.
              </p>
            </div>

            <div style={{ background: "#1a1a1a", border: "1px solid #333333", borderRadius: "10px", padding: "28px" }}>
              <div style={{ fontSize: "32px", marginBottom: "16px" }}>🔄</div>
              <h3 style={{ fontSize: "18px", color: "#ffffff", marginBottom: "10px" }}>Flexible Freeze & Upgrades</h3>
              <p style={{ color: "#a3a3a3", fontSize: "14px", lineHeight: "1.6" }}>
                Going on vacation? Freeze your membership anytime. Ready for more benefits? Upgrade with a single click.
              </p>
            </div>

            <div style={{ background: "#1a1a1a", border: "1px solid #333333", borderRadius: "10px", padding: "28px" }}>
              <div style={{ fontSize: "32px", marginBottom: "16px" }}>🧾</div>
              <h3 style={{ fontSize: "18px", color: "#ffffff", marginBottom: "10px" }}>Tax Invoices & Receipts</h3>
              <p style={{ color: "#a3a3a3", fontSize: "14px", lineHeight: "1.6" }}>
                Instant access to digital GST invoices, payment history records, and printable receipts for every transaction.
              </p>
            </div>

            <div style={{ background: "#1a1a1a", border: "1px solid #333333", borderRadius: "10px", padding: "28px" }}>
              <div style={{ fontSize: "32px", marginBottom: "16px" }}>📍</div>
              <h3 style={{ fontSize: "18px", color: "#ffffff", marginBottom: "10px" }}>Multi-Branch Facilities</h3>
              <p style={{ color: "#a3a3a3", fontSize: "14px", lineHeight: "1.6" }}>
                Convenient locations across the city so you never miss a workout, whether near home or your workplace.
              </p>
            </div>

            <div style={{ background: "#1a1a1a", border: "1px solid #333333", borderRadius: "10px", padding: "28px" }}>
              <div style={{ fontSize: "32px", marginBottom: "16px" }}>🎧</div>
              <h3 style={{ fontSize: "18px", color: "#ffffff", marginBottom: "10px" }}>Dedicated Support Desk</h3>
              <p style={{ color: "#a3a3a3", fontSize: "14px", lineHeight: "1.6" }}>
                Have questions or need assistance? Submit support tickets and receive rapid responses from gym staff.
              </p>
            </div>
          </div>
        </div>
      </section>

      {/* Membership Plans Showcase */}
      <section id="plans" style={{ padding: "80px 24px" }}>
        <div style={{ maxWidth: "1100px", margin: "0 auto" }}>
          <div style={{ textAlign: "center", marginBottom: "48px" }}>
            <div style={{ color: "#f97316", fontWeight: "bold", fontSize: "13px", letterSpacing: "1px", textTransform: "uppercase" }}>
              AFFORDABLE FITNESS PRICING
            </div>
            <h2 style={{ fontSize: "34px", color: "#ffffff", marginTop: "8px", marginBottom: "12px" }}>
              Choose The Perfect Membership Plan
            </h2>
            <p style={{ color: "#a3a3a3", fontSize: "16px" }}>
              Transparent pricing. No hidden fees. Instant access to all gym features.
            </p>
          </div>

          <div style={{ display: "grid", gridTemplateColumns: "repeat(auto-fit, minmax(300px, 1fr))", gap: "24px" }}>
            {plans.map((p, idx) => (
              <div
                key={p.id || idx}
                style={{
                  background: idx === 1 ? "#1f1813" : "#171717",
                  border: idx === 1 ? "2px solid #f97316" : "1px solid #333333",
                  borderRadius: "12px",
                  padding: "32px 24px",
                  display: "flex",
                  flexDirection: "column",
                  position: "relative",
                }}
              >
                {idx === 1 && (
                  <div
                    style={{
                      position: "absolute",
                      top: "-12px",
                      left: "50%",
                      transform: "translateX(-50%)",
                      backgroundColor: "#f97316",
                      color: "#000000",
                      fontSize: "11px",
                      fontWeight: "900",
                      padding: "4px 12px",
                      borderRadius: "12px",
                      letterSpacing: "0.5px",
                    }}
                  >
                    MOST POPULAR
                  </div>
                )}

                <h3 style={{ fontSize: "22px", color: "#ffffff", marginBottom: "8px" }}>{p.name}</h3>
                <p style={{ color: "#a3a3a3", fontSize: "13px", minHeight: "38px", marginBottom: "20px" }}>{p.description}</p>

                <div style={{ marginBottom: "20px" }}>
                  <span style={{ fontSize: "36px", fontWeight: "900", color: "#f97316" }}>Rs. {p.price}</span>
                  <span style={{ color: "#a3a3a3", fontSize: "14px", marginLeft: "6px" }}>/ {p.durationMonths} Month(s)</span>
                </div>

                <div style={{ borderTop: "1px solid #333333", paddingTop: "16px", marginBottom: "24px", flexGrow: 1 }}>
                  <div style={{ fontSize: "12px", color: "#a3a3a3", fontWeight: "bold", textTransform: "uppercase", marginBottom: "12px" }}>
                    Included Benefits:
                  </div>
                  {p.benefits ? (
                    p.benefits.split(",").map((b, bIdx) => (
                      <div key={bIdx} style={{ display: "flex", alignItems: "center", gap: "8px", fontSize: "13px", color: "#e5e5e5", marginBottom: "8px" }}>
                        <span style={{ color: "#22c55e", fontWeight: "bold" }}>✓</span> {b.trim()}
                      </div>
                    ))
                  ) : (
                    <div style={{ fontSize: "13px", color: "#e5e5e5" }}>✓ Unlimited Gym Access</div>
                  )}
                </div>

                <button
                  onClick={() => {
                    if (token) navigate("/purchase");
                    else navigate("/register");
                  }}
                  style={{
                    backgroundColor: idx === 1 ? "#f97316" : "#262626",
                    color: idx === 1 ? "#000000" : "#ffffff",
                    fontWeight: "bold",
                    padding: "12px",
                    borderRadius: "6px",
                    border: "none",
                    cursor: "pointer",
                    fontSize: "14px",
                    width: "100%",
                  }}
                >
                  {token ? "Subscribe Now" : "Join with this Plan"}
                </button>
              </div>
            ))}
          </div>
        </div>
      </section>

      {/* Gym Branches / Locations */}
      <section id="branches" style={{ padding: "60px 24px", backgroundColor: "#111111" }}>
        <div style={{ maxWidth: "1100px", margin: "0 auto" }}>
          <div style={{ textAlign: "center", marginBottom: "40px" }}>
            <h2 style={{ fontSize: "30px", color: "#ffffff", marginBottom: "10px" }}>Find A Gym Near You</h2>
            <p style={{ color: "#a3a3a3", fontSize: "15px" }}>
              Equipped with modern facilities and certified trainers — filter by state and city to see the branches closest to you.
            </p>
          </div>

          <div
            style={{
              display: "grid",
              gridTemplateColumns: "repeat(auto-fit, minmax(200px, 1fr))",
              gap: "12px",
              background: "#171717",
              border: "1px solid #333333",
              borderRadius: "10px",
              padding: "20px",
              marginBottom: "16px",
            }}
          >
            <div>
              <label style={{ display: "block", fontSize: "12px", color: "#a3a3a3", marginBottom: "6px", textTransform: "uppercase", letterSpacing: "0.5px" }}>Search locality</label>
              <input
                type="text"
                value={locQuery}
                onChange={(e) => setLocQuery(e.target.value)}
                placeholder="e.g. Bandra, Sector 18"
                style={{ width: "100%", padding: "10px 12px", borderRadius: "6px", border: "1px solid #333333", background: "#0f0f0f", color: "#f5f5f5", fontSize: "14px" }}
              />
            </div>
            <div>
              <label style={{ display: "block", fontSize: "12px", color: "#a3a3a3", marginBottom: "6px", textTransform: "uppercase", letterSpacing: "0.5px" }}>State</label>
              <select
                value={locState}
                onChange={(e) => {
                  setLocState(e.target.value);
                  setLocCity("");
                }}
                style={{ width: "100%", padding: "10px 12px", borderRadius: "6px", border: "1px solid #333333", background: "#0f0f0f", color: "#f5f5f5", fontSize: "14px" }}
              >
                <option value="">All states</option>
                {locStates.map((stateName) => (
                  <option key={stateName} value={stateName}>{stateName}</option>
                ))}
              </select>
            </div>
            <div>
              <label style={{ display: "block", fontSize: "12px", color: "#a3a3a3", marginBottom: "6px", textTransform: "uppercase", letterSpacing: "0.5px" }}>City</label>
              <select
                value={locCity}
                onChange={(e) => setLocCity(e.target.value)}
                style={{ width: "100%", padding: "10px 12px", borderRadius: "6px", border: "1px solid #333333", background: "#0f0f0f", color: "#f5f5f5", fontSize: "14px" }}
              >
                <option value="">All cities</option>
                {locCities.map((cityName) => (
                  <option key={cityName} value={cityName}>{cityName}</option>
                ))}
              </select>
            </div>
          </div>

          <div style={{ display: "flex", justifyContent: "space-between", alignItems: "center", gap: "12px", marginBottom: "20px", flexWrap: "wrap" }}>
            <span style={{ color: "#a3a3a3", fontSize: "14px" }}>
              {visibleBranches.length} {visibleBranches.length === 1 ? "branch" : "branches"} found
              {locCity ? ` in ${locCity}` : locState ? ` in ${locState}` : " across India"}
            </span>
            {hasLocFilter && (
              <button
                onClick={clearLocFilters}
                style={{ background: "#262626", color: "#ffffff", border: "1px solid #404040", borderRadius: "6px", padding: "8px 14px", cursor: "pointer", fontSize: "13px" }}
              >
                Clear filters
              </button>
            )}
          </div>

          {visibleBranches.length === 0 ? (
            <p style={{ color: "#a3a3a3", fontSize: "15px", textAlign: "center" }}>
              No branches in this location yet. Try another city or state.
            </p>
          ) : (
            <div style={{ display: "grid", gridTemplateColumns: "repeat(auto-fit, minmax(280px, 1fr))", gap: "20px" }}>
              {visibleBranches.map((b, idx) => (
                <div key={b.id || idx} style={{ background: "#171717", border: "1px solid #333333", borderRadius: "8px", padding: "20px" }}>
                  <h4 style={{ color: "#f97316", margin: "0 0 8px 0", fontSize: "17px" }}>📍 {b.name}</h4>
                  <p style={{ color: "#cccccc", fontSize: "14px", margin: "0 0 6px 0" }}>
                    {[b.address, b.city, b.state].filter(Boolean).join(", ")}
                  </p>
                  <p style={{ color: "#a3a3a3", fontSize: "13px", margin: 0 }}>📞 {b.phone || "Open Mon-Sun: 6 AM - 10 PM"}</p>
                </div>
              ))}
            </div>
          )}
        </div>
      </section>

      {/* Why Choose Us */}
      <section id="why-us" style={{ padding: "70px 24px", textAlign: "center", maxWidth: "800px", margin: "0 auto" }}>
        <h2 style={{ fontSize: "32px", color: "#ffffff", marginBottom: "16px" }}>
          Ready to Start Your Transformation?
        </h2>
        <p style={{ color: "#a3a3a3", fontSize: "16px", lineHeight: "1.6", marginBottom: "32px" }}>
          Join thousands of members who have achieved their fitness milestones with us. Sign up today and experience the difference.
        </p>
        <Link
          to="/register"
          style={{
            backgroundColor: "#f97316",
            color: "#000000",
            fontWeight: "bold",
            fontSize: "16px",
            padding: "14px 36px",
            borderRadius: "8px",
            textDecoration: "none",
            display: "inline-block",
          }}
        >
          Create Member Account Now
        </Link>
      </section>

      {/* Footer */}
      <footer
        style={{
          borderTop: "1px solid #262626",
          padding: "32px 24px",
          backgroundColor: "#050505",
          textAlign: "center",
          color: "#737373",
          fontSize: "13px",
        }}
      >
        <div style={{ color: "#f97316", fontWeight: "bold", fontSize: "16px", marginBottom: "8px" }}>
          GYM MANAGER
        </div>
        <p style={{ margin: "0 0 12px 0" }}>
          Complete Gym Membership Management System • Full Stack Spring Boot + React
        </p>
        <div>© {new Date().getFullYear()} Gym Manager. All rights reserved.</div>
      </footer>
    </div>
  );
};

export default LandingPage;
