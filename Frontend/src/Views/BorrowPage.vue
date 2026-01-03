<template>
  <div class="borrow-page">
    <header class="sticky-header">
      <div class="header-inner">
        <div class="left-section">
          <button class="back-btn" @click="goBack">
            <span class="icon">←</span>
            Back to Book Details
          </button>
          <div class="separator"></div>
          <div class="title-block">
            <h1 class="main-title">Open Library</h1>
            <p class="subtitle">Borrow &amp; Manage Your Books</p>
          </div>
        </div>

        <div class="right-section">
          <div class="user-text">
            <p class="welcome">Welcome back,</p>
            <p class="username">{{ userName }}</p>
          </div>
          <div class="avatar"><span>{{ userInitials }}</span></div>
        </div>
      </div>
    </header>

    <div class="content-wrapper">
      
      <div class="tabs">
        <button
          :class="['tab-btn', mainTab === 'borrow' ? 'active' : '']"
          @click="mainTab = 'borrow'"
        >
          <span class="tab-ic">📚</span>
          Borrow Books
        </button>

        <button
          :class="['tab-btn', mainTab === 'history' ? 'active' : '']"
          @click="mainTab = 'history'"
        >
          <span class="tab-ic">🕒</span>
          My Borrowing History
        </button>
      </div>

      <div class="panel">
        
        <div v-if="mainTab === 'borrow'" class="empty-borrow-placeholder">
          <div class="empty-inner">
            <p class="empty-text">Please choose book from other page</p>
          </div>
        </div>

        <div v-if="mainTab === 'history'" class="history-section-new">
          
          <div class="section-header">
            <h2>My Borrowed Books</h2>
            <p>Track your borrowing history and manage your loan extensions</p>
          </div>

          <div v-if="overdueManagedBooks.length > 0" class="new-alert-banner red">
            <span class="alert-icon">⚠️</span>
            <span>You have outstanding late fees totaling {{ formattedTotalLateFees }}</span>
          </div>

          <div class="sub-tabs-container">
            <div class="sub-tab-track">
              <button 
                :class="['sub-tab-btn', subTab === 'active' ? 'active' : '']" 
                @click="subTab = 'active'"
              >
                Active <span class="sub-tab-count">{{ activeManagedBooks.length }}</span>
              </button>
              <button 
                :class="['sub-tab-btn', subTab === 'overdue' ? 'active' : '']" 
                @click="subTab = 'overdue'"
              >
                Overdue <span class="sub-tab-count red">{{ overdueManagedBooks.length }}</span>
              </button>
              <button 
                :class="['sub-tab-btn', subTab === 'history' ? 'active' : '']" 
                @click="subTab = 'history'"
              >
                History
              </button>
            </div>
          </div>

          <div class="new-book-list">
            <div v-for="book in currentSubTabBooks" :key="book.id" class="new-book-card">
              
              <div class="card-top-badge">
                <span v-if="book.status === 'active'" class="badge-status blue">
                  <span class="ic">📖</span> Active
                </span>
                <span v-if="book.status === 'overdue'" class="badge-status red">
                  ⚠️ Overdue
                </span>
                <span v-if="book.status === 'history'" class="badge-status green">
                  🔄 Returned
                </span>
              </div>

              <div class="card-main-content">
                <div class="card-col-img">
                   <img :src="book.image" :alt="book.title" class="card-book-img" />
                </div>

                <div class="card-col-details">
                  <h3 class="nb-title">{{ book.title }}</h3>
                  <p class="nb-author">{{ book.author }}</p>

                  <div class="nb-meta-list">
                    <div class="nb-meta-item">
                      <span class="ic">📅</span> Borrowed: {{ formatDate(book.borrowedDate) }}
                    </div>
                    <div class="nb-meta-item">
                      <span class="ic">🕒</span> Due: {{ formatDate(book.dueDate) }}
                    </div>
                    <div v-if="book.returnedDate" class="nb-meta-item">
                      <span class="ic">🔄</span> Returned: {{ formatDate(book.returnedDate) }}
                    </div>
                    
                    
                  </div>
                </div>
              </div> <div class="card-divider"></div>

              <div class="card-footer">
                <div class="footer-left">
                  <div class="fee-row">
                    <span class="fee-label">Borrowing Fee</span>
                    <span class="fee-value">IDR {{ formatCurrency(book.fee) }}</span>
                  </div>
                  <div v-if="book.lateFee > 0" class="fee-row">
                    <span class="fee-label red">Late Fee ({{ book.lateDays }} days overdue)</span>
                    <span class="fee-value red">IDR {{ formatCurrency(book.lateFee) }}</span>
                  </div>
                  <div class="fee-row status-row">
                    <span class="fee-label">Payment Status</span>
                    <span v-if="book.paymentStatus === 'Paid'" class="badge-payment green" style="margin-left:8px;">Paid</span>
                    <span v-else-if="book.paymentStatus === 'Pending'" class="badge-payment orange" style="margin-left:8px;">Pending</span>
                  </div>
                </div>

                <div class="footer-right">
                  <!-- payment badge moved next to label -->

                  <!-- Extend Loan button removed -->

                  <!-- Pay Now button removed -->
                </div>
              </div>

               <div v-if="book.status === 'overdue'" class="card-inner-alert red">
                 <span class="ic">⚠️</span> Please pay the late fee to avoid account restrictions.
               </div>

            </div>
          </div> </div>

      </div>
    </div>

    <div v-if="showModal && selectedBook && selectedBook.id" class="modal-overlay">
      <div class="modal">
        <button class="modal-close" @click="closeModal">×</button>

        <template v-if="modalContext === 'borrow'">
          <div v-if="modalStep === 'confirm'">
            <div class="modal-header">
              <h3>Confirm Borrowing</h3>
              <p>Review the borrowing details before proceeding</p>
            </div>
            <div class="modal-book">
              <div class="modal-book-img">
                <img :src="selectedBook.image" alt="book" />
              </div>
              <div class="modal-book-text">
                <h4>{{ selectedBook.title }}</h4>
                <p>{{ selectedBook.author }}</p>
              </div>
            </div>
            <div class="modal-divider"></div>
            <div class="modal-details">
              <div class="detail-row">
                <div class="l"><span class="d-ic">📅</span> Borrowing Period</div>
                <div class="r">7 days</div>
              </div>
              <div class="detail-row">
                <div class="label">Start Date</div>
                <input type="date" v-model="startDate" class="date-input" :min="minStartDate" />
              </div>
              <div class="detail-row">
                <div class="label">Due Date</div>
                <input type="date" v-model="dueDate" class="date-input" readonly disabled />
              </div>
              <div class="modal-divider"></div>
              <div class="payment-box">
                <div class="payment-row">
                  <span>Borrowing Fee</span>
                  <span class="muted">IDR {{ formatCurrency(selectedBook.fee || 0) }}</span>
                </div>
                <div class="payment-row">
                  <span>Total Amount</span>
                  <span class="bold">IDR {{ formatCurrency(selectedBook.fee || 0) }}</span>
                </div>
              </div>
            </div>
            <div class="modal-alert blue">
              <span class="alert-ic">ⓘ</span>
              Late returns may incur a fine of IDR {{ formatCurrency(selectedBook.dendaPerHari || 1000) }} per day.
            </div>
            <div class="modal-footer">
              <button class="btn ghost" @click="closeModal">Cancel</button>
              <button class="btn dark" @click="proceedToPayment">Proceed →</button>
            </div>
          </div>

          <div v-else-if="modalStep === 'payment'">
            <div class="modal-header center">
              <h3>Payment on The Spot</h3>
              <p>Payment is made directly at the Library</p>
            </div>
            <div class="modal-book">
              <div class="modal-book-img"><img :src="selectedBook.image" alt="book" /></div>
              <div class="modal-book-text">
                <h4>{{ selectedBook.title }}</h4><p>{{ selectedBook.author }}</p>
              </div>
            </div>
            <div class="modal-divider"></div>
            <div class="payment-summary">
              <div class="summary-row"><span class="muted">Total Amount</span><span class="total">IDR {{ formatCurrency(selectedBook.fee || 0) }}</span></div>
              <div class="summary-row"><span class="muted">Borrowing by</span><span>{{ userName }}</span></div>
            </div>
            <div class="modal-divider"></div>
            <div class="modal-details">
              <div class="detail-row"><div class="l">Start Date</div><div class="r">{{ formattedStartDate }}</div></div>
              <div class="detail-row"><div class="l">Due Date</div><div class="r">{{ formattedDueDate }}</div></div>
            </div>
            <div class="modal-alert green">
              <span class="alert-ic">ⓘ</span>
              Show this info to the librarian for payment.
            </div>
            <div class="modal-footer">
              <button class="btn ghost" @click="modalStep = 'confirm'">Back</button>
              <button class="btn dark" @click="finishPayment">Next →</button>
            </div>
          </div>

          <div v-else-if="modalStep === 'success'">
            <div class="success-top">
              <div class="success-icon">✓</div>
              <h3>Borrowing Confirmed!</h3>
              <p class="muted center">Your book has been added to your borrowed items</p>
            </div>
            <div class="success-card">
              <div class="success-book">
                <img :src="selectedBook.image" alt="book" />
                <div><h4>{{ selectedBook.title }}</h4><p>{{ selectedBook.author }}</p></div>
              </div>
              <div class="modal-divider"></div>
              <div class="success-details">
                <div class="detail-row"><div class="l muted">Due Date</div><div class="r">{{ formattedDueDate }}</div></div>
                <div class="detail-row"><div class="l muted">Amount Paid</div><div class="r paid">IDR 70.000,00</div></div>
              </div>
            </div>
            <div class="modal-footer">
              <button class="btn ghost" @click="mainTab = 'history'; closeModal()">View Borrowed Books</button>
              <button class="btn dark" @click="closeModal">Done</button>
            </div>
          </div>
        </template>

        <template v-else-if="modalContext === 'extend'">
          <div v-if="modalStep === 'confirm'">
            <div class="modal-header">
              <h3>Extend Loan Period</h3>
              <p>Review the extension details before proceeding</p>
            </div>
            <div class="modal-book">
              <div class="modal-book-img">
                <img :src="selectedBook.image" alt="book" />
              </div>
              <div class="modal-book-text">
                <h4>{{ selectedBook.title }}</h4>
                <p>{{ selectedBook.author }}</p>
              </div>
            </div>
            <div class="modal-divider"></div>
            <div class="ext-grid">
              <div class="ext-row">
                <span class="ext-label"><span class="d-ic">📅</span> Current Due Date</span>
                <span class="ext-val">{{ formatDate(selectedBook.dueDate) }}</span>
              </div>
              <div class="ext-row">
                <span class="ext-label"><span class="d-ic">📅</span> New Due Date</span>
                <span class="ext-val val-green">{{ extendedDueDate }}</span>
              </div>
              <div class="ext-row">
                <span class="ext-label"><span class="d-ic">🔄</span> Extension Period</span>
                <span class="ext-val">+7 days</span>
              </div>
              <div class="ext-row">
                <span class="ext-label"><span class="d-ic">🔄</span> Extensions Used</span>
                <span class="ext-val">{{ selectedBook.extensionsUsed + 1 }}/{{ selectedBook.maxExtensions }}</span>
              </div>
            </div>
            <div class="modal-divider"></div>
            <div class="ext-fee-box">
              <span class="label">Extension Fee</span>
              <span class="amount">IDR 10.000</span>
            </div>
            <div class="modal-alert blue">
              <span class="alert-ic">🛡️</span>
              You can extend this book 1 more time after this extension.
            </div>
            <div class="modal-footer">
              <button class="btn ghost" @click="closeModal">Cancel</button>
              <button class="btn dark" @click="proceedToExtensionSuccess">Proceed to Payment</button>
            </div>
          </div>

          <div v-else-if="modalStep === 'success'">
            <div class="success-top">
              <div class="success-icon">✓</div>
              <h3>Borrowing Confirmed!</h3>
              <p class="muted center">
                Your book is already extended and the book has been added to your borrowed items
              </p>
            </div>
            <div class="success-card">
              <div class="success-book">
                <img :src="selectedBook.image" alt="book" />
                <div>
                  <h4>{{ selectedBook.title }}</h4>
                  <p>{{ selectedBook.author }}</p>
                </div>
              </div>
              <div class="modal-divider"></div>
              <div class="success-details">
                <div class="detail-row">
                  <div class="l muted">Due Date</div>
                  <div class="r">{{ extendedDueDate }}</div>
                </div>
                <div class="detail-row">
                  <div class="l muted">Amount Paid</div>
                  <div class="r paid">IDR 10.000,00</div>
                </div>
                <div class="detail-row">
                  <div class="l muted">Book ID</div>
                  <div class="r">{{ selectedBook.id }}</div>
                </div>
              </div>
            </div>
            <div class="modal-alert blue">
              <span class="alert-ic">ⓘ</span>
              Late returns may incur a fine of IDR {{ formatCurrency(selectedBook.dendaPerHari || 1000) }} per day. Please return the book on time.
            </div>
            <div class="modal-footer">
              <button class="btn ghost" @click="closeModal">View My Borrowed Books</button>
              <button class="btn dark" @click="closeModal">Done</button>
            </div>
          </div>
        </template>

      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, watch, onMounted } from "vue";
