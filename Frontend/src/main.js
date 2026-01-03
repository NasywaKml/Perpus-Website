import { createApp } from "vue";
import App from "./App.vue";
import router from "./router";
import axios from "axios";

// Set base URL globally
axios.defaults.baseURL = "http://localhost:8080"; 

// Add JWT token to all authenticated requests
axios.interceptors.request.use(
  (config) => {
    const token = sessionStorage.getItem("token");
    const tokenType = sessionStorage.getItem("tokenType") || "Bearer";
    
    // Add Authorization header if token exists
    if (token) {
      config.headers.Authorization = `${tokenType} ${token}`;
    }
    
    return config;
  },
  (error) => {
    return Promise.reject(error);
  }
);

// Handle 401 Unauthorized responses
axios.interceptors.response.use(
  (response) => response,
  (error) => {
    if (error.response?.status === 401) {
      // Clear session and redirect to login
      sessionStorage.clear();
      router.push("/LoginPage");
    }
    return Promise.reject(error);
  }
);

const app = createApp(App);
app.use(router);
app.mount("#app");
