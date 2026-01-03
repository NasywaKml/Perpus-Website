<template>
  <div class="admin-page">
    <aside class="sidebar">
      <div class="brand">
        <div class="logo">
          <svg width="22" height="22" viewBox="0 0 24 24" fill="none" xmlns="http://www.w3.org/2000/svg">
            <path d="M3 18V6a2 2 0 012-2h12" stroke="white" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
            <path d="M21 18V6a2 2 0 00-2-2H7" stroke="white" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
          </svg>
        </div>
        <div class="brand-text">Library System</div>
      </div>

      <nav class="nav">
        <button type="button" class="nav-item" :class="{active: activeTab==='books'}" @click="setTab('books')">
          <span class="icon">📚</span>
          <span class="label">Books</span>
        </button>
        <button type="button" class="nav-item" :class="{active: activeTab==='users'}" @click="setTab('users')">
          <span class="icon">👥</span>
          <span class="label">Users</span>
        </button>
        <button type="button" class="nav-item" :class="{active: activeTab==='borrowings'}" @click="setTab('borrowings')">
          <span class="icon">📥</span>
          <span class="label">Borrowings</span>
        </button>
      </nav>

      <!-- Logout Button added here -->
      <div class="sidebar-footer">
        <button type="button" class="nav-item logout-btn" @click="handleLogout">
          <span class="icon">🚪</span>
          <span class="label">Logout</span>
        </button>
      </div>
    </aside>

    <div class="main">
      <header class="topbar">
        <div class="top-left">
          <h2 class="page-title">{{currentTitle}}</h2>
        </div>
        <div class="top-right">
          <div class="searchbox">
            <input v-model="globalSearch" placeholder="Search..." />
          </div>
          <div class="avatar" />
        </div>
      </header>

      <section class="content">
        <!-- Tabs removed to match screenshots; sidebar controls the active view -->

        <div v-if="activeTab==='books'" class="panel">
          <div class="actions-row">
            <div class="left-actions">
              <button class="btn primary" @click="openAddBook">+ Add New Book</button>
              <select class="btn" v-model="selectedCategory" style="padding-right:24px">
                <option value="">All Categories</option>
                <option v-for="c in uniqueCategories" :key="c" :value="c">{{c}}</option>
              </select>
            </div>
            <div class="right-actions">
              <button class="btn" @click="exportCSV">Export</button>
            </div>
          </div>

          <table class="table">
            <thead>
              <tr>
                <th>Book ID</th>
                <th>Title</th>
                <th>Author</th>
                <th>ISBN</th>
                <th>Category</th>
                <th>Status</th>
                <th>Copies</th>
                <th>Actions</th>
              </tr>
            </thead>
            <tbody>
              <tr v-for="b in paginatedBooks" :key="b.idBuku">
                <td>{{formatId(b.idBuku)}}</td>
                <td class="title-cell">
                  <div class="cover" v-if="b.urlGambarSampul" :style="{backgroundImage: 'url('+b.urlGambarSampul+')'}"></div>
                  <div>{{b.judul}}</div>
                </td>
                <td>{{b.pengarang}}</td>
                <td>{{b.isbn}}</td>
                <td><span class="tag">{{b.kategori}}</span></td>
                <td><span :class="['status', statusClass(b)]">{{statusLabel(b)}}</span></td>
                <td>{{b.jumlahStok ?? '-'}}</td>
                <td>
                  <button class="btn small" @click="openAdjustStock(b)">Adjust Stock</button>
                </td>
              </tr>
            </tbody>
          </table>

          <div class="pager">Showing {{bookStart}}-{{bookEnd}} of {{totalBooks}} books
            <div class="pages">
              <button v-for="n in bookPageArray" :key="n" class="page" :class="{active: n===pageBooks}" @click="pageBooks = n">{{n}}</button>
            </div>
          </div>
        </div>

        <div v-if="activeTab==='users'" class="panel">
          <div class="actions-row">
                  <div class="left-actions">
                        <button class="btn primary" @click="openAddUser">+ Add Pustakawan</button>
                        <button class="btn primary" style="margin-left:8px" @click="openAddMember">+ Add Member</button>
                          <button class="btn" disabled title="Filters not available">Filters</button>
                      </div>
            <div class="right-actions">
              <button class="btn" @click="exportCSV">Export</button>
            </div>
          </div>

          <table class="table">
            <thead>
              <tr>
                <th>User ID</th>
                <th>Name</th>
                <th>Email</th>
                <th>User Type</th>
                <th>Status</th>
              </tr>
            </thead>
            <tbody>
              <!-- Users list: show backend error if endpoint unavailable -->
              <tr v-if="usersError">
                <td colspan="5" class="empty" style="color: #ef4444;">Error loading users: {{usersError}}</td>
              </tr>
              <tr v-else-if="users.length===0">
                <td colspan="5" class="empty">No users found in database.</td>
              </tr>
              <tr v-else v-for="u in paginatedUsers" :key="u.id || u.idUser">
                <td>{{u.id || u.idUser}}</td>
                <td>{{u.name || u.username}}</td>
                <td>{{u.email}}</td>
                <td>{{u.type || u.role}}</td>
                <td>
                  <select 
                    class="status-select" 
                    :class="userStatusClass(u.statusKeanggotaan)" 
                    :value="u.statusKeanggotaan || 'AKTIF'"
                    @change="updateUserStatus(u, $event.target.value)"
                  >
                    <option value="AKTIF">Active</option>
                    <option value="NONAKTIF">Inactive</option>
                  </select>
                </td>
              </tr>
            </tbody>
          </table>

          <div class="pager">Showing {{userStart}}-{{userEnd}} of {{totalUsers}} users
            <div class="pages">
              <button v-for="n in userPageArray" :key="n" class="page" :class="{active: n===pageUsers}" @click="pageUsers = n">{{n}}</button>
            </div>
          </div>
        </div>

        <div v-if="activeTab==='borrowings'" class="panel">
          <div class="actions-row">
            <div class="left-actions">
              <!-- Removed + New Borrowing button -->
              <button class="btn" disabled title="Filters not available">Filters</button>
            </div>
            <div class="right-actions">
              <button class="btn" @click="exportCSV">Export</button>
            </div>
          </div>

          <div class="cards">
            <div class="card">Active Borrowings<br/><span class="num">{{borrowings.length}}</span></div>
            <div class="card">Overdue<br/><span class="num">{{overdueCount}}</span></div>
            <div class="card">Returned Today<br/><span class="num">{{returnedToday}}</span></div>
            <div class="card">Total Fines<br/><span class="num">{{totalFinesDisplay}}</span></div>
          </div>

          <table class="table">
            <thead>
              <tr>
                <th>Borrowing ID</th>
                <th>User</th>
                <th>Book</th>
                <th>Borrow Date</th>
                <th>Due Date</th>
                <th>Status</th>
                  <th>Fine</th>
                  <th>Actions</th>
              </tr>
            </thead>
            <tbody>
              <tr v-if="borrowingsError">
                <td colspan="8" class="empty">Error loading borrowings: {{borrowingsError}}</td>
              </tr>
              <tr v-else-if="borrowings.length===0">
                <td colspan="8" class="empty">No borrowings found.</td>
              </tr>
              <tr v-else v-for="p in paginatedBorrowings" :key="p.idPeminjaman">
                <td>{{formatBorrowId(p.idPeminjaman)}}</td>
                <td>{{p.username}}</td>
                <td>{{p.judulBuku}}</td>
                <td>{{formatDate(p.tanggalPinjam)}}</td>
                <td>{{formatDate(p.tanggalKembali)}}</td>
                <td><span :class="['status', p.status && p.status.toLowerCase()==='overdue' ? 'red' : 'blue']">{{p.status}}</span></td>
                <td>IDR {{ formatCurrency(p.totalDenda || 0) }}</td>
                <td>
                  <button class="btn" @click="returnBorrowing(p.idPeminjaman)" :disabled="p.status && p.status.toLowerCase()==='dikembalikan'">Return</button>
                </td>
              </tr>
            </tbody>
          </table>

          <div class="pager">Showing {{borrowStart}}-{{borrowEnd}} of {{totalBorrowings}} borrowings
            <div class="pages">
              <button v-for="n in borrowPageArray" :key="n" class="page" :class="{active: n===pageBorrowings}" @click="pageBorrowings = n">{{n}}</button>
            </div>
          </div>
        </div>

        <!-- Global Modals (render regardless of active tab) -->
        <!-- Add Book Modal -->
        <div v-if="showAddBook" class="modal-backdrop">
          <div class="modal">
            <h3>Add New Book</h3>
            <div class="form-row"><label>Title</label><input v-model="newBook.judul" /></div>
            <div class="form-row"><label>Author</label><input v-model="newBook.pengarang" /></div>
            <div class="form-row"><label>ISBN</label><input v-model="newBook.isbn" /></div>
            <div class="form-row"><label>Category</label><input v-model="newBook.kategori" /></div>
            <div class="form-row"><label>Copies</label><input type="number" v-model.number="newBook.jumlahStok" min="0" /></div>
            <div class="form-row"><label>Year</label><input type="number" v-model.number="newBook.tahunTerbit" placeholder="2025" /></div>
            <div class="form-row"><label>Cover URL</label><input v-model="newBook.urlGambarSampul" /></div>
            <div class="modal-actions"><button class="btn" @click="closeAddBook">Cancel</button><button class="btn primary" @click="submitAddBook">Save</button></div>
          </div>
        </div>

        <!-- Add User Modal -->
        <div v-if="showAddUser" class="modal-backdrop">
          <div class="modal">
            <h3>Add New Pustakawan</h3>
            <div class="form-row"><label>Username</label><input v-model="newUser.username" /></div>
            <div class="form-row"><label>Email</label><input v-model="newUser.email" /></div>
            <div class="form-row"><label>Password</label><input type="password" v-model="newUser.password" /></div>
            <div class="form-row"><label>NIP</label><input v-model="newUser.nip" /></div>
            <div class="form-row"><label>Jabatan</label><input v-model="newUser.jabatan" placeholder="e.g. Kepala Perpustakaan" /></div>
            <div class="modal-actions"><button class="btn" @click="closeAddUser">Cancel</button><button class="btn primary" @click="submitAddUser">Create</button></div>
          </div>
        </div>

        <!-- Add Member Modal -->
        <div v-if="showAddMember" class="modal-backdrop">
          <div class="modal">
            <h3>Add New Member</h3>
            <div class="form-row"><label>Username</label><input v-model="newMember.username" /></div>
            <div class="form-row"><label>Email</label><input v-model="newMember.email" /></div>
            <div class="form-row"><label>Password</label><input type="password" v-model="newMember.password" /></div>
            <div class="form-row"><label>Phone</label><input v-model="newMember.noTelpon" /></div>
            <div class="form-row"><label>Address</label><input v-model="newMember.alamat" /></div>
            <div class="modal-actions"><button class="btn" @click="closeAddMember">Cancel</button><button class="btn primary" @click="submitAddMember">Create</button></div>
          </div>
        </div>

        <!-- New Borrowing Modal -->
        <div v-if="showNewBorrowing" class="modal-backdrop">
          <div class="modal">
            <h3>New Borrowing</h3>
            <div class="form-row"><label>Member ID</label><input v-model="newBorrow.idPemustaka" /></div>
            <div class="form-row"><label>Book ID</label><input v-model="newBorrow.idBuku" /></div>
            <div class="modal-actions"><button class="btn" @click="closeNewBorrowing">Cancel</button><button class="btn primary" @click="submitNewBorrowing">Create</button></div>
          </div>
        </div>

        <!-- Adjust Stock Modal -->
        <div v-if="showAdjustStock" class="modal-backdrop">
          <div class="modal">
            <h3>Adjust Stock</h3>
            <p style="margin-bottom:12px; font-size:14px; color:#666">Enter amount to add (e.g. 5) or remove (e.g. -2)</p>
            <div class="form-row"><label>Delta</label><input type="number" v-model.number="adjustStockValue" /></div>
            <div class="modal-actions"><button class="btn" @click="closeAdjustStock">Cancel</button><button class="btn primary" @click="submitAdjustStock">Update</button></div>
          </div>
        </div>

      </section>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted, watch } from 'vue';