import { useRouter, useRoute } from "vue-router";
import axios from "axios";

const router = useRouter();
const route = useRoute();

// Get book ID from route if available (when navigating from BookDetails)
const routeBookId = route.params.id ? parseInt(route.params.id) : null;

function goBack() { router.back(); }

/* ============================
   STATE MANAGEMENT
============================ */
// Ambil tab dari query parameter URL, default ke 'history'
const mainTab = ref('borrow');
const subTab = ref("active");

const availableBooks = ref([]); 

// Fetch available books from API
// Fetch available books from API
async function loadAvailableBooks() {
  try {
    const res = await axios.get("/api/buku");
    const books = res.data || [];
    console.log("Available books from API (RAW):", books);
    
    // Also fetch user's borrowing history (if logged in) to mark books borrowed by user
    let history = [];
    try {
      if (sessionStorage.getItem("token")) {
        const hres = await axios.get("/api/peminjaman/history");
        history = hres.data || [];
        console.log('User borrowing history:', history);
      }
    } catch (err) {
      console.warn('Failed to load user history while computing available books:', err);
    }

    // Fetch all borrow records (used to determine next available return date for sold-out books)
    let allBorrows = [];
    try {
      if (sessionStorage.getItem("token")) {
        const allRes = await axios.get("/api/peminjaman");
        allBorrows = allRes.data || [];
        console.log('All borrow records:', allBorrows);
      } else {
        console.debug('No JWT in sessionStorage; skipping fetch of global borrow records');
      }
    } catch (err) {
      // Non-ok responses are expected for non-admin users
      if (err.response?.status !== 403 && err.response?.status !== 401) {
        console.warn('Failed to load global borrow records:', err);
      }
    }

    // Build a lookup by book title for active borrows
    const activeBorrowByTitle = {};
    for (const h of history) {
      const status = (h.status || '').toString().toUpperCase();
      if (status === 'DIPINJAM' || status === 'PINJAM' || status === 'BORROWED') {
        const title = (h.judulBuku || '').trim();
        if (title) activeBorrowByTitle[title] = h;
      }
    }

    // Build a lookup of upcoming (non-overdue) return dates per book title
    const upcomingReturnByTitle = {};
    try {
      const todayStart = new Date();
      todayStart.setHours(0,0,0,0);
      for (const p of allBorrows) {
        const status = (p.status || '').toString().toUpperCase();
        if (status === 'DIPINJAM' || status === 'PINJAM' || status === 'BORROWED') {
          if (!p.tanggalKembali) continue;
          const ret = new Date(p.tanggalKembali);
          ret.setHours(0,0,0,0);
          if (ret.getTime() < todayStart.getTime()) continue;
          const title = (p.judulBuku || '').trim();
          if (!title) continue;
          upcomingReturnByTitle[title] = upcomingReturnByTitle[title] || [];
          upcomingReturnByTitle[title].push(p);
        }
      }
    } catch (err) {
      console.warn('Error building upcomingReturnByTitle:', err);
    }

    // Transform API response to UI model
    availableBooks.value = books.map((book) => {
      // Cek field stok dengan berbagai kemungkinan nama field
      // BukuSearchResponseDto menggunakan jumlahStok (camelCase)
      const rawStok = book.jumlahStok ?? book.jumlah_stok ?? book.stok ?? book.stock;
      
      // Cek apakah stok null/undefined terlebih dahulu
      // Jika null/undefined, anggap masih ada stok (bisa dipinjam)
      // Jika ada nilai, konversi ke number dan cek apakah <= 0
      const soldOut = (rawStok !== null && rawStok !== undefined) 
        ? (Number(rawStok) <= 0) 
        : false; // Jika null/undefined, anggap masih ada stok
      
      const title = (book.judul || '').trim();
      const userBorrowEntry = activeBorrowByTitle[title];
      const borrowedByUser = !!userBorrowEntry;
      
      let borrowedUntil = null;
      if (userBorrowEntry && userBorrowEntry.tanggalPinjam) {
        const bd = new Date(userBorrowEntry.tanggalPinjam);
        bd.setDate(bd.getDate() + 7);
        borrowedUntil = bd.toLocaleDateString('id-ID');
      }

      if (soldOut) {
        try {
          const upcoming = upcomingReturnByTitle[title] || [];
          if (upcoming.length > 0) {
            const dates = upcoming.map(x => new Date(x.tanggalKembali).getTime());
            const minTs = Math.min(...dates);
            const dt = new Date(minTs);
            borrowedUntil = dt.toLocaleDateString('id-ID');
          }
        } catch (err) {
          console.warn('Error computing next available date for', title, err);
        }
      }

      // Pastikan ID buku ada - jika tidak ada, log warning
      const bookId = book.idBuku;
      if (!bookId) {
        console.warn(`WARNING: Book "${title}" tidak memiliki idBuku!`, book);
      }

      console.log(`Book "${title}": rawStok=${rawStok}, soldOut=${soldOut} (type: ${typeof soldOut}), borrowedByUser=${borrowedByUser} (type: ${typeof borrowedByUser}), id=${bookId}`);

      return {
        id: bookId, 
        title: title,
        author: book.pengarang || '',
        image: book.urlGambarSampul || "https://via.placeholder.com/80x120",
        soldOut: Boolean(soldOut),
        borrowedByUser: Boolean(borrowedByUser),
        borrowedUntil,
        fee: book.hargaSewa || 0,
        dendaPerHari: book.dendaPerHari || 1000,
        dueDate: addDaysISO(new Date().toISOString().split("T")[0], 7),
        _bookTitle: title,
        _bookAuthor: book.pengarang
      };
    });

    console.log("Transformed available books:", availableBooks.value);
  } catch (err) {
    console.error("Error loading available books:", err);
  }
}


