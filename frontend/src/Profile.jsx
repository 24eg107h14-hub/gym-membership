import { useState, useEffect } from "react";
import { userApi } from "./services/apiServices.js";

const Profile = () => {
  const [profile, setProfile] = useState({
    fullName: "",
    email: "",
    phone: "",
    address: "",
    gender: "Male",
    dob: "",
    role: "",
  });

  const [passwordData, setPasswordData] = useState({
    currentPassword: "",
    newPassword: "",
    confirmPassword: "",
  });

  const [loading, setLoading] = useState(true);
  const [profileMsg, setProfileMsg] = useState("");
  const [profileErr, setProfileErr] = useState("");
  const [passMsg, setPassMsg] = useState("");
  const [passErr, setPassErr] = useState("");

  useEffect(() => {
    const fetchProfile = async () => {
      setLoading(true);
      try {
        const res = await userApi.getProfile();
        setProfile({
          fullName: res.data.name || res.data.fullName || "",
          email: res.data.email || "",
          phone: res.data.phone || "",
          address: res.data.address || "",
          gender: res.data.gender || "Male",
          dob: res.data.dob || "",
          role: res.data.role || "",
        });
      } catch {
        setProfileErr("Failed to load profile.");
      } finally {
        setLoading(false);
      }
    };

    fetchProfile();
  }, []);

  const handleUpdateProfile = async (e) => {
    e.preventDefault();
    setProfileMsg("");
    setProfileErr("");

    try {
      await userApi.updateProfile({
        name: profile.fullName,
        email: profile.email,
        phone: profile.phone,
        address: profile.address,
        gender: profile.gender,
        dob: profile.dob || null,
      });
      setProfileMsg("Profile updated successfully!");
      if (profile.fullName) {
        localStorage.setItem("name", profile.fullName);
      }
    } catch (err) {
      setProfileErr(err.response?.data?.message || "Failed to update profile.");
    }
  };

  const handleChangePassword = async (e) => {
    e.preventDefault();
    setPassMsg("");
    setPassErr("");

    if (passwordData.newPassword !== passwordData.confirmPassword) {
      setPassErr("New passwords do not match.");
      return;
    }

    try {
      await userApi.changePassword({
        currentPassword: passwordData.currentPassword,
        newPassword: passwordData.newPassword,
        confirmPassword: passwordData.confirmPassword,
      });
      setPassMsg("Password changed successfully!");
      setPasswordData({ currentPassword: "", newPassword: "", confirmPassword: "" });
    } catch (err) {
      setPassErr(err.response?.data?.message || "Failed to change password.");
    }
  };

  if (loading) {
    return <div style={{ color: "var(--text-muted)", padding: "20px 0" }}>Loading profile...</div>;
  }

  return (
    <div style={{ maxWidth: "700px", margin: "0 auto" }}>
      <h2 className="page-title">User Profile & Account Settings</h2>

      {/* Personal Profile Details Card */}
      <div className="card" style={{ marginBottom: "24px" }}>
        <h3 style={{ color: "#ffffff", marginBottom: "16px", fontSize: "18px" }}>
          Personal Information ({profile.role})
        </h3>

        {profileMsg && <div style={{ color: "var(--success)", marginBottom: "12px", fontSize: "14px" }}>{profileMsg}</div>}
        {profileErr && <div className="error-text">{profileErr}</div>}

        <form onSubmit={handleUpdateProfile}>
          <div style={{ marginBottom: "12px" }}>
            <label>Full Name</label>
            <input
              type="text"
              required
              value={profile.fullName}
              onChange={(e) => setProfile({ ...profile, fullName: e.target.value })}
            />
          </div>

          <div style={{ display: "grid", gridTemplateColumns: "1fr 1fr", gap: "12px", marginBottom: "12px" }}>
            <div>
              <label>Email (Account Login)</label>
              <input
                type="email"
                disabled
                value={profile.email}
              />
            </div>
            <div>
              <label>Phone Number</label>
              <input
                type="tel"
                value={profile.phone}
                onChange={(e) => setProfile({ ...profile, phone: e.target.value })}
              />
            </div>
          </div>

          <div style={{ display: "grid", gridTemplateColumns: "1fr 1fr", gap: "12px", marginBottom: "12px" }}>
            <div>
              <label>Gender</label>
              <select
                value={profile.gender}
                onChange={(e) => setProfile({ ...profile, gender: e.target.value })}
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
                value={profile.dob}
                onChange={(e) => setProfile({ ...profile, dob: e.target.value })}
              />
            </div>
          </div>

          <div style={{ marginBottom: "16px" }}>
            <label>Address</label>
            <input
              type="text"
              value={profile.address}
              onChange={(e) => setProfile({ ...profile, address: e.target.value })}
              placeholder="City, State"
            />
          </div>

          <button type="submit" className="btn">
            Save Profile Changes
          </button>
        </form>
      </div>

      {/* Change Password Card */}
      <div className="card">
        <h3 style={{ color: "#ffffff", marginBottom: "16px", fontSize: "18px" }}>
          Security & Change Password
        </h3>

        {passMsg && <div style={{ color: "var(--success)", marginBottom: "12px", fontSize: "14px" }}>{passMsg}</div>}
        {passErr && <div className="error-text">{passErr}</div>}

        <form onSubmit={handleChangePassword}>
          <div style={{ marginBottom: "12px" }}>
            <label>Current Password</label>
            <input
              type="password"
              required
              value={passwordData.currentPassword}
              onChange={(e) => setPasswordData({ ...passwordData, currentPassword: e.target.value })}
              placeholder="••••••••"
            />
          </div>

          <div style={{ display: "grid", gridTemplateColumns: "1fr 1fr", gap: "12px", marginBottom: "16px" }}>
            <div>
              <label>New Password (min 6)</label>
              <input
                type="password"
                required
                minLength={6}
                value={passwordData.newPassword}
                onChange={(e) => setPasswordData({ ...passwordData, newPassword: e.target.value })}
                placeholder="••••••••"
              />
            </div>
            <div>
              <label>Confirm New Password</label>
              <input
                type="password"
                required
                minLength={6}
                value={passwordData.confirmPassword}
                onChange={(e) => setPasswordData({ ...passwordData, confirmPassword: e.target.value })}
                placeholder="••••••••"
              />
            </div>
          </div>

          <button type="submit" className="btn btn-gray">
            Update Password
          </button>
        </form>
      </div>
    </div>
  );
};

export default Profile;