import axios from 'axios';

const activeTab = ref('books');
const globalSearch = ref('');

const books = ref([]);
const users = ref([]);
const usersError = ref(null);
const borrowingsError = ref(null);
const borrowings = ref([]);

// Pagination state
const pageSize = ref(5);
const pageBooks = ref(1);
const pageUsers = ref(1);
const pageBorrowings = ref(1);

const overdueCount = ref(0);
const returnedToday = ref(0);
const totalFines = ref(0);

const totalFinesDisplay = computed(() => 'IDR ' + formatCurrency(totalFines.value || 0));

const currentTitle = computed(() => {
  if (activeTab.value === 'books') return 'Books';
  if (activeTab.value === 'users') return 'Users';
  if (activeTab.value === 'borrowings') return 'Borrowings';
  return 'Admin Panel';
});

function formatId(id) {
  if (id === undefined || id === null) return '-';
  return String(id).padStart(3, '0');
}
function formatBorrowId(id) { return 'B' + String(id).padStart(3, '0'); }
function formatDate(d) { if (!d) return '-'; try { return new Date(d).toISOString().slice(0, 10); } catch { return '-'; } }
function formatCurrency(val) {
  return Number(val).toLocaleString("id-ID");
}

function statusLabel(b) {
  if (!b) return '';
  if ((b.jumlahStok ?? 0) <= 0) return 'Unavailable';
  return b.status ?? 'Available';
}
function statusClass(b) {
  const lab = statusLabel(b).toLowerCase();
  if (lab.includes('overdue') || lab.includes('borrow')) return 'red';
  if (lab.includes('unavail')) return 'gray';
  return 'green';
}