// Helper function to get book ID by title
async function getBookIdByTitle(title) {
  try {
    // GUNAKAN AXIOS
    const searchRes = await axios.get("/api/buku/search", {
      params: { keyword: title }
    });
    const results = searchRes.data || [];
    const exactMatch = results.find(b => b.judul === title);
    if (exactMatch) {
      return null;
    }
  } catch (err) {
    console.warn("Error searching for book:", err);
  }
  return null;
}

// Managed books dari API (history peminjaman user)
const managedBooks = ref([]);

const activeManagedBooks = computed(() => managedBooks.value.filter(b => b.status === "active"));
const overdueManagedBooks = computed(() => managedBooks.value.filter(b => b.status === "overdue"));
const historyManagedBooks = computed(() => managedBooks.value.filter(b => b.status === "history"));

// Total late fees (sum of lateFee on overdue items)
const totalLateFees = computed(() => {
  return overdueManagedBooks.value.reduce((sum, b) => {
    const n = Number(b.lateFee || 0);
    return sum + (isNaN(n) ? 0 : n);
  }, 0);
});

const formattedTotalLateFees = computed(() => {
  return 'IDR ' + formatCurrency(totalLateFees.value);
});

// Helper to get books based on current subTab
const currentSubTabBooks = computed(() => {
    if (subTab.value === 'active') return activeManagedBooks.value;
    if (subTab.value === 'overdue') return overdueManagedBooks.value;
    if (subTab.value === 'history') return historyManagedBooks.value;
    return [];
})

