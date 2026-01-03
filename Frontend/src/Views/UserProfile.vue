<template>
  <div class="layout">
    <!-- SIDEBAR -->
    <aside class="sidebar">
      <div class="logo-group">
        <div class="logo-circle"></div>
        <div>
          <h2 class="logo-title">LibrarySys</h2>
          <p class="logo-sub">Library Management System</p>
        </div>
      </div>
      <button class="menu-btn active">
        <span class="menu-ic">👤</span> User Profile
      </button>
      <!-- Borrow Page removed -->
    </aside>

    <!-- MAIN CONTENT -->
    <main class="content">
      <div class="header">
        <h1 class="page-title">User Profile</h1>
        <button class="back-btn" @click="goToHome">← Back to Home</button>
      </div>

      <div v-if="loading" class="loading-text">Loading profile...</div>
      <div v-else-if="error" class="error-text">{{ error }}</div>

      <div v-else>
        <!-- TOP CARD -->
        <div class="top-card">
          <div class="left-block">
            <div class="avatar"></div>

            <div class="left-info">
              <div class="name-row">
                <h2 class="name">{{ userData.username }}</h2>

                <div class="small-badges">
                  <span class="badge role">{{ getRoleLabel(userData) }}</span>
                  <span class="badge active">Active</span>
                </div>
              </div>

              <p class="member-id">
                Member ID: {{ userData.noAnggota || '-' }}
              </p>

              <div class="info-row">
                <div class="info-left">
                  <span class="icon">📧</span>
                  <div class="info-col">
                    <label>Email</label>
                    <b>{{ userData.email }}</b>
                  </div>
                </div>

                <div class="info-right">
                  <span class="icon">📞</span>
                  <div class="info-col">
                    <label>Phone</label>
                    <b>{{ userData.noTelpon || '-' }}</b>
                  </div>
                </div>
              </div>

              <div class="info-row">
                <div class="info-left">
                  <span class="icon">🏠</span>
                  <div class="info-col">
                    <label>Address</label>
                    <b>{{ userData.alamat || '-' }}</b>
                  </div>
                </div>

                <div class="info-right">
                  <span class="icon">👤</span>
                  <div class="info-col">
                    <label>Role</label>
                    <b>{{ getRoleLabel(userData) }}</b>
                  </div>
                </div>
              </div>
            </div>
          </div>

        </div>

        <!-- STATISTICS -->
        <div class="stats-row">
          <div class="stat-card blue">
            <div class="icon">📘</div>
            <p class="number">{{ currentBorrowed.length }}</p>
            <p class="label">Currently Borrowed</p>
          </div>

          <div class="stat-card red">
            <div class="icon">⛔</div>
            <p class="number">{{ overdueBooks.length }}</p>
            <p class="label">Overdue Books</p>
          </div>

          <div class="stat-card yellow">
            <div class="icon">💲</div>
            <p class="number">IDR {{ formatCurrency(totalFines) }}</p>
            <p class="label">Outstanding Fines</p>
          </div>

          <div class="stat-card green">
            <div class="icon">✔</div>
            <p class="number">{{ borrowHistory.length }}</p>
            <p class="label">Total Borrowed</p>
          </div>
        </div>

        <!-- CURRENTLY BORROWED -->
        <div class="section">
          <h2>Currently Borrowed Books</h2>

          <table class="data-table" v-if="currentBorrowed.length">
            <thead>
              <tr>
                <th style="width:110px;">Book ID</th>
                <th style="width:260px;">Title</th>
                <th style="width:180px;">Author</th>
                <th style="width:120px;">Borrow Date</th>
                <th style="width:120px;">Due Date</th>
                <th style="width:110px;">Status</th>
                
              </tr>
            </thead>
            <tbody>
              <tr v-for="book in currentBorrowed" :key="book.idPeminjaman">
                <td>{{ book.idBuku || '-' }}</td>
                <td class="title-cell">
                  <div class="book-thumb" :style="book.image ? { backgroundImage: `url(${book.image})`, backgroundSize: 'cover', backgroundPosition: 'center' } : {}"></div>
                  {{ book.judulBuku }}
                </td>
                <td>{{ book.pengarang }}</td>
                <td>{{ formatDate(book.tanggalPinjam) }}</td>
                <td>{{ formatDate(book.tanggalKembali) }}</td>
                <td>
                  <span :class="['badge', getStatusClass(book.status)]">
                    {{ book.status }}
                  </span>
                </td>
                
              </tr>
            </tbody>
          </table>

          <p v-else>No books currently borrowed.</p>
        </div>

        <!-- FINES -->
        <div class="section">
          <div class="section-header-row">
            <h2>Fine Status</h2>
          </div>

          <table class="data-table" v-if="overdueBooks.length">
            <thead>
              <tr>
                <th style="width:90px;">Fine ID</th>
                <th style="width:220px;">Book</th>
                <th style="width:120px;">Reason</th>
                <th style="width:120px;">Due Date</th>
                <th style="width:120px;">Amount</th>
                <th style="width:110px;">Status</th>
                
              </tr>
            </thead>
            <tbody>
              <tr v-for="book in overdueBooks" :key="book.idPeminjaman">
                <td>-</td>
                <td>{{ book.judulBuku }}</td>
                <td>Late Return</td>
                <td>{{ formatDate(book.tanggalKembali) }}</td>
                <td>IDR {{ formatCurrency(book.totalDenda || 0) }}</td>
                <td>
                  <span class="badge unpaid">Unpaid</span>
                </td>
                
              </tr>
            </tbody>
          </table>

          <p v-else>No outstanding fines.</p>
        </div>

        <!-- BORROW HISTORY -->
        <div class="section">
          <h2>Borrowing History</h2>

          <table class="data-table" v-if="borrowHistory.length">
            <thead>
              <tr>
                <th>Transaction ID</th>
                <th>Title</th>
                <th>Author</th>
                <th>Borrow Date</th>
                <th>Return Date</th>
                <th>Status</th>
              </tr>
            </thead>

            <tbody>
              <tr v-for="item in borrowHistory" :key="item.idPeminjaman">
                <td>{{ item.idPeminjaman }}</td>

                <td class="title-cell">
                  <div class="book-thumb" :style="item.image ? { backgroundImage: `url(${item.image})`, backgroundSize: 'cover', backgroundPosition: 'center' } : {}"></div>
                  {{ item.judulBuku }}
                </td>

                <td>{{ item.pengarang }}</td>

                <td>{{ formatDate(item.tanggalPinjam) }}</td>
                <td>
                  {{ item.tanggalKembali ? formatDate(item.tanggalKembali) : '-' }}
                </td>

                <td>
                  <span :class="['badge', getStatusClass(item.status)]">
                    {{ item.status }}
                  </span>
                </td>
              </tr>
            </tbody>
          </table>

          <p v-else>No borrowing history yet.</p>
        </div>
      </div>
    </main>
  </div>