function userStatusClass(status) {
  if (!status || status === 'AKTIF') return 'green';
  if (status === 'NONAKTIF') return 'red';
  return 'gray';
}

async function updateUserStatus(user, newStatus) {
  const oldStatus = user.statusKeanggotaan;
  // Optimistic UI update
  user.statusKeanggotaan = newStatus;
  
  try {
    const token = sessionStorage.getItem("token");
    const tokenType = sessionStorage.getItem("tokenType") || "Bearer";
    const userId = user.id || user.idUser;
    
    if (!userId) throw new Error("User ID is missing");

    await axios.put(`/api/pemustaka/${userId}/status`, 
      { statusKeanggotaan: newStatus },
      { headers: { 'Authorization': `${tokenType} ${token}` } }
    );
    // Success - keep the new status
  } catch (err) {
    console.error("Failed to update user status:", err);
    // Revert on failure
    user.statusKeanggotaan = oldStatus;
    const msg = err.response?.data?.message || err.message || "Update failed";
    alert(`Failed to update status: ${msg}`);
  }
}

// Category Filter
const uniqueCategories = computed(() => {
  const cats = new Set(books.value.map(b => b.kategori).filter(Boolean));
  return Array.from(cats).sort();
});
const selectedCategory = ref('');

const filteredBooks = computed(() => {
  let res = books.value;
  // Category Filter
  if (selectedCategory.value) {
    res = res.filter(b => b.kategori === selectedCategory.value);
  }
  // Search Filter
  const q = globalSearch.value && globalSearch.value.toLowerCase();
  if (q) {
    res = res.filter(b => (b.judul || '').toLowerCase().includes(q) || (b.pengarang || '').toLowerCase().includes(q) || (b.isbn || '').toLowerCase().includes(q));
  }
  return res;
});