// User Info
const userName = ref("User");
const userInitials = ref("U");

/* ============================
   MODAL STATE
============================ */
const showModal = ref(false);
const selectedBook = ref({
  id: null,
  title: "",
  author: "",
  image: "",
  fee: 0
});
const modalStep = ref("confirm");
const modalContext = ref("borrow");
const startDate = ref(""); 
const dueDate = ref("");

/* ============================
   HELPERS
============================ */
// Format date to DD/MM/YYYY as per design images
function formatDate(dateStr) {
  if (!dateStr) return "-";
  const d = new Date(dateStr);
  return d.toLocaleDateString("id-ID", { month: "numeric", day: "numeric", year: "numeric" });
}

function formatCurrency(val) {
  return Number(val).toLocaleString("id-ID");
}

function addDaysISO(isoDate, days) {
  const d = new Date(isoDate); d.setDate(d.getDate() + days);
  return d.toISOString().split("T")[0];
}

const extendedDueDate = computed(() => {
  if (!selectedBook.value) return "-";
  const current = new Date(selectedBook.value.dueDate);
  current.setDate(current.getDate() + 7);
  return current.toLocaleDateString("id-ID", { month: 'numeric', day: 'numeric', year: 'numeric' });
});

const minStartDate = computed(() => new Date().toISOString().split("T")[0]);
const formattedStartDate = computed(() => startDate.value ? new Date(startDate.value).toLocaleDateString("id-ID") : "-");
const formattedDueDate = computed(() => dueDate.value ? new Date(dueDate.value).toLocaleDateString("id-ID") : "-");

watch(startDate, (val) => {
  if (!val) { dueDate.value = ""; return; }
  dueDate.value = addDaysISO(val, 7);
});

/* ============================
   LOAD DATA & ACTIONS
============================ */
async function loadUserDataFromSession() {
    try {
        const idUser = sessionStorage.getItem("idUser");
        let username = sessionStorage.getItem("username");
        
        // Jika username belum ada di sessionStorage, coba ambil dari userData
        if (!username) {
          const userData = sessionStorage.getItem("userData");
          if (userData) {
            const parsedData = JSON.parse(userData);
            username = parsedData.username || parsedData.nama || "User";
            console.log("Username from userData:", username);
          }
        }
        
        if (idUser && username) {
          userName.value = username;
          userInitials.value = username.split(" ").map(n => n[0]).join("").toUpperCase();
          console.log("Loaded user data - username:", username, "initials:", userInitials.value);
        } else {
          console.warn("User data not found - idUser:", idUser, "username:", username);
        }
        
        return idUser;
      } catch (err) {
        console.error("Error loading user data:", err);
      }
}

