<template>
  <div :class="['container', currentTab + '-mode']">
    <div class="header">
      <div class="icon-container">
        <svg fill="none" viewBox="0 0 32 32">
          <path d="M1.33368 1.33368L6.66701 20.0003" stroke="white" stroke-linecap="round" stroke-linejoin="round" stroke-width="2.66667"/>
          <path d="M1.33333 1.33333V20" stroke="white" stroke-linecap="round" stroke-linejoin="round" stroke-width="2.66667"/>
          <path d="M1.33333 1.33333V17.3333" stroke="white" stroke-linecap="round" stroke-linejoin="round" stroke-width="2.66667"/>
          <path d="M1.33333 1.33333V22.6667" stroke="white" stroke-linecap="round" stroke-linejoin="round" stroke-width="2.66667"/>
        </svg>
      </div>

      <div class="title-container">
        <p class="main-title">Library Management System</p>
        <p class="subtitle">Manage your library collection and borrowing with ease</p>
      </div>
    </div>

    <div :class="['card', currentTab + '-mode']">
      <div class="tab-list">
        <button :class="['tab-button', currentTab === 'login' ? 'active' : '']" @click="switchTab('login')">Login</button>
        <button :class="['tab-button', currentTab === 'register' ? 'active' : '']" @click="switchTab('register')">Register</button>
      </div>

      <div class="content-card">
        <div class="card-header">
          <div class="card-title">
            <svg v-html="headerIcon" fill="none" viewBox="0 0 20 20"></svg>
            <h2>{{ headerTitle }}</h2>
          </div>
          <p class="card-description">{{ headerDescription }}</p>
        </div>

        <!-- LOGIN FORM -->
        <form v-show="currentTab === 'login'" class="form" @submit.prevent="handleLogin">
          <div class="form-field">
            <label class="form-label">Username</label>
            <div class="input-wrapper">
              <input type="text" class="form-input" placeholder="Enter your username" v-model="loginUsername" required />
            </div>
          </div>

          <div class="form-field">
            <label class="form-label">Password</label>
            <div class="input-wrapper">
              <input type="password" class="form-input" placeholder="Enter your password" v-model="loginPassword" required />
            </div>
          </div>

          <button type="submit" class="submit-button">Login</button>
        </form>

        <!-- REGISTER FORM -->
        <form v-show="currentTab === 'register'" class="form" @submit.prevent="handleRegister">

          <div class="form-field">
            <label class="form-label">Username</label>
            <div class="input-wrapper">
              <input type="text" class="form-input" placeholder="Choose a username" v-model="registerName" required />
            </div>
          </div>

          <div class="form-field">
            <label class="form-label">Email</label>
            <div class="input-wrapper">
              <input type="email" class="form-input" placeholder="Enter your email" v-model="registerEmail" required />
            </div>
          </div>

          <div class="form-field">
            <label class="form-label">Alamat</label>
            <div class="input-wrapper">
              <input type="text" class="form-input" placeholder="Enter your address" v-model="registerAlamat" />
            </div>
          </div>

          <div class="form-field">
            <label class="form-label">No Telepon</label>
            <div class="input-wrapper">
              <input type="text" class="form-input" placeholder="Enter your phone number" v-model="registerNoTelpon" />
            </div>
          </div>

          <div class="form-field">
            <label class="form-label">Password</label>
            <div class="input-wrapper">
              <input type="password" class="form-input" placeholder="Create a password (min 6 characters)" v-model="registerPassword" required minlength="6" />
            </div>
          </div>

          <div class="form-field">
            <label class="form-label">Confirm Password</label>
            <div class="input-wrapper">
              <input type="password" class="form-input" placeholder="Confirm your password" v-model="registerConfirmPassword" required />
            </div>
          </div>

          <button type="submit" class="submit-button">Register</button>
        </form>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed } from "vue";
import axios from "axios";

const API_BASE = "http://localhost:8080/api/auth";

const currentTab = ref("login");

// LOGIN FIELDS
const loginUsername = ref("");
const loginPassword = ref("");

// REGISTER FIELDS
const registerName = ref("");
const registerEmail = ref("");
const registerAlamat = ref("");
const registerNoTelpon = ref("");
const registerPassword = ref("");
const registerConfirmPassword = ref("");

// Dynamic text for UI
const headerTitle = computed(() => (currentTab.value === "login" ? "Login" : "Register"));
const headerDescription = computed(() =>
  currentTab.value === "login"
    ? "Enter your account to access the library system"
    : "Create a new account to access the library system"
);

function switchTab(tab) {
  currentTab.value = tab;
}

// ------------------ LOGIN --------------------
async function handleLogin() {
  try {
    const res = await axios.post(`${API_BASE}/login`, {
      username: loginUsername.value,
      password: loginPassword.value,
    });

    alert("Login Berhasil!\nUser ID: " + res.data.idUser);
    console.log("LOGIN SUCCESS:", res.data);
  } catch (err) {
    alert("Login gagal: " + (err.response?.data || "Server error"));
  }
}