</template>

<script setup>
import { ref, onMounted, computed } from "vue";
import { useRouter } from "vue-router";
import axios from "axios";

const router = useRouter();
const API_BASE = "http://localhost:8080";

const loading = ref(true);
const error = ref(null);

const userData = ref({});
const currentBorrowed = ref([]);
const borrowHistory = ref([]);
const availableBooks = ref([]);

// Overdue + Fine calculations
const overdueBooks = computed(() =>
  currentBorrowed.value.filter(b => b.status === "TERLAMBAT")
);

const totalFines = computed(() =>
  overdueBooks.value.reduce((sum, b) => sum + (b.totalDenda || 0), 0)
);

// Helpers
function goToHome() {
  router.push("/HomePage");
}

function getRoleLabel(u) {
  return u.noAnggota !== undefined ? "Pemustaka" : "Pustakawan";
}

function getStatusClass(status) {
  if (status === "TERLAMBAT") return "overdue";
  if (status === "DIPINJAM") return "active";
  if (status === "KEMBALI") return "returned-late";
  return "unpaid";
}

function formatDate(dateStr) {
  if (!dateStr) return "-";
  return new Date(dateStr).toLocaleDateString("id-ID");
}

function formatCurrency(value) {
  return Number(value || 0).toLocaleString("id-ID");
}

// Load available books from API and transform to minimal UI model
async function loadAvailableBooks() {
  try {
    const res = await axios.get(`${API_BASE}/api/buku`);
    const books = res.data || [];
    // Note: BukuSearchResponseDto doesn't include idBuku
    availableBooks.value = books.map(b => ({
      id: null, 
      title: b.judul,
      author: b.pengarang,
      image: b.urlGambarSampul || null,
      fee: b.hargaSewa || 0,
      stock: b.jumlahStok || 0
    }));
  } catch (err) {
    console.warn("Could not load available books:", err);
    availableBooks.value = [];
  }
}