// Pagination computed for books
const totalBooks = computed(() => filteredBooks.value.length);
const bookTotalPages = computed(() => Math.max(1, Math.ceil(totalBooks.value / pageSize.value)));
const bookPageArray = computed(() => Array.from({ length: bookTotalPages.value }, (_, i) => i + 1));
const paginatedBooks = computed(() => {
  const start = (pageBooks.value - 1) * pageSize.value;
  return filteredBooks.value.slice(start, start + pageSize.value);
});
const bookStart = computed(() => totalBooks.value === 0 ? 0 : (pageBooks.value - 1) * pageSize.value + 1);
const bookEnd = computed(() => Math.min(pageBooks.value * pageSize.value, totalBooks.value));

watch(filteredBooks, () => { if (pageBooks.value > bookTotalPages.value) pageBooks.value = bookTotalPages.value; });

async function loadBooks() {
  try {
    const res = await axios.get("/api/buku");
    books.value = res.data || [];
  } catch (e) { books.value = []; }
}

// Users pagination
const totalUsers = computed(() => users.value.length);
const userTotalPages = computed(() => Math.max(1, Math.ceil(totalUsers.value / pageSize.value)));
const userPageArray = computed(() => Array.from({ length: userTotalPages.value }, (_, i) => i + 1));
const paginatedUsers = computed(() => {
  const start = (pageUsers.value - 1) * pageSize.value;
  return users.value.slice(start, start + pageSize.value);
});
const userStart = computed(() => totalUsers.value === 0 ? 0 : (pageUsers.value - 1) * pageSize.value + 1);
const userEnd = computed(() => Math.min(pageUsers.value * pageSize.value, totalUsers.value));

watch(users, () => { if (pageUsers.value > userTotalPages.value) pageUsers.value = userTotalPages.value; });

// Compute how many books each user currently has borrowed using `borrowings` data
const borrowedCounts = computed(() => {
  const map = {};
  for (const b of borrowings.value || []) {
    // backend may use idPemustaka or idPemustaka field name; try common keys
    const uid = b.idPemustaka || b.idPemustaka || b.idUser || b.idPemustaka;
    const status = (b.status || '').toString().toLowerCase();
    // Count as borrowed if status is not returned (e.g., not 'dikembalikan')
    if (!uid) continue;
    if (status === 'dikembalikan' || status === 'returned') continue;
    map[uid] = (map[uid] || 0) + 1;
  }
  return map;
});

