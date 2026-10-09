import { useState } from "react";
import { Link, useNavigate } from "react-router-dom";
import api from "./Api.jsx";

const Login = () => {
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [error, setError] = useState("");
  const [loading, setLoading] = useState(false);
  const navigate = useNavigate();

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError("");
    setLoading(true);

    try {
      const response = await api.post("/auth/login", {
        email: email.trim(),
        password: password,
      });
      const { token, role, name } = response.data;

      localStorage.setItem("token", token);
      localStorage.setItem("role", role);
      localStorage.setItem("name", name);

      if (role === "SUPPORT") {
        navigate("/support-dashboard");
      } else {
        navigate("/dashboard");
      }
    } catch (err) {
      const message =
        err.response && err.response.data && err.response.data.message
          ? err.response.data.message
          : "Invalid email or password";
      setError(message);
    } finally {
      setLoading(false);
    }
  };

  const fillDemo = (demoEmail, demoPass) => {
    setEmail(demoEmail);
    setPassword(demoPass);
    setError("");
  };

  return (
    <div className="auth-page">
      <div className="login-card">
        <h2 className="auth-title">GYM MANAGER</h2>

        {error && <div className="error-text">{error}</div>}

        <form onSubmit={handleSubmit} className="auth-form">
          <div>
            <label htmlFor="email">Email</label>
            <input
              id="email"
              type="email"
              required
              value={email}
              onChange={(e) => setEmail(e.target.value)}
              placeholder="admin@gym.com"
            />
          </div>

          <div>
            <label htmlFor="password">Password</label>
            <input
              id="password"
              type="password"
              required
              value={password}
              onChange={(e) => setPassword(e.target.value)}
              placeholder="••••••••"
            />
          </div>

          <button
            type="submit"
            disabled={loading}
            className="btn"
            style={{ width: "100%" }}
          >
            {loading ? "Signing in..." : "Sign In"}
          </button>
        </form>

        {/* Demo Credentials Quick Fill */}
        <div style={{ marginTop: "20px", borderTop: "1px solid var(--border)", paddingTop: "14px" }}>
          <div style={{ fontSize: "11px", color: "var(--text-muted)", textTransform: "uppercase", marginBottom: "8px", fontWeight: "bold" }}>
            Quick Demo Login:
          </div>
          <div style={{ display: "flex", gap: "8px" }}>
            <button
              type="button"
              onClick={() => fillDemo("admin@gym.com", "admin123")}
              className="btn btn-gray btn-sm"
              style={{ flex: 1, fontSize: "11px" }}
            >
              Admin
            </button>
            <button
              type="button"
              onClick={() => fillDemo("support@gym.com", "support123")}
              className="btn btn-gray btn-sm"
              style={{ flex: 1, fontSize: "11px" }}
            >
              Support
            </button>
          </div>
        </div>

        <div className="auth-footer">
          Don't have an account? <Link to="/register">Register</Link>
        </div>
      </div>
    </div>
  );
};

export default Login;