// Enrich borrowing history items with book details (image, author, fee)
async function loadBorrowingHistory() {
  try {
    // Use /api/peminjaman/history which uses JWT token (Principal)
    const res = await axios.get(`${API_BASE}/api/peminjaman/history`);
    const history = res.data || [];

    const today = new Date();
    const enriched = await Promise.all(history.map(async (item) => {
      let rawStatus = (item.status || "").toString().toUpperCase();
      let status = rawStatus;
      if (rawStatus === "PINJAM" || rawStatus === "BORROWED") status = "DIPINJAM";
      if (rawStatus === "RETURNED" || rawStatus === "DIKEMBALIKAN") status = "KEMBALI";

      let image = null;
      let author = item.pengarang || "";

      try {
        const match = availableBooks.value.find(b => b.title === item.judulBuku);
        if (match) {
          image = match.image || null;
          author = match.author || author;
        } else {
          const searchRes = await axios.get(`${API_BASE}/api/buku/search?keyword=${encodeURIComponent(item.judulBuku)}`);
          if (searchRes.data && searchRes.data.length > 0) {
            const foundBook = searchRes.data.find(b => b.judul === item.judulBuku);
            if (foundBook) {
              image = foundBook.urlGambarSampul || null;
              author = foundBook.pengarang || author;
            }
          }
        }
      } catch (err) {
        console.warn("Failed to fetch book detail for", item.judulBuku, err);
      }

      // Calculate due date: tanggalPinjam + 7 days
      let dueDate = null;
      if (item.tanggalPinjam) {
        const borrowDate = new Date(item.tanggalPinjam);
        borrowDate.setDate(borrowDate.getDate() + 7);
        dueDate = borrowDate.toISOString().split("T")[0];
      }

      // Overdue logic: jika belum dikembalikan dan dueDate < hari ini
      let isOverdue = false;
      if (status === "DIPINJAM" && dueDate) {
        const due = new Date(dueDate);
        if (due < today) {
          status = "TERLAMBAT";
          isOverdue = true;
        }
      }

      let totalDenda = item.totalDenda || 0;
      if (isOverdue && totalDenda === 0) {
        const due = new Date(dueDate);
        const daysLate = Math.max(0, Math.ceil((today - due) / (1000 * 60 * 60 * 24)));
        totalDenda = daysLate * 5000;
      }

      return {
        idPeminjaman: item.idPeminjaman,
        idBuku: null,
        judulBuku: item.judulBuku,
        pengarang: author || item.username || "-",
        tanggalPinjam: item.tanggalPinjam,
        tanggalKembali: dueDate,
        totalDenda,
        status,
        image
      };
    }));

    // set both history and current borrowed lists
    borrowHistory.value = enriched;
    currentBorrowed.value = enriched.filter(it => (it.status === "DIPINJAM" || it.status === "TERLAMBAT"));
  } catch (err) {
    console.error("Error loading borrowing history:", err);
    borrowHistory.value = [];
    currentBorrowed.value = [];
  }
}

// FETCH PROFILE
async function fetchProfile() {
  try {
    const token = sessionStorage.getItem("token");
    const storedUserData = sessionStorage.getItem("userData");

    if (!token) {
      error.value = "No user logged in";
      router.push("/LoginPage");
      return;
    }

    // Use stored user data from login
    if (storedUserData) {
      userData.value = JSON.parse(storedUserData);
    }

    // Load available books first so we can use them as fallback
    await loadAvailableBooks();

    // Load and enrich borrowing history (uses JWT token)
    await loadBorrowingHistory();

  } catch (err) {
    console.error(err);
    if (err.response?.status === 401) {
      error.value = "Session expired. Please login again.";
      router.push("/LoginPage");
    } else {
      error.value = "Failed to load profile data.";
    }
  } finally {
    loading.value = false;
  }
}


onMounted(() => {
  fetchProfile();
});

function goToBorrowHistory() {
  router.push({ path: '/BorrowPage', query: { tab: 'my-borrowing-history', menu: 'active' } });
}

</script>