async function loadBorrowings() {
  borrowingsError.value = null;
  try {
    console.log('Loading borrowings from /api/peminjaman...');
    const token = sessionStorage.getItem("token");
    const tokenType = sessionStorage.getItem("tokenType") || "Bearer";
    console.log('Token exists for borrowings:', !!token);
    if (!token) {
      borrowingsError.value = "No authentication token found. Please login first.";
      borrowings.value = [];
      console.error('No token found in sessionStorage for borrowings');
      return;
    }

    const res = await axios.get("/api/peminjaman", {
      headers: { 'Authorization': `${tokenType} ${token}` }
    });
    console.log('Borrowings response:', res.data);
    borrowings.value = res.data || [];
    overdueCount.value = borrowings.value.filter(b => (b.status || '').toLowerCase().includes('overdue')).length;
    returnedToday.value = 0;
    totalFines.value = borrowings.value.reduce((s, b) => s + (b.totalDenda || 0), 0);
    console.log('Loaded borrowings:', borrowings.value.length);
  } catch (e) {
    console.error('Error loading borrowings:', e);
    borrowings.value = [];
    const status = e.response?.status;
    const data = e.response?.data;
    let msg = e.message || 'Unknown error';
    if (status === 401) msg = 'Unauthorized: Please login with an admin account';
    else if (status === 403) msg = 'Forbidden: You need ADMIN role to access this endpoint';
    else if (status === 404) msg = 'Endpoint not found: /api/peminjaman';
    else if (data && typeof data === 'string') msg = data;
    else if (data && data.error) msg = data.error;
    else if (data) msg = JSON.stringify(data);
    borrowingsError.value = status ? `${status} — ${msg}` : msg;
    console.error('Load borrowings failed:', status, msg);
  }
}

// Load users list from backend
async function loadUsers() {
  usersError.value = null;
  try {
    console.log('Loading users from /api/users...');
    const token = sessionStorage.getItem("token");
    const tokenType = sessionStorage.getItem("tokenType") || "Bearer";
    
    console.log('Token exists:', !!token);
    console.log('Token type:', tokenType);
    console.log('Token value (first 20 chars):', token ? token.substring(0, 20) + '...' : 'null');
    
    if (!token) {
      usersError.value = "No authentication token found. Please login first.";
      users.value = [];
      console.error('No token found in sessionStorage');
      return;
    }
    
    console.log('Making request to /api/users/pemustaka with Authorization header...');
    const res = await axios.get("/api/users/pemustaka", {
      headers: {
        'Authorization': `${tokenType} ${token}`
      }
    });
    console.log('Users response:', res.data);
    
    if (res.data && Array.isArray(res.data)) {
      users.value = res.data;
      usersError.value = null;
      console.log('Loaded users from database:', users.value.length);
    } else {
      users.value = [];
      usersError.value = "Invalid response format from server";
      console.error('Invalid response format:', res.data);
    }
  } catch (err) {
    console.error('Error loading users:', err);
    users.value = [];
    
    const status = err.response?.status;
    const data = err.response?.data;
    let errorMsg = err.message || 'Unknown error';
    
    if (status === 401) {
      errorMsg = "Unauthorized: Please login with an admin account";
    } else if (status === 403) {
      errorMsg = "Forbidden: You need ADMIN role to access this endpoint";
    } else if (status === 404) {
      errorMsg = "Endpoint not found: /api/users";
    } else if (data && typeof data === 'string') {
      errorMsg = data;
    } else if (data && data.error) {
      errorMsg = data.error;
    } else if (data) {
      errorMsg = JSON.stringify(data);
    }
    
    // Report errors to UI; do not fallback to mock users here
    usersError.value = status ? `${status} — ${errorMsg}` : errorMsg;
    console.error('Load users failed:', status, errorMsg);
  }
}

// Borrowings pagination
const totalBorrowings = computed(() => borrowings.value.length);
const borrowTotalPages = computed(() => Math.max(1, Math.ceil(totalBorrowings.value / pageSize.value)));
const borrowPageArray = computed(() => Array.from({ length: borrowTotalPages.value }, (_, i) => i + 1));
const paginatedBorrowings = computed(() => {
  const start = (pageBorrowings.value - 1) * pageSize.value;
  return borrowings.value.slice(start, start + pageSize.value);
});
const borrowStart = computed(() => totalBorrowings.value === 0 ? 0 : (pageBorrowings.value - 1) * pageSize.value + 1);
const borrowEnd = computed(() => Math.min(pageBorrowings.value * pageSize.value, totalBorrowings.value));

watch(borrowings, () => { if (pageBorrowings.value > borrowTotalPages.value) pageBorrowings.value = borrowTotalPages.value; });

onMounted(() => {
  loadBooks();
  loadBorrowings();
  // Development helper: if running on localhost and no token present,
  // perform an automatic login with demo admin credentials so the
  // admin Users view can load real data during local demos.
  (async () => {
    const devAutoLogin = window.location.hostname === 'localhost' || window.location.hostname === '127.0.0.1';
    if (devAutoLogin && !sessionStorage.getItem('token')) {
      try {
        console.log('No token found — performing dev auto-login as axis');
        const res = await axios.post('/api/auth/login', { username: 'axis', password: '123456' });
        const token = res.data?.token;
        const type = res.data?.type || 'Bearer';
        if (token) {
          sessionStorage.setItem('token', token);
          sessionStorage.setItem('tokenType', type);
          console.log('Dev auto-login succeeded');
        }
      } catch (e) {
        console.warn('Dev auto-login failed (ok for production):', e?.response?.status || e.message);
      }
    }

    // Auto-load users when admin page is opened
    await loadUsers();
  })();
});