// ------------------ REGISTER --------------------
async function handleRegister() {
  if (registerPassword.value !== registerConfirmPassword.value) {
    alert("Passwords do not match!");
    return;
  }

  try {
    const res = await axios.post(`${API_BASE}/register`, {
      username: registerName.value,
      email: registerEmail.value,
      password: registerPassword.value,
      alamat: registerAlamat.value,
      noTelpon: registerNoTelpon.value,
    });

    alert("Register Berhasil!\nNomor Anggota: " + res.data.noAnggota);
    console.log("REGISTER SUCCESS:", res.data);
  } catch (err) {
    alert("Registrasi gagal: " + (err.response?.data || "Server error"));
  }
}
</script>

<style scoped>
/* CSS kamu tetap sama — tidak diubah */
* {
  margin: 0;
  padding: 0;
  box-sizing: border-box;
}

body {
  font-family: Arial, sans-serif;
  background: white;
  width: 100vw;
  height: 100vh;
  overflow: hidden;
}

.container {
  position: absolute;
  left: 50%;
  top: 50%;
  transform: translate(-50%, -50%);
  width: 448px;
  display: flex;
  flex-direction: column;
  gap: 24px;
  transition: height 0.3s ease;
}

.container.login-mode {
  height: 588px;
}

.container.register-mode {
  height: 736px;
}

.header {
  height: 132px;
  position: relative;
  width: 100%;
}

.icon-container {
  position: absolute;
  background: #030213;
  left: 196px;
  top: 0;
  width: 56px;
  height: 56px;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 12px;
}

.icon-container svg {
  width: 32px;
  height: 32px;
}

.title-container {
  position: absolute;
  left: 0;
  top: 72px;
  width: 448px;
  height: 60px;
}

.main-title {
  font-size: 30px;
  font-weight: bold;
  line-height: 36px;
  color: #0a0a0a;
  text-align: center;
  left: 50%;
  transform: translateX(-50%);
  position: absolute;
  white-space: nowrap;
  top: -3px;
}

.subtitle {
  font-size: 16px;
  line-height: 24px;
  color: #717182;
  text-align: center;
  left: 50%;
  transform: translateX(-50%);
  position: absolute;
  white-space: nowrap;
  top: 34px;
}

.card {
  background: white;
  border-radius: 14px;
  width: 100%;
  border: 1px solid rgba(0, 0, 0, 0.1);
  padding: 25px 0 0 25px;
  transition: height 0.3s ease;
}

.card.login-mode {
  height: 432px;
}

.card.register-mode {
  height: 580px;
}

.tab-list {
  background: #ececf0;
  height: 36px;
  border-radius: 14px;
  width: 398px;
  padding: 3.5px 3px;
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 0;
  margin-bottom: 32px;
}

.tab-button {
  height: 29px;
  border-radius: 14px;
  border: none;
  background: transparent;
  cursor: pointer;
  font-size: 14px;
  line-height: 20px;
  color: #0a0a0a;
  transition: background 0.2s;
  font-family: Arial, sans-serif;
}

.tab-button.active {
  background: white;
}

.tab-button:hover {
  opacity: 0.8;
}

.content-card {
  background: white;
  border-radius: 14px;
  border: 1px solid rgba(0, 0, 0, 0.1);
  width: 398px;
  padding: 24px;
}

.card-header {
  margin-bottom: 25px;
}

.card-title {
  display: flex;
  align-items: center;
  gap: 8px;
  margin-bottom: 6px;
}

.card-title svg {
  width: 20px;
  height: 20px;
}

.card-title h2 {
  font-size: 16px;
  line-height: 16px;
  color: #0a0a0a;
  font-weight: normal;
}

.card-description {
  font-size: 16px;
  line-height: 24px;
  color: #717182;
  max-width: 298px;
}

.form {
  display: flex;
  flex-direction: column;
  gap: 16px;
}

.form-field {
  display: flex;
  flex-direction: column;
  gap: 8px;
}

.form-label {
  font-size: 14px;
  line-height: 14px;
  color: #0a0a0a;
}

.input-wrapper {
  background: #f3f3f5;
  height: 36px;
  border-radius: 8px;
  padding: 4px 12px;
  display: flex;
  align-items: center;
}

.form-input {
  width: 100%;
  background: transparent;
  border: none;
  outline: none;
  font-size: 14px;
  color: #0a0a0a;
  font-family: Arial, sans-serif;
}

.form-input::placeholder {
  color: #717182;
}

.submit-button {
  background: #030213;
  height: 36px;
  border-radius: 8px;
  border: none;
  color: white;
  font-size: 14px;
  line-height: 20px;
  cursor: pointer;
  font-family: Arial, sans-serif;
  transition: opacity 0.2s;
}

.submit-button:hover {
  opacity: 0.9;
}

.submit-button:active {
  opacity: 0.8;
}
</style>