<style scoped>
.layout {
  display: flex;
  min-height: 100vh;
  background: #f8fafc;
  font-family: Inter, sans-serif;
}
.sidebar {
  width: 230px;
  background: #ffffff;
  border-right: 1px solid #e5e7eb;
  padding: 26px 18px;
}
.logo-group {
  display: flex;
  align-items: center;
  gap: 10px;
  margin-bottom: 26px;
}
.logo-circle {
  width: 42px;
  height: 42px;
  background: #e2e8f0;
  border-radius: 50%;
}
.logo-title {
  font-size: 17px;
  margin: 0;
  font-weight: 700;
}
.logo-sub {
  margin: 0;
  font-size: 11px;
  color: #64748b;
}
.menu-btn {
  width: 100%;
  padding: 10px 12px;
  border-radius: 8px;
  font-size: 14px;
  border: none;
  background: transparent;
  text-align: left;
  cursor: pointer;
  color: #334155;
  display: flex;
  align-items: center;
  gap: 10px;
}
.menu-btn.active {
  background: #f1f5f9;
  font-weight: 600;
}
.content {
  flex: 1;
  padding: 32px 50px;
}
.header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding-bottom: 6px;
  border-bottom: 1px solid #e5e7eb;
}
.page-title {
  font-size: 20px;
  font-weight: 700;
}
.back-btn {
  background: transparent;
  border: none;
  font-size: 14px;
  color: #475569;
  cursor: pointer;
}
.loading-text,
.error-text {
  text-align: center;
  padding: 30px;
  font-size: 16px;
}
.error-text {
  color: red;
}
.top-card {
  margin-top: 20px;
  background: white;
  padding: 26px 28px;
  border-radius: 12px;
  border: 1px solid #e5e7eb;
  display: flex;
  justify-content: space-between;
  align-items: flex-start;
}
.left-block {
  display: flex;
  gap: 20px;
}
.avatar {
  width: 70px;
  height: 70px;
  border-radius: 50%;
  background: #e2e8f0;
}
.name-row {
  display: flex;
  flex-direction: column;
}
.small-badges {
  display: flex;
  gap: 6px;
  margin: 4px 0;
}
.badge {
  padding: 4px 10px;
  border-radius: 6px;
  font-size: 12px;
}
.badge.role {
  background: #f1f5f9;
  color: #475569;
}
.badge.active {
  background: #dcfce7;
  color: #16a34a;
}
.member-id {
  margin: 4px 0 10px 0;
  font-size: 13px;
  color: #475569;
}
.info-row {
  display: flex;
  justify-content: space-between;
  width: 520px;
  margin-top: 12px;
}
.info-left,
.info-right {
  display: flex;
  gap: 10px;
  align-items: center;
}
.info-col {
  display: flex;
  flex-direction: column;
}
.info-col label {
  font-size: 12px;
  color: #6b7280;
}
.info-col b {
  font-size: 13px;
}
.edit-btn {
  background: #0f172a;
  color: white;
  padding: 8px 18px;
  font-size: 13px;
  border-radius: 6px;
  border: none;
  cursor: pointer;
}
.stats-row {
  display: flex;
  gap: 16px;
  margin-top: 20px;
}
.stat-card {
  flex: 1;
  background: white;
  padding: 16px;
  text-align: center;
  border: 1px solid #e5e7eb;
  border-radius: 10px;
}
.stat-card .icon {
  font-size: 22px;
}
.number {
  font-size: 20px;
  font-weight: 700;
  margin: 6px 0;
}
.label {
  font-size: 12px;
}
/* Responsive & modern styling improvements */
.section {
  margin-top: 28px;
  background: #fff;
  border-radius: 10px;
  box-shadow: 0 2px 8px rgba(0,0,0,0.03);
  padding: 18px 16px;
}
.section h2 {
  font-size: 16px;
  font-weight: 700;
  margin-bottom: 8px;
}
.section-header-row {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 8px;
}
.pay-all-btn {
  background: #0f172a;
  color: white;
  padding: 6px 16px;
  border-radius: 6px;
  border: none;
  cursor: pointer;
  font-weight: 600;
  transition: background 0.2s;
}
.pay-all-btn:disabled {
  background: #9ca3af;
  cursor: not-allowed;
}
.data-table {
  width: 100%;
  margin-top: 12px;
  background: white;
  border-radius: 10px;
  overflow: hidden;
  border-collapse: collapse;
  box-shadow: 0 1px 4px rgba(0,0,0,0.04);
}
.data-table th {
  background: #f8fafc;
  padding: 12px 8px;
  font-size: 13px;
  font-weight: 700;
  border-bottom: 1px solid #e2e8f0;
  text-align: left;
}
.data-table td {
  padding: 12px 8px;
  border-bottom: 1px solid #f1f5f9;
  font-size: 13px;
  vertical-align: middle;
}
.book-thumb {
  width: 28px;
  height: 36px;
  background: #e2e8f0;
  border-radius: 6px;
  margin-right: 10px;
  box-shadow: 0 1px 2px rgba(0,0,0,0.06);
}
.title-cell {
  display: flex;
  align-items: center;
}
.badge.overdue {
  background: #fee2e2;
  color: #dc2626;
  font-weight: 600;
}
.badge.active {
  background: #dcfce7;
  color: #16a34a;
  font-weight: 600;
}
.badge.returned-late {
  background: #fef9c3;
  color: #b45309;
  font-weight: 600;
}
.badge.unpaid {
  background: #fee2e2;
  color: #dc2626;
  font-weight: 600;
}
.renew-btn,
.pay-btn {
  background: #0f172a;
  color: white;
  padding: 6px 14px;
  border-radius: 6px;
  border: none;
  cursor: pointer;
  font-weight: 600;
  transition: background 0.2s;
}
.renew-btn:hover,
.pay-btn:hover {
  background: #334155;
}
@media (max-width: 900px) {
  .content {
    padding: 24px 8px;
  }
  .info-row {
    width: 100%;
    flex-direction: column;
    gap: 8px;
  }
  .sidebar {
    width: 100px;
    padding: 12px 4px;
  }
  .top-card {
    flex-direction: column;
    gap: 16px;
    padding: 16px 10px;
  }
}
</style>
