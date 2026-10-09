import axios from "axios";

const api = axios.create({
  baseURL: "http://localhost:8080/api",
});

api.interceptors.request.use(
  (config) => {
    const token = localStorage.getItem("token");
    if (token) {
      config.headers.Authorization = `Bearer ${token}`;
    }
    return config;
  },
  (error) => {
    return Promise.reject(error);
  }
);

api.interceptors.response.use(
  (response) => {
    return response;
  },
  (error) => {
    const status = error.response ? error.response.status : null;
    const url = error.config && error.config.url ? error.config.url : "";

    if ((status === 401 || status === 403) && !url.includes("/auth/")) {
      const hadSession = Boolean(localStorage.getItem("token"));
      localStorage.clear();

      // Session expired / was rejected: send the user home instead of the login
      // page. Only when a session actually existed, so anonymous 401s on a
      // public page cannot trigger a reload loop.
      if (hadSession && window.location.pathname !== "/") {
        window.location.href = "/";
      }
    }

    return Promise.reject(error);
  }
);

export default api;