function setTab(t) { 
  activeTab.value = t; 
  // Auto-load data when switching tabs
  if (t === 'users' && users.value.length === 0 && !usersError.value) {
    loadUsers();
  }
  if (t === 'borrowings' && borrowings.value.length === 0) {
    loadBorrowings();
  }
}

// Logout function
function handleLogout() {
  if (confirm('Are you sure you want to logout?')) {
    sessionStorage.removeItem('token');
    sessionStorage.removeItem('tokenType');
    window.location.href = '/LoginPage';
  }
}

// --- Button handlers and modals ---
const showAddBook = ref(false);
const newBook = ref({ judul: '', pengarang: '', isbn: '', kategori: '', jumlahStok: 1, urlGambarSampul: '' });
function openAddBook() { showAddBook.value = true; }
function closeAddBook() { showAddBook.value = false; }

async function submitAddBook() {
  try {
    const payload = {
      judul: newBook.value.judul,
      pengarang: newBook.value.pengarang,
      isbn: newBook.value.isbn,
      kategori: newBook.value.kategori,
      jumlahStok: Number(newBook.value.jumlahStok) || 0,
      urlGambarSampul: newBook.value.urlGambarSampul,
      tahunTerbit: Number(newBook.value.tahunTerbit) || 2025,
      hargaSewa: 0,
      dendaPerHari: 5000, // Default Rp 5.000 per hari
      status: 'Tersedia'
    };
    await axios.post("/api/buku", payload);
    await loadBooks();
    closeAddBook();
    newBook.value = { judul: '', pengarang: '', isbn: '', kategori: '', jumlahStok: 1, urlGambarSampul: '' };
  } catch (err) { 
    console.error('Add book failed', err); 
    const msg = err.response?.data && typeof err.response.data === 'string' 
                ? err.response.data 
                : (err.response?.data?.error || err.message);
    alert('Failed to add book: ' + msg); 
  }
}

// Add Pustakawan (Register Librarian)
const showAddUser = ref(false);
const newUser = ref({ username: '', email: '', password: '', nip: '', jabatan: '' });
function openAddUser() { showAddUser.value = true; }
function closeAddUser() { showAddUser.value = false; }
async function submitAddUser() {
  if (!newUser.value.username || !newUser.value.password || !newUser.value.nip || !newUser.value.jabatan) { 
    alert('Username, password, NIP, and Jabatan are required'); 
    return; 
  }
  try {
    const payload = { 
      username: newUser.value.username, 
      email: newUser.value.email, 
      password: newUser.value.password,
      nip: newUser.value.nip,
      jabatan: newUser.value.jabatan
    };
    await axios.post("/api/auth/register/pustakawan", payload);
    alert('Pustakawan created successfully');
    await loadUsers(); // Note: this will only reload members if endpoint is /pemustaka :/
    closeAddUser();
    newUser.value = { username: '', email: '', password: '', nip: '', jabatan: '' };
  } catch (err) { 
    console.error(err); 
    const msg = err.response?.data && typeof err.response.data === 'string' 
                ? err.response.data 
                : (err.response?.data?.message || err.message);
    alert('Failed to create pustakawan: ' + msg); 
  }
}

// Add Member (Pemustaka)
const showAddMember = ref(false);
const newMember = ref({ username: '', email: '', password: '', noTelpon: '', alamat: '' });
function openAddMember() { showAddMember.value = true; }
function closeAddMember() { showAddMember.value = false; }
async function submitAddMember() {
   if (!newMember.value.username || !newMember.value.password || !newMember.value.email) {
       alert('Username, Email, and Password are required');
       return;
   }
   try {
       await axios.post("/api/auth/register", newMember.value);
       alert('Member created successfully');
       await loadUsers();
       closeAddMember();
       newMember.value = { username: '', email: '', password: '', noTelpon: '', alamat: '' };
   } catch (err) { 
       console.error(err);
       const msg = err.response?.data && typeof err.response.data === 'string' 
                           ? err.response.data 
                           : (err.response?.data?.message || err.message);
       alert('Failed to create member: ' + msg); 
   }
}

// New Borrowing (simple)
const showNewBorrowing = ref(false);
const newBorrow = ref({ idPemustaka: '', idBuku: '' });
function openNewBorrowing() { showNewBorrowing.value = true; }
function closeNewBorrowing() { showNewBorrowing.value = false; }
async function submitNewBorrowing() {
  if (!newBorrow.value.idPemustaka || !newBorrow.value.idBuku) { alert('Member ID and Book ID required'); return; }
  try {
    const payload = { idPemustaka: Number(newBorrow.value.idPemustaka), idBuku: Number(newBorrow.value.idBuku) };
    await axios.post("/api/peminjaman/pinjam", payload);
    await loadBorrowings();
    alert('Borrowing created');
    closeNewBorrowing();
    newBorrow.value = { idPemustaka: '', idBuku: '' };
  } catch (err) { 
    console.error(err); 
    const msg = err.response?.data?.message || err.response?.data || err.message || 'Unknown error';
    alert('Failed to create borrowing: ' + (typeof msg === 'string' ? msg : JSON.stringify(msg))); 
  }
}

