import { createApp } from "vue";
import App from "./App.vue";
import router from "./router";
import axios from "axios";

// [CRITICAL] This allows cookies (JSESSIONID) to be sent/received
axios.defaults.withCredentials = true; 
// [OPTIONAL] Set base URL globally to avoid typing it everywhere
axios.defaults.baseURL = "http://localhost:8080"; 

const app = createApp(App);
app.use(router);
app.mount("#app");