// Load borrowing history dari API (uses JWT token via Principal)
async function loadBorrowingHistory() {
    try {
        // GUNAKAN AXIOS
        const res = await axios.get("/api/peminjaman/history");
        const history = res.data || [];
        console.log("Borrowing history from API:", history);
        
    // Transform API response ke format UI
    managedBooks.value = await Promise.all(history.map(async (item) => {
      const borrowedDate = item.tanggalPinjam ? new Date(item.tanggalPinjam) : null;
      const dueDate = borrowedDate ? new Date(borrowedDate.getTime() + 7 * 24 * 60 * 60 * 1000) : null;
      const returnedDate = item.tanggalKembali ? new Date(item.tanggalKembali) : null;

      let status = "history";
      let lateDays = 0;

      const rawStatus = (item.status || "").toString().toUpperCase();
      if (rawStatus === "DIPINJAM" || rawStatus === "PINJAM" || rawStatus === "BORROWED") {
        if (dueDate && new Date() > dueDate) {
          status = "overdue";
          lateDays = Math.max(0, Math.ceil((new Date() - dueDate) / (1000 * 60 * 60 * 24)));
        } else {
          status = "active";
        }
      } else if (rawStatus === "DIKEMBALIKAN" || rawStatus === "RETURNED" || rawStatus === "KEMBALI") {
        status = "history";
      }

      let bookImage = "https://via.placeholder.com/80x120";
      let bookFee = 0;
      let bookAuthor = item.username || "";
      let idBuku = null;
      let bookDendaPerHari = 1000; // Default fallback

      try {
        const match = availableBooks.value.find(b => b.title === item.judulBuku);
        if (match) {
          bookImage = match.image || bookImage;
          bookFee = match.fee || 0;
          bookAuthor = match.author || bookAuthor;
          idBuku = match.id;
          bookDendaPerHari = match.dendaPerHari || 1000;
        } else {
          // GUNAKAN AXIOS
          const searchRes = await axios.get("/api/buku/search", {
            params: { keyword: item.judulBuku }
          });
          if (searchRes.data && searchRes.data.length > 0) {
            const foundBook = searchRes.data.find(b => b.judul === item.judulBuku);
            if (foundBook) {
              bookImage = foundBook.urlGambarSampul || bookImage;
              bookFee = foundBook.hargaSewa || 0;
              bookAuthor = foundBook.pengarang || bookAuthor;
              bookDendaPerHari = foundBook.dendaPerHari || 1000;
            }
          }
        }
      } catch (err) {
        console.warn("Could not fetch book image or details:", err);
      }

      let apiLate = Number(item.totalDenda || 0);
      if (isNaN(apiLate)) apiLate = 0;
      const computedLate = (status === 'overdue' && apiLate === 0) ? (lateDays * 5000) : apiLate;

      return {
        id: item.idPeminjaman,
        idBuku: idBuku,
        title: item.judulBuku,
        author: bookAuthor || item.username || "Unknown",
        image: bookImage,
        borrowedDate: item.tanggalPinjam,
        dueDate: dueDate ? dueDate.toISOString().split("T")[0] : null,
        returnedDate: item.tanggalKembali || null,
        extensionsUsed: 0,
        maxExtensions: 2,
        extensionAvailable: true,
        waitingCount: 0,
        fee: bookFee,
        dendaPerHari: bookDendaPerHari,
        lateFee: computedLate,
        lateDays: lateDays,
        paymentStatus: (computedLate && computedLate > 0) ? "Pending" : "Paid",
        status: status
      };
    }));
        
        console.log("Transformed managed books:", managedBooks.value);
    } catch (err) {
        console.error("Error loading borrowing history:", err);
        if (err.response?.status === 401) {
          alert("Session expired. Please login again.");
          router.push("/LoginPage");
        }
        managedBooks.value = [];
    }
}

async function loadBooks() {
  await loadAvailableBooks();
}

async function openBorrowModal(book) {
  console.log("DEBUG openBorrowModal called with book:", book);
  console.log("DEBUG: book.soldOut =", book.soldOut, "type:", typeof book.soldOut);
  console.log("DEBUG: book.borrowedByUser =", book.borrowedByUser, "type:", typeof book.borrowedByUser);
  console.log("DEBUG: book.id =", book.id, "type:", typeof book.id);
  
  if (book.soldOut || book.borrowedByUser) {
    console.log("DEBUG: Modal blocked - soldOut or borrowedByUser is true");
    return;
  }
  
  // Validasi ID buku
  if (!book.id) {
    console.error("ERROR: Book tidak memiliki ID!", book);
    alert("Error: Book ID tidak ditemukan. Silakan refresh halaman dan coba lagi.");
    return;
  }
  
  console.log("DEBUG: Opening modal with book:", book);
  
  selectedBook.value = {
    ...book,
    id: book.id // Pastikan ID ter-set
  };
  
  console.log("DEBUG: selectedBook.value.id =", selectedBook.value.id);
  console.log("DEBUG: selectedBook.value.title =", selectedBook.value.title);
  
  modalContext.value = "borrow";
  showModal.value = true;
  modalStep.value = "confirm";
  
  const t = new Date();
  startDate.value = t.toISOString().split("T")[0];
  dueDate.value = addDaysISO(startDate.value, 7);
}

function proceedToPayment() { modalStep.value = "payment"; }

async function finishPayment() { 
  try {
    if (!sessionStorage.getItem("token")) {
      alert("Sesi Anda telah berakhir. Silakan login kembali.");
      router.push("/LoginPage");
      return;
    }
    
    console.log("DEBUG finishPayment - selectedBook.value:", selectedBook.value);
    
    // Validasi: Cek apakah buku masih tersedia (tidak soldOut dan tidak borrowedByUser)
    if (selectedBook.value.soldOut) {
      alert("Maaf, stok buku ini sedang habis. Silakan pilih buku lain.");
      closeModal();
      return;
    }
    
    if (selectedBook.value.borrowedByUser) {
      alert("Anda sudah meminjam buku ini. Silakan kembalikan terlebih dahulu sebelum meminjam lagi.");
      closeModal();
      return;
    }
    
    let bookId = selectedBook.value.id || routeBookId;
    
    if (!bookId) {
      alert("ID buku tidak ditemukan. Silakan coba lagi atau pilih buku dari halaman detail buku.");
      return;
    }
    
    console.log("DEBUG: Submitting borrow request with:");
    console.log("  - idBuku:", bookId);
    console.log("  - tanggalPinjam:", startDate.value);
    
    const peminjamanRequest = {
      idBuku: bookId,
      tanggalPinjam: startDate.value
    };
    
    console.log("DEBUG: Full request object:", peminjamanRequest);
    
    // GUNAKAN AXIOS
    const res = await axios.post("/api/peminjaman/pinjam", peminjamanRequest);
    
    console.log("Response status:", res.status);
    console.log("Response data:", res.data);
    
    console.log("Borrow successful:", res.data);
    modalStep.value = "success"; 
  } catch (err) {
    console.error("Error borrowing book:", err);
    
    // Handle 401 Unauthorized
    if (err.response?.status === 401) {
      alert("Sesi Anda telah berakhir. Silakan login kembali.");
      router.push("/LoginPage");
      return;
    }
    
    // Extract error message dari response
    let errorMsg = "Gagal meminjam buku";
    let errorTitle = "Error";
    
    if (err.response?.data) {
      const errorData = err.response.data;
      
      // Ambil pesan error yang lebih user-friendly
      if (errorData.message) {
        errorMsg = errorData.message;
      } else if (errorData.error) {
        errorTitle = errorData.error;
        errorMsg = errorData.message || errorData.error;
      } else if (typeof errorData === 'string') {
        errorMsg = errorData;
      }
    } else if (err.message) {
      errorMsg = err.message;
    }
    
    // Tampilkan pesan error yang lebih informatif
    alert(`${errorTitle}\n\n${errorMsg}`);
  }
}