// Return borrowing
async function returnBorrowing(id) {
  if (!confirm('Mark this borrowing as returned?')) return;
  try {
    await axios.put(`/api/peminjaman/kembali/${id}`);
    await loadBorrowings();
    alert('Borrowing marked as returned');
  } catch (err) { 
    console.error(err); 
    const msg = err.response?.data?.message || err.response?.data || err.message || 'Unknown error';
    alert('Failed to return borrowing: ' + (typeof msg === 'string' ? msg : JSON.stringify(msg))); 
  }
}

// Adjust Stock
const showAdjustStock = ref(false);
const adjustStockId = ref(null);
const adjustStockValue = ref(0);

function openAdjustStock(book) {
  adjustStockId.value = book.idBuku || book.id;
  adjustStockValue.value = 0;
  showAdjustStock.value = true;
}
function closeAdjustStock() { showAdjustStock.value = false; }
async function submitAdjustStock() {
  if (!adjustStockId.value || adjustStockValue.value === 0) { alert('Invalid stock value'); return; }
  try {
    const token = sessionStorage.getItem("token");
    const tokenType = sessionStorage.getItem("tokenType") || "Bearer";
    await axios.patch(`/api/buku/${adjustStockId.value}/stok`, 
      { delta: Number(adjustStockValue.value) },
      { headers: { 'Authorization': `${tokenType} ${token}` } }
    );
    await loadBooks();
    alert('Stock updated');
    closeAdjustStock();
  } catch (err) { console.error(err); alert('Failed to adjust stock'); }
}



// Export current tab to CSV
function exportCSV() {
  let rows = [];
  if (activeTab.value === 'books') {
    rows = books.value.map(b => ({ id: b.idBuku ?? b.id ?? b.id_buku ?? null, judul: b.judul, pengarang: b.pengarang, isbn: b.isbn, kategori: b.kategori, stok: b.jumlahStok }));
  } else if (activeTab.value === 'borrowings') {
    rows = borrowings.value.map(p => ({ id: p.idPeminjaman, user: p.username, book: p.judulBuku, borrowDate: p.tanggalPinjam, dueDate: p.tanggalKembali, status: p.status, fine: p.totalDenda }));
  } else {
    rows = users.value.slice();
  }
  if (rows.length === 0) { alert('No data to export'); return; }
  const keys = Object.keys(rows[0]);
  const csv = [keys.join(',')].concat(rows.map(r => keys.map(k => '"' + String(r[k] ?? '') + '"').join(','))).join('\n');
  const blob = new Blob([csv], { type: 'text/csv;charset=utf-8;' });
  const url = URL.createObjectURL(blob);
  const a = document.createElement('a');
  a.href = url; a.download = `${activeTab.value}-export.csv`; document.body.appendChild(a); a.click(); a.remove(); URL.revokeObjectURL(url);
}

</script>

