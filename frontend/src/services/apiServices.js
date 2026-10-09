import api from "../Api.jsx";

export const authApi = {
  login: (data) => api.post("/auth/login", data),
  register: (data) => api.post("/auth/register", data),
};

export const userApi = {
  getProfile: () => api.get("/user/profile"),
  updateProfile: (data) => api.put("/user/profile", data),
  changePassword: (data) => api.post("/user/change-password", data),
};

export const planApi = {
  getAll: (all = false) => api.get(`/plans${all ? "?all=true" : ""}`),
  getById: (id) => api.get(`/plans/${id}`),
  create: (data) => api.post("/plans", data),
  update: (id, data) => api.put(`/plans/${id}`, data),
  toggleStatus: (id) => api.patch(`/plans/${id}/status`),
  delete: (id) => api.delete(`/plans/${id}`),
};

export const membershipApi = {
  getMyMembership: () => api.get("/memberships/my-membership"),
  purchase: (data) => api.post("/memberships/purchase", data),
  cancel: (id) => api.post(`/memberships/${id}/cancel`),
  freeze: (id) => api.post(`/memberships/${id}/freeze`),
  renew: (id, planId) => api.post(`/memberships/${id}/renew${planId ? `?planId=${planId}` : ""}`),
  upgrade: (id, newPlanId) => api.post(`/memberships/${id}/upgrade?newPlanId=${newPlanId}`),
  getAll: () => api.get("/memberships"),
};

export const branchApi = {
  getAll: () => api.get("/branches"),
  getById: (id) => api.get(`/branches/${id}`),
  create: (data) => api.post("/branches", data),
  update: (id, data) => api.put(`/branches/${id}`, data),
  toggleStatus: (id) => api.patch(`/branches/${id}/status`),
  delete: (id) => api.delete(`/branches/${id}`),
};

export const couponApi = {
  getAll: () => api.get("/coupons"),
  getById: (id) => api.get(`/coupons/${id}`),
  create: (data) => api.post("/coupons", data),
  update: (id, data) => api.put(`/coupons/${id}`, data),
  toggleStatus: (id) => api.patch(`/coupons/${id}/status`),
  delete: (id) => api.delete(`/coupons/${id}`),
  validate: (code, planPrice) => api.post("/coupons/validate", { code, planPrice }),
};

export const paymentApi = {
  getAll: () => api.get("/payments"),
  getById: (id) => api.get(`/payments/${id}`),
  getMyPayments: () => api.get("/payments/my-payments"),
  update: (id, data) => api.put(`/payments/${id}`, data),
  delete: (id) => api.delete(`/payments/${id}`),
};

export const invoiceApi = {
  getAll: () => api.get("/invoices"),
  getById: (id) => api.get(`/invoices/${id}`),
  getMyInvoices: () => api.get("/invoices/my-invoices"),
  delete: (id) => api.delete(`/invoices/${id}`),
};

export const supportApi = {
  create: (data) => api.post("/tickets", data),
  getMyTickets: () => api.get("/tickets/my-tickets"),
  getById: (id) => api.get(`/tickets/${id}`),
  getAll: (status, category) => {
    const params = {};
    if (status) params.status = status;
    if (category) params.category = category;
    return api.get("/tickets", { params });
  },
  updateStatus: (id, status) => api.patch(`/tickets/${id}/status?status=${status}`),
  respond: (id, response) => api.post(`/tickets/${id}/respond`, { response }),
  delete: (id) => api.delete(`/tickets/${id}`),
  getSupportStats: () => api.get("/dashboard/support-stats"),
};

export const dashboardApi = {
  getAdminStats: () => api.get("/dashboard/stats"),
  getSupportStats: () => api.get("/dashboard/support-stats"),
};