function openExtendModal(book) {
  modalContext.value = "extend";
  selectedBook.value = book;
  showModal.value = true;
  modalStep.value = "confirm";
}

function proceedToExtensionSuccess() {
  modalStep.value = "success";
}

function closeModal() {
  showModal.value = false;
  selectedBook.value = {
    id: null,
    title: "",
    author: "",
    image: "",
    fee: 0
  };
  modalStep.value = "confirm";
  
  // Refresh data setelah modal ditutup
  loadBorrowingHistory();
}

onMounted(async () => {
  // Load initial lists
  try {
    await loadBooks();
    await loadBorrowingHistory();
  } catch (e) {
    console.warn('Error during initial load:', e);
  }

  // Only auto-open the borrow confirm modal when navigation explicitly
  // requested a borrow action (route contains a book id AND tab=borrow).
  if (routeBookId && String(route.query.tab || '').toLowerCase() === 'borrow') {
    try {
      const res = await axios.get(`/api/buku/${routeBookId}`);
      const b = res.data || {};
      selectedBook.value = {
        id: b.idBuku ?? b.id ?? routeBookId,
        title: b.judul ?? b.title ?? 'Unknown',
        author: b.pengarang ?? b.author ?? '',
        image: b.urlGambarSampul ?? b.image ?? 'https://via.placeholder.com/80x120',
        fee: b.hargaSewa ?? b.fee ?? 0,
        dendaPerHari: b.dendaPerHari ?? 1000
      };

      modalContext.value = 'borrow';
      modalStep.value = 'confirm';
      showModal.value = true;

      const t = new Date();
      startDate.value = t.toISOString().split('T')[0];
      dueDate.value = addDaysISO(startDate.value, 7);
    } catch (err) {
      console.warn('Failed to auto-open borrow modal for book id', routeBookId, err);
    }
  }
});

// payAllFees() removed — payment handled externally now

onMounted(async () => {
  await loadUserDataFromSession();
  await loadBooks();
  
  // Load borrowing history (uses JWT token, no need for idUser parameter)
  await loadBorrowingHistory();
  
  const urlParams = new URLSearchParams(window.location.search);
  const tab = urlParams.get('tab');
  mainTab.value = tab === 'history' ? 'history' : 'borrow';
});
</script>

<style scoped>
/* =========================================
   STYLE GROUP: BASE & HEADER (Existing)
   ========================================= */