<style scoped>
.admin-page{ display:flex; min-height:100vh; font-family:Inter, Arial, sans-serif; background:#f6f7f8; }
.sidebar{ width:240px; background:#ffffff; border-right:1px solid #f1f3f5; padding:20px 16px; display:flex; flex-direction:column; }
.brand{ display:flex; align-items:center; gap:12px; padding:6px 6px 18px 6px }
.logo{ width:40px; height:40px; border-radius:50%; background:#0f1724; display:flex; align-items:center; justify-content:center }
.brand-text{ font-weight:700; color:#0f1724 }
.nav{ display:flex; flex-direction:column; gap:6px; margin-top:6px }
.nav-item{ display:flex; align-items:center; gap:12px; color:#374151; padding:10px 12px; border-radius:8px; background:transparent; border:none; text-align:left; cursor:pointer }
.nav-item .icon{ width:24px; display:inline-flex; align-items:center; justify-content:center; }
.nav-item .label{ font-size:14px }
.nav-item:hover{ background:#f8fafc }
.nav-item.active{ background:#f1f5f9; box-shadow: inset 4px 0 0 0 #0f1724; font-weight:600 }
.main{ flex:1; display:flex; flex-direction:column; }
  .topbar{ display:flex; justify-content:space-between; align-items:center; padding:18px 28px; background:#fff; border-bottom:1px solid #eee; }
  .top-right{ display:flex; align-items:center; gap:12px; }
  .searchbox{ display:block; }
  .searchbox input{ width:360px; max-width:560px; padding:9px 12px; border-radius:8px; border:1px solid #e6e9ee; background:#f5f6fa; box-sizing:border-box }
  .avatar{ width:36px; height:36px; border-radius:50%; background:#e9edf2; flex:0 0 36px }
.content{ padding:24px 32px; }
.page-title{ margin:0; font-size:18px; font-weight:600 }
.cards{ display:flex; gap:12px; margin:16px 0 18px 0 }
.card{ background:#fff; padding:18px; border-radius:10px; min-width:200px; box-shadow:0 1px 4px rgba(0,0,0,0.04); }
.card .num{ font-size:22px; font-weight:700; display:block; margin-top:8px }
/* tabs removed to match screenshots */
.actions-row{ display:flex; justify-content:space-between; align-items:center; margin-bottom:12px }
.left-actions{ display:flex; gap:10px }
.right-actions{ }
.btn{ padding:8px 14px; border-radius:8px; border:1px solid #dfe6ea; background:#fff; cursor:pointer }
.btn.primary{ background:#0f1724; color:#fff; cursor:pointer }
.btn[disabled]{ opacity:0.6; cursor:not-allowed }
.table{ width:100%; border-collapse:collapse; background:#fff; border-radius:10px; overflow:hidden; box-shadow:0 1px 4px rgba(0,0,0,0.04); }
.table th, .table td{ padding:14px 18px; text-align:left; border-bottom:1px solid #f1f3f5; font-size:14px }
.title-cell{ display:flex; align-items:center; gap:12px }
.cover{ width:44px; height:56px; background-size:cover; background-position:center; border-radius:6px }
.tag{ background:#f5f6fa; padding:6px 8px; border-radius:8px; font-size:12px }
.status{ padding:6px 8px; border-radius:8px; font-weight:600; font-size:13px }
.status.green{ color:#16a34a }
.status.red{ color:#ef4444 }
.status.blue{ color:#60a5fa }
.status.gray{ color:#9ca3af }
/* New status-select styling to look like a badge */
.status-select {
  padding: 6px 24px 6px 12px;
  border-radius: 8px;
  border: 1px solid transparent;
  font-size: 13px;
  font-weight: 600;
  cursor: pointer;
  appearance: none;
  background-image: url("data:image/svg+xml;charset=US-ASCII,%3Csvg%20xmlns%3D%22http%3A%2F%2Fwww.w3.org%2F2000%2Fsvg%22%20width%3D%22292.4%22%20height%3D%22292.4%22%3E%3Cpath%20fill%3D%22%231F2937%22%20d%3D%22M287%2069.4a17.6%2017.6%200%200%200-13-5.4H18.4c-5%200-9.3%201.8-12.9%205.4A17.6%2017.6%200%200%200%200%2082.2c0%205%201.8%209.3%205.4%2012.9l128%20127.9c3.6%203.6%207.8%205.4%2012.8%205.4s9.2-1.8%2012.8-5.4L287%2095c3.5-3.5%205.4-7.8%205.4-12.8%200-5-1.9-9.2-5.5-12.8z%22%2F%3E%3C%2Fsvg%3E");
  background-repeat: no-repeat;
  background-position: right 8px center;
  background-size: 8px;
}
.status-select.green { color:#16a34a; background-color: #f0fdf4; }
.status-select.red { color:#ef4444; background-color: #fef2f2; }
.status-select.gray { color:#9ca3af; background-color: #f9fafb; }
.status-select:hover { opacity: 0.8; }

.pager{ margin:14px 0; color:#6b7280; display:flex; justify-content:space-between; align-items:center }
.pages .page{ margin-left:8px; padding:6px 10px; border-radius:6px; border:1px solid #e6e9ee; background:#fff }
.pages .page.active{ background:#0f1724; color:#fff }
.empty{ padding:18px; text-align:center; color:#6b7280 }
/* Modal */
.modal-backdrop{ position:fixed; inset:0; background:rgba(0,0,0,0.35); display:flex; align-items:center; justify-content:center; z-index:60 }
.modal{ background:#fff; padding:18px; border-radius:10px; width:420px; box-shadow:0 6px 30px rgba(0,0,0,0.2) }
.modal h3{ margin:0 0 12px 0 }
.form-row{ display:flex; flex-direction:column; gap:6px; margin-bottom:10px }
.form-row label{ font-size:13px; color:#374151 }
.form-row input{ padding:8px 10px; border-radius:8px; border:1px solid #e6e9ee }
.modal-actions{ display:flex; justify-content:flex-end; gap:10px; margin-top:8px }
/* Sidebar Footer */
.sidebar-footer{ margin-top:auto; padding-top:16px; border-top:1px solid #f1f3f5; }
.logout-btn{ width:100%; color:#ef4444 !important; }
.logout-btn:hover{ background:#fef2f2; }
</style>