.borrow-page { background: #fff; min-height: 100vh; font-family: Inter, sans-serif; color: #0f172a; }
.sticky-header { position: fixed; top: 0; left: 0; right: 0; height: 76px; background: #fff; border-bottom: 1px solid #eef0f3; z-index: 50; }
.header-inner { height: 100%; display: flex; align-items: center; justify-content: space-between; padding: 0 56px; }
.left-section { display: flex; align-items: center; gap: 14px; }
.back-btn { font-size: 12px; background: transparent; border: 0; display: flex; gap: 6px; align-items: center; color: #111827; cursor: pointer; padding: 4px 0; }
.separator { width: 1px; height: 22px; background: #e5e7eb; }
.title-block { display: flex; flex-direction: column; gap: 2px; }
.main-title { font-size: 18px; font-weight: 600; margin: 0; line-height: 1.2; }
.subtitle { font-size: 13px; color: #6b7280; margin: 0; }
.right-section { display: flex; align-items: center; gap: 12px; }
.user-text { text-align: right; }
.welcome { font-size: 12px; color: #6b7280; margin: 0; }
.username { font-size: 12px; font-weight: 600; margin: 2px 0 0; color: #111827; }
.avatar { width: 32px; height: 32px; border-radius: 999px; background: #6366f1; color: #fff; display: flex; align-items: center; justify-content: center; font-size: 12px; font-weight: 700; }

/* =========================================
   STYLE GROUP: CONTENT & TABS (Existing)
   ========================================= */
.content-wrapper { padding-top: 104px; padding-left: 56px; padding-right: 56px; max-width: 1400px; margin: 0 auto; padding-bottom: 60px; }
.tabs { display: inline-flex; gap: 6px; background: #f3f4f6; padding: 4px; border-radius: 999px; height: 32px; align-items: center; }
.tab-btn { height: 24px; padding: 0 12px; border: 0; background: transparent; border-radius: 999px; font-size: 12px; color: #111827; display: flex; align-items: center; gap: 6px; cursor: pointer; }
.tab-btn.active { background: #fff; box-shadow: 0 1px 2px rgba(0,0,0,0.08); font-weight: 600; }
.section-header { margin-top: 18px; margin-bottom: 24px; }
.section-header h2 { font-size: 18px; font-weight: 700; margin: 0 0 6px 0; color: #111827; }
.section-header p { font-size: 13px; color: #6b7280; margin: 0; }

/* =========================================
   STYLE GROUP: BORROW LIST (Existing Tab 1)
   ========================================= */
.books-list { display: flex; flex-direction: column; gap: 12px; }
.book-card { position: relative; display: flex; gap: 16px; padding: 16px; border: 1px solid #eceff3; border-radius: 10px; background: #fff; min-height: 128px; }
.book-card.borrowed { background: #fbfafb; }
.book-img { width: 92px; height: 128px; border-radius: 8px; overflow: hidden; background: #f3f4f6; flex-shrink: 0; }
.book-img img { width: 100%; height: 100%; object-fit: cover; }
.book-info { flex: 1; }
.book-header { display: flex; justify-content: space-between; gap: 12px; }
.book-title { font-size: 13px; font-weight: 600; margin: 0 0 4px 0; }
.book-author { font-size: 12px; color: #6b7280; margin: 0; }
.badge { font-size: 10px; padding: 2px 8px; border-radius: 999px; height: max-content; border: 1px solid transparent; }
.badge-green { background: #ecfdf3; color: #16a34a; border-color: #bbf7d0; }
.badge-red { background: #fee2e2; color: #ef4444; border-color: #fecaca; }
.borrow-period { margin-top: 8px; display: flex; align-items: center; gap: 6px; font-size: 12px; color: #6b7280; }
.book-alert { margin-top: 8px; display: flex; gap: 8px; align-items: flex-start; background: #eff6ff; border: 1px solid #bfdbfe; color: #1d4ed8; padding: 8px 10px; border-radius: 8px; font-size: 11px; line-height: 1.4; }
.borrow-btn { 
  margin-top: 8px; 
  background: #0b1020; 
  color: white; 
  border: 0; 
  height: 28px; 
  padding: 0 10px; 
  border-radius: 7px; 
  font-size: 11px; 
  display: inline-flex; 
  align-items: center; 
  gap: 6px; 
  cursor: pointer; 
  width: max-content;
  position: relative;
  z-index: 1;
  pointer-events: auto;
}
.borrow-btn:disabled { 
  background: #9ca3af; 
  cursor: not-allowed;
  pointer-events: none;
}
.borrow-btn:not(:disabled):hover {
  background: #1a1f2e;
  opacity: 0.9;
}


/* =========================================
   STYLE GROUP: NEW HISTORY/MANAGE TAB STYLES
   ========================================= */

/* New Alert Banner */
.new-alert-banner { display: flex; align-items: center; padding: 14px 18px; border-radius: 10px; font-size: 14px; margin-bottom: 20px; gap: 12px; }
.new-alert-banner.red { background: #FFF7ED; border: 1px solid #FDE6CF; color: #92400e; }
.alert-icon { margin-right: 12px; font-size: 16px; }
.alert-action-btn { margin-left: auto; background: #ffffff; border: 1px solid #E8E6E3; padding: 8px 14px; border-radius: 10px; font-size: 13px; font-weight: 600; cursor: pointer; color: #374151; box-shadow: 0 1px 2px rgba(16,24,40,0.04); }

/* New Pill Sub Tabs */
.sub-tabs-container { margin-bottom: 24px; }
.sub-tab-track { display: flex; background: #F6F6F7; border-radius: 999px; padding: 6px; width: 100%; }
.sub-tab-btn { flex: 1; display: flex; align-items: center; justify-content: center; gap: 8px; height: 40px; border: none; background: transparent; border-radius: 999px; font-size: 14px; font-weight: 600; color: #6B7280; cursor: pointer; transition: all 0.18s; }
.sub-tab-btn.active { background: #ffffff; color: #111827; box-shadow: 0 6px 18px rgba(15,23,42,0.06); }
.sub-tab-count { background: #EEF1F3; color: #374151; font-size: 12px; padding: 4px 8px; border-radius: 999px; }
.sub-tab-count.red { background: #FFF1F0; color: #B91C1C; }

/* New Book Card List */
.new-book-list { display: flex; flex-direction: column; gap: 18px; }
.new-book-card { background: #fff; border: 1px solid #EEF0F3; border-radius: 12px; padding: 22px; position: relative; overflow: hidden; box-shadow: 0 8px 28px rgba(15,23,42,0.04); }

/* Card Top Badge (Absolute) */
.card-top-badge { position: absolute; top: 18px; right: 18px; }
.badge-status { display: inline-flex; align-items: center; gap: 6px; padding: 6px 12px; border-radius: 999px; font-size: 12px; font-weight: 700; }
.badge-status.blue { background: #EFF6FF; color: #0369A1; }
.badge-status.red { background: #FEF2F2; color: #B91C1C; }
.badge-status.green { background: #ECFDF5; color: #059669; }
.badge-status .ic { font-size: 12px; }

/* Card Main Content Layout */
.card-main-content { display: flex; gap: 16px; }
.card-col-img { flex-shrink: 0; }
.card-book-img { width: 80px; height: 120px; object-fit: cover; border-radius: 6px; box-shadow: 0 2px 4px rgba(0,0,0,0.1); }
.card-col-details { flex: 1; }
.nb-title { font-size: 15px; font-weight: 600; color: #111827; margin: 0 0 4px 0; }
.nb-author { font-size: 13px; color: #6B7280; margin: 0 0 12px 0; }

/* Meta List Items */
.nb-meta-list { display: flex; flex-direction: column; gap: 6px; }
.nb-meta-item { display: flex; align-items: center; gap: 8px; font-size: 13px; color: #4B5563; }
.nb-meta-item .ic { color: #9CA3AF; font-size: 14px; width: 16px; text-align: center; }

/* Extension Progress Bar */
.ext-progress { background: #E5E7EB; height: 6px; width: 60px; border-radius: 999px; position: relative; margin-left: 4px; }
/* Mock progress fill - in real app use dynamic width */
.ext-progress::after { content: ''; position: absolute; left: 0; top: 0; bottom: 0; width: 50%; background: #3B82F6; border-radius: 999px; }
.ext-count { margin-left: 70px; font-size: 12px; color: #6B7280; }

/* Extension Status Text */
.nb-extension-status { margin-top: 8px; display: flex; align-items: center; gap: 8px; font-size: 13px; font-weight: 500; }
.nb-extension-status.green { color: #10B981; }
.nb-extension-status.muted { color: #6B7280; }

/* Card Divider */
.card-divider { height: 1px; background: #F3F4F6; margin: 16px 0; }

/* Card Footer (Fees & Actions) */
.card-footer { display: flex; justify-content: space-between; align-items: flex-end; }
.footer-left { display: flex; flex-direction: column; gap: 8px; }
.fee-row { display: flex; align-items: center; gap: 12px; font-size: 13px; width: 100%; }
.status-row { margin-top: 4px; }
.fee-label { color: #6B7280; flex: 1; white-space: nowrap; overflow: hidden; text-overflow: ellipsis; }
.fee-label.red { color: #EF4444; }
.fee-value { font-weight: 600; color: #111827; white-space: nowrap; margin-left: 8px; }
.fee-value.red { color: #EF4444; }

.footer-right { display: flex; flex-direction: column; align-items: flex-end; gap: 12px; }
/* Payment Status Badges */
.badge-payment { display: inline-block; padding: 2px 8px; border-radius: 4px; font-size: 11px; font-weight: 600; }
.badge-payment.green { background: #ECFDF5; color: #10B981; }
.badge-payment.orange { background: #FFF7ED; color: #F97316; }

/* Action Buttons */
.action-btn { display: inline-flex; align-items: center; gap: 6px; height: 32px; padding: 0 16px; border-radius: 6px; font-size: 13px; font-weight: 500; cursor: pointer; border: none; transition: all 0.2s; }
.action-btn.black { background: #111827; color: #fff; }
.action-btn.black:hover { background: #1F2937; }
.action-btn:disabled { background: #F3F4F6; color: #9CA3AF; cursor: not-allowed; }
.btn-ic { font-size: 14px; }

/* Inner Alert (e.g. Overdue warning at bottom of card) */
.card-inner-alert { margin-top: 16px; padding: 12px; border-radius: 8px; font-size: 13px; display: flex; align-items: center; gap: 8px; }
.card-inner-alert.red { background: #FEF2F2; border: 1px solid #FECDD3; color: #B91C1C; }


/* =========================================
   STYLE GROUP: MODAL (Existing)
   ========================================= */
/* ... (Semua style modal dari kode sebelumnya tetap sama, tidak diubah) ... */
.modal-overlay { position: fixed; inset: 0; background: rgba(15,23,42,0.55); z-index: 1000; display: flex; align-items: center; justify-content: center; }
.modal { width: 420px; background: #fff; border-radius: 10px; border: 1px solid #eef0f3; padding: 16px 16px 14px; position: relative; box-shadow: 0 12px 30px rgba(0,0,0,0.18); }
.modal-close { position: absolute; right: 12px; top: 10px; border: 0; background: transparent; font-size: 16px; cursor: pointer; color: #6b7280; }
.modal-header h3 { margin: 0; font-size: 13px; font-weight: 700; }
.modal-header p { margin: 4px 0 0; font-size: 11px; color: #6b7280; }
.modal-header.center { text-align: center; }
.modal-book { margin-top: 12px; display: flex; gap: 10px; align-items: center; }
.modal-book-img { width: 60px; height: 84px; border-radius: 6px; overflow: hidden; background: #f3f4f6; flex-shrink: 0; }
.modal-book-img img { width: 100%; height: 100%; object-fit: cover; }
.modal-book-text h4 { margin: 0; font-size: 12px; font-weight: 600; }
.modal-book-text p { margin: 3px 0 0; font-size: 11px; color: #6b7280; }
.modal-divider { height: 1px; background: #eef0f3; margin: 12px 0; }
.modal-details { font-size: 11px; }
.detail-row { display: flex; justify-content: space-between; align-items: center; padding: 4px 0; }
.detail-row .l { display: flex; align-items: center; gap: 6px; color: #374151; }
.detail-row .r { color: #111827; }
.payment-box { background: #f9fafb; border: 1px solid #eef0f3; padding: 10px; border-radius: 8px; font-size: 11px; display: flex; flex-direction: column; gap: 6px; }
.payment-row { display: flex; justify-content: space-between; }
.muted { color: #6b7280; }
.bold { font-weight: 700; color: #111827; }
.payment-summary { font-size: 11px; display: flex; flex-direction: column; gap: 8px; }
.summary-row { display: flex; justify-content: space-between; align-items: center; }
.total { font-size: 14px; font-weight: 800; color: #111827; }
.modal-alert { margin-top: 10px; font-size: 11px; line-height: 1.4; padding: 9px 10px; border-radius: 8px; border: 1px solid transparent; display: flex; gap: 8px; align-items: flex-start; }
.modal-alert.blue { background: #eff6ff; border-color: #bfdbfe; color: #1d4ed8; }
.modal-alert.green { background: #ecfdf3; border-color: #bbf7d0; color: #15803d; }
.modal-footer { margin-top: 12px; display: flex; justify-content: flex-end; gap: 8px; }
.btn { height: 28px; padding: 0 12px; border-radius: 7px; font-size: 11px; cursor: pointer; border: 1px solid transparent; }
.btn.ghost { background: #fff; border-color: #e5e7eb; color: #111827; }
.btn.dark { background: #0b1020; color: #fff; }
.success-top { text-align: center; margin-top: 4px; }
.success-icon { width: 34px; height: 34px; margin: 0 auto 8px; border-radius: 999px; background: #dcfce7; color: #16a34a; display: flex; align-items: center; justify-content: center; font-weight: 900; border: 2px solid #22c55e; font-size: 14px; }
.success-card { margin-top: 12px; border: 1px solid #eef0f3; border-radius: 8px; padding: 10px; }
.success-book { display: flex; gap: 10px; align-items: center; }
.success-book img { width: 46px; height: 64px; object-fit: cover; border-radius: 6px; }
.success-details { font-size: 11px; margin-top: 6px; }
.paid { color: #16a34a; font-weight: 700; }
.center { text-align: center; }
.date-input { border: none; background: transparent; text-align: right; font-size: 14px; outline: none; }
.ext-grid { display: flex; flex-direction: column; gap: 10px; font-size: 11px; }
.ext-row { display: flex; justify-content: space-between; align-items: center; }
.ext-label { color: #374151; display: flex; gap: 6px; align-items: center; }
.ext-val { font-weight: 500; color: #111827; }
.ext-val.val-green { color: #16a34a; font-weight: 600; }
.ext-fee-box { display: flex; justify-content: space-between; align-items: center; padding: 10px 0; }
.ext-fee-box .label { font-size: 12px; color: #374151; }
.ext-fee-box .amount { font-size: 16px; font-weight: 600; color: #111827; }

/* Placeholder when borrow tab has no list */
.empty-borrow-placeholder { display: flex; align-items: center; justify-content: center; padding: 40px 0; min-height: 360px; }
.empty-inner { text-align: center; }
.empty-text { color: #9CA3AF; font-size: 16px; font-weight: 600; background: #fff; padding: 18px 24px; border-radius: 10px; border: 1px dashed #EEF0F3; }
</style>
