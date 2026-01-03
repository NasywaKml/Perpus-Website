<template>
  <div class="library-home">
    <nav class="navbar">
      <div class="container nav-inner">
        <div class="nav-left">
          <div class="logo">
            <div class="logo-icon"></div>
            <div class="logo-text">
              <span class="lt-main" @click="goToProfile" :class="{ clickable: isLoggedIn }">{{ displayName }}</span>
              <span class="lt-sub">Library Management System</span>
            </div>
          </div>
          <ul class="nav-links">
            <li>
              <a href="#" class="active" @click.prevent="goToHome">Beranda</a>
            </li>
          </ul>
        </div>

        <div class="nav-center">
          <div class="search-wrapper">
            <span class="search-icon">🔍</span>
            <input v-model="searchQuery" @keydown.enter="goToCatalogSearch" type="text" placeholder="Cari buku, jurnal, atau artikel..." />
          </div>
        </div>

        <div class="nav-right">
          <template v-if="isLoggedIn">
            <button class="btn-primary small" @click="handleLogout">Logout</button>
          </template>
          <template v-else>
            <button class="btn-text" @click="goToLogin">Masuk</button>
            <button class="btn-primary small" @click="goToRegister">Daftar</button>
          </template>
        </div>
      </div>
    </nav>

    <header class="hero-section">
      <div class="container hero-inner">
        <div class="hero-text">
          <h1>Platform Digital untuk Sumber Bacaan Anda</h1>
          <p class="hero-desc">Akses ribuan jurnal, buku, dan artikel penelitian dari berbagai disiplin ilmu. Dukung penelitian Anda dengan koleksi digital terlengkap.</p>
          <div class="hero-actions">
            <button class="btn-primary" @click="goToCatalog">Mulai Baca <span>→</span></button>
            <button class="btn-outline" @click="goToCatalog">Jelajahi Koleksi</button>
          </div>

          <div class="hero-stats">
            <div class="stat-item">
              <strong>{{ formatShortCount(ebooksCount) }}</strong>
              <span>Buku Digital</span>
            </div>
            <div class="stat-item">
              <strong>{{ formatShortCount(physicalCount) }}</strong>
              <span>Buku Fisik</span>
            </div>
          </div>
        </div>

        <div class="hero-image-wrapper">
          <div class="hero-img-container">
            <img src="https://images.unsplash.com/photo-1481627834876-b7833e8f5570?ixlib=rb-4.0.3&auto=format&fit=crop&w=1000&q=80" alt="Library" class="main-hero-img" />

            <div class="float-card card-1">
              <div class="fc-icon">📖</div>
              <div class="fc-text">
                <strong>Akses Mudah</strong>
                <span>24/7 Online</span>
              </div>
            </div>

            <div class="float-card card-2">
              <div class="fc-icon">📄</div>
              <div class="fc-text">
                <strong>Multi Format</strong>
                <span>PDF, EPUB, HTML</span>
              </div>
            </div>
          </div>
        </div>
      </div>
    </header>

    <div class="container">
      <div class="filter-bar">
        <div class="fb-top">
          <div class="dropdown-wrapper">
            <div class="dropdown" @click="toggleCategoryDropdown">
              <span>{{ selectedCategory || "Semua Kategori" }}</span>
              <span class="chev">⌄</span>
            </div>

            <div v-if="showCategoryDropdown" class="dropdown-menu" @click.stop>
              <button v-for="cat in categoryOptions" :key="cat" class="dropdown-item" @click="selectCategory(cat)">
                {{ cat }}
              </button>
            </div>
          </div>
            <div class="fb-controls">
            <div class="dropdown-wrapper">
              <div class="dropdown sort-control" @click="toggleSortDropdown">
                <span class="sort-label">{{ selectedSortLabel }}</span>
                <span class="chev">⌄</span>
              </div>

              <div v-if="showSortDropdown" class="dropdown-menu" @click.stop>
                <button v-for="opt in sortOptions" :key="opt.value" class="dropdown-item" @click="selectSort(opt.value)">
                  {{ opt.label }}
                </button>
              </div>
            </div>
          </div>
        </div>
        <div class="fb-tags">
          <span class="fb-label">Filter oleh:</span>

          <template v-if="activeFilters.length">
            <button v-for="filter in activeFilters" :key="filter" class="tag" @click="clearAllFilters">{{ filter }} ×</button>

            <button class="tag-clear" @click="clearAllFilters">Hapus semua</button>
          </template>
        </div>
      </div>
    </div>

    <section class="section container">
      <div class="section-header">
        <div>
          <h2>Koleksi Unggulan</h2>
          <p class="section-sub">Buku dan jurnal terpopuler untuk penelitian</p>
        </div>
        <a href="#" class="link-all" @click.prevent="goToCatalog">Lihat Semua</a>
      </div>

      <div class="grid-3">
        <div v-for="(book, index) in featuredBooksToShow" :key="index" class="book-card">
          <div class="bc-cover">
            <span class="badge-oa">Open Access</span>
            <img :src="book.image" :alt="book.title" />
          </div>
          <div class="bc-info">
            <h3 class="bc-title">{{ book.title }}</h3>
            <p class="bc-author">{{ book.author }}</p>
            
            <div class="bc-tags">
              <span v-for="fmt in book.formats" :key="fmt">{{ fmt }}</span>
            </div>
            <button class="btn-block" @click="goToBookDetails(book.id)"><span class="icon">👁</span> Baca Sekarang</button>
          </div>
        </div>
      </div>
    </section>

    <section class="section container bg-gray">
      <div class="section-header center">
        <h2>Jelajahi Berdasarkan Kategori</h2>
        <p class="section-sub">Temukan koleksi buku dan jurnal berdasarkan bidang studi yang Anda minati</p>
      </div>

      <div class="grid-4">
        <div v-for="(cat, index) in categories" :key="index" class="cat-card">
          <div class="cat-top">
            <div class="cat-icon" :style="{ color: cat.color, backgroundColor: cat.bg }">
              {{ cat.icon }}
            </div>
            <span class="cat-count">{{ cat.count }}</span>
          </div>
          <h3 class="cat-title">{{ cat.name }}</h3>
          <p class="cat-desc">{{ cat.desc }}</p>
        </div>
      </div>
    </section>

    <section class="section container">
      <div class="section-header">
        <div>
          <h2>Penambahan Terbaru</h2>
          <p class="section-sub">Koleksi terbaru yang baru saja ditambahkan ke perpustakaan</p>
        </div>
        <button class="btn-outline-sm" @click="goToCatalog">↻ Lihat Semua</button>
      </div>

      <div class="list-layout">
        <div v-for="(item, index) in visibleNewBooks" :key="index" class="list-item">
          <div class="li-content">
            <h3 class="li-title">{{ item.title }}</h3>
            <p class="li-author">{{ item.author }}</p>
            <p class="li-desc">{{ item.desc }}</p>
            <div class="li-meta">
              <span v-if="item.views" class="stats">👁 {{ item.views }}</span>
              <span v-if="item.publisher" class="publisher"> • {{ item.publisher }}</span>
            </div>
            <div class="li-tags">
              <span v-for="tag in item.tags" :key="tag" class="tag-sm">{{ tag }}</span>
            </div>
          </div>
          <div class="li-actions">
            <span class="time-ago">🕒 {{ item.time }}</span>
            <div class="btn-group">
              <button class="btn-black sm" @click="goToBookDetails(item.id)">Baca</button>
            </div>
          </div>
        </div>
      </div>

      <div class="center-btn-wrapper">
        <button v-if="showLoadMore" class="btn-outline" @click="loadMoreNew">Muat Lebih Banyak</button>
      </div>
    </section>

    <footer class="footer">
      <div class="container footer-inner">
        <div class="footer-col brand-col">
          <div class="logo">
            <div class="logo-icon"></div>
            <div class="logo-text">
              <span class="lt-main">LibraryHub</span>
              <span class="lt-sub">Platform Penelitian Digital</span>
            </div>
          </div>
          <p class="footer-desc">Platform perpustakaan digital terdepan untuk penelitian akademik dengan akses ke ribuan buku, jurnal, dan artikel dari berbagai disiplin ilmu.</p>
          <div class="socials"><span>f</span> <span>t</span> <span>in</span> <span>yt</span></div>
        </div>

        <div class="footer-col">
          <h4>Tautan Cepat</h4>
          <ul>
            <li>Beranda</li>
            <li>Koleksi Buku</li>
            <li>Jurnal Ilmiah</li>
            <li>Artikel Penelitian</li>
            <li>Kategori</li>
            <li>Pencarian Lanjutan</li>
          </ul>
        </div>

        <div class="footer-col">
          <h4>Layanan</h4>
          <ul>
            <li>Akses Digital</li>
            <li>Bantuan Penelitian</li>
            <li>Dukungan Teknis</li>
            <li>Pelatihan Pengguna</li>
            <li>API Developer</li>
            <li>Kemitraan</li>
          </ul>
        </div>

        <div class="footer-col contact-col">
          <h4>Kontak & Berlangganan</h4>
          <div class="contact-info">
            <p>✉ info@libraryhub.ac.id</p>
            <p>📞 +62 21 1234 5678</p>
            <p>📍 Jl. Pendidikan No. 123, Jakarta Selatan, Indonesia</p>
          </div>
          <div class="newsletter">
            <p>Berlangganan Newsletter</p>
            <div class="input-group">
              <input type="email" placeholder="Email Anda" />
              <button>Langganan</button>
            </div>
          </div>
        </div>
      </div>
      <div class="container footer-bottom">
        <p>© 2024 LibraryHub. Semua hak dilindungi.</p>
        <div class="fb-links">
          <a href="#">Kebijakan Privasi</a> • <a href="#">Syarat Penggunaan</a> • <a href="#">Bantuan</a> •
          <a href="#">Sitemap</a>
        </div>
      </div>
    </footer>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from "vue";
import { watch } from "vue";
import { useRouter } from "vue-router";
import axios from "axios";

const router = useRouter();

const API_BASE = "http://localhost:8080";

// Auth state
const isLoggedIn = ref(false);
const displayName = ref("LibrarySys");

// Filter & kategori state
const categoryOptions = ref([]);
const selectedCategory = ref(null);
const showCategoryDropdown = ref(false);
const filteredBooks = ref([]);
// Sort dropdown state
const showSortDropdown = ref(false);
const sortOptions = ref([
  { value: "relevan", label: "Paling Relevan" },
  { value: "az", label: "A - Z" },
  { value: "za", label: "Z - A" },
  { value: "terlaris", label: "Terlaris" },
]);
const selectedSort = ref("relevan");

const selectedSortLabel = computed(() => {
  const opt = sortOptions.value.find((o) => o.value === selectedSort.value);
  return opt ? opt.label : "Paling Relevan";
});

let borrowCounts = {}; // map title -> count (computed when auth available)

// Navigation functions
function goToHome() {
  router.push("/HomePage");
}

// goToBorrow removed (feature removed from nav)

function goToBookDetails(bookId) {
  router.push(`/BookDetails/${bookId}`);
}

function goToLogin() {
  router.push("/LoginPage");
}

function goToRegister() {
  router.push("/LoginPage?tab=register");
}

function goToCatalog() {
  router.push("/BookCatalog");
}

function handleLogout() {
  sessionStorage.removeItem("token");
  sessionStorage.removeItem("tokenType");
  sessionStorage.removeItem("idUser");
  sessionStorage.removeItem("username");
  sessionStorage.removeItem("userData");

  isLoggedIn.value = false;
  displayName.value = "LibrarySys";

  router.push("/HomePage");
}

function goToProfile() {
  const idUser = sessionStorage.getItem("idUser");
  if (!idUser) return router.push("/LoginPage");
  router.push("/UserProfile");
}

// Search from homepage -> navigate to BookCatalog with query
const searchQuery = ref("");
function goToCatalogSearch() {
  const q = (searchQuery.value || "").trim();
  if (!q) {
    router.push("/BookCatalog");
    return;
  }
  router.push({ path: "/BookCatalog", query: { q } });
}

// Dropdown handlers
function toggleCategoryDropdown() {
  showCategoryDropdown.value = !showCategoryDropdown.value;
}

function toggleSortDropdown() {
  showSortDropdown.value = !showSortDropdown.value;
}

function selectSort(value) {
  selectedSort.value = value;
  showSortDropdown.value = false;
  applySortToLists();
}

async function computeBorrowCountsIfAuth() {
  try {
    const token = sessionStorage.getItem("token");
    const tokenType = sessionStorage.getItem("tokenType") || "Bearer";
    borrowCounts = {};
    if (!token) return;

    const res = await axios.get(`${API_BASE}/api/peminjaman`, { headers: { Authorization: `${tokenType} ${token}` } });
    const list = res.data || [];
    for (const p of list) {
      const title = (p.judulBuku || "").trim();
      if (!title) continue;
      borrowCounts[title] = (borrowCounts[title] || 0) + 1;
    }
  } catch (err) {
    // ignore errors (likely 401 if not admin) and keep borrowCounts empty
    console.debug("Could not compute borrow counts (maybe unauthorized):", err && err.message ? err.message : err);
    borrowCounts = {};
  }
}

function applySortToLists() {
  const mode = selectedSort.value;

  function sortByAZ(a, b) {
    return (a.title || "").localeCompare(b.title || "", "id", { sensitivity: "base" });
  }
  function sortByZA(a, b) {
    return (b.title || "").localeCompare(a.title || "", "id", { sensitivity: "base" });
  }
  function sortByTerlaris(a, b) {
    const ca = borrowCounts[a.title] || 0;
    const cb = borrowCounts[b.title] || 0;
    if (cb !== ca) return cb - ca; // descending by count
    // tie-breaker by id (ascending)
    const ia = a.id || 0;
    const ib = b.id || 0;
    return ia - ib;
  }

  // If 'relevan' (Paling Relevan) is selected, keep original order (no sort)
  if (mode === "relevan") {
    return;
  }
  const sorter = mode === "az" ? sortByAZ : mode === "za" ? sortByZA : sortByTerlaris;

  // Apply to featuredBooks and allNewBooks (and filteredBooks if active)
  try {
    if (filteredBooks.value && filteredBooks.value.length) {
      filteredBooks.value.sort(sorter);
    } else if (featuredBooks.value && featuredBooks.value.length) {
      featuredBooks.value.sort(sorter);
    }

    if (allNewBooks.value && allNewBooks.value.length) {
      allNewBooks.value.sort(sorter);
    }
  } catch (err) {
    console.error("Error applying sort:", err);
  }
}

function selectCategory(category) {
  selectedCategory.value = category;
  showCategoryDropdown.value = false;

  if (!category) {
    filteredBooks.value = [];
    return;
  }

  loadBooksByCategory(category);
}

function clearAllFilters() {
  selectedCategory.value = null;
  filteredBooks.value = [];
}

const activeFilters = computed(() => {
  const filters = [];
  if (selectedCategory.value) {
    filters.push(`Kategori: ${selectedCategory.value}`);
  }
  return filters;
});

const featuredBooksToShow = computed(() => (filteredBooks.value.length ? filteredBooks.value : featuredBooks.value));

async function loadCategories() {
  try {
    const res = await axios.get(`${API_BASE}/api/buku`);
    const books = res.data || [];
    const set = new Set(books.map((b) => b.kategori).filter((k) => k && typeof k === "string" && k.trim() !== ""));
    categoryOptions.value = Array.from(set);
  } catch (err) {
    console.error("Failed to load categories:", err);
    categoryOptions.value = [];
  }
}

// Replace dummy category counts with dynamic data from API
// Helper to normalize category name for mapping
function normalizeCategoryKey(name) {
  return name
    .toLowerCase()
    .replace(/&/g, "dan")
    .replace(/[^a-z0-9]/g, "")
    .replace(/\s+/g, "");
}

async function loadCategoryCounts() {
  try {
    // Fetch all books and compute counts per kategori locally
    const res = await axios.get(`${API_BASE}/api/buku`);
    const books = res.data || [];

    const map = {};
    for (const b of books) {
      const k = b.kategori || "";
      const key = normalizeCategoryKey(k);
      map[key] = (map[key] || 0) + 1;
    }

    categories.value = categories.value.map((cat) => {
      const key = normalizeCategoryKey(cat.name);
      const count = map[key] || 0;
      return {
        ...cat,
        count: count,
      };
    });
  } catch (err) {
    console.error("Failed to load category counts:", err);
  }
}

async function loadBooksByCategory(category) {
  try {
    const res = await axios.get(`${API_BASE}/api/buku/kategori/${encodeURIComponent(category)}`);
    const books = res.data || [];

    filteredBooks.value = books.map((b) => ({
      title: b.judul,
      author: `${b.pengarang || ""}${b.tahunTerbit ? " • " + b.tahunTerbit : ""}`,
      rating: 0,
      views: "",
      pages: 0,
      formats: ["PDF"],
      image: b.urlGambarSampul || "https://images.unsplash.com/photo-1512820790803-83ca734da794?auto=format&fit=crop&w=400&q=80",
    }));
  } catch (err) {
    console.error("Failed to load books by category:", err);
    filteredBooks.value = [];
  }
}

onMounted(async () => {
  const idUser = sessionStorage.getItem("idUser");

  if (idUser) {
    isLoggedIn.value = true;

    let name = sessionStorage.getItem("username") || null;
    const storedUser = sessionStorage.getItem("userData");

    if (!name && storedUser) {
      try {
        const parsed = JSON.parse(storedUser);
        name = parsed.username || null;
      } catch {
        // ignore parse error, fallback to default name
      }
    }

    displayName.value = name || "LibrarySys";
  } else {
    isLoggedIn.value = false;
    displayName.value = "LibrarySys";
  }

  // compute borrow counts if authenticated, then load data and apply current sort
  await computeBorrowCountsIfAuth();
  await Promise.all([loadCategories(), loadCategoryCounts(), loadNewAdditions(), loadFeaturedBooks(), loadHeroStats()]);
  applySortToLists();
});

// Load featured books from API
async function loadFeaturedBooks() {
  try {
    const res = await axios.get(`${API_BASE}/api/buku`);
    const books = res.data || [];

    // Ambil 3 buku pertama sebagai featured books
    featuredBooks.value = books.slice(0, 3).map((b) => ({
      id: b.idBuku,
      title: b.judul || "Untitled",
      author: (b.pengarang || "") + (b.tahunTerbit ? " • " + b.tahunTerbit : ""),
      rating: 4.5,
      views: "10K+",
      pages: 300,
      formats: ["PDF"],
      image: b.urlGambarSampul || "https://images.unsplash.com/photo-1506880018603-83d5b814b5a6?auto=format&fit=crop&w=400&q=80",
    }));

    console.log("Featured books loaded:", featuredBooks.value);
  } catch (err) {
    console.error("Failed to load featured books:", err);
    featuredBooks.value = [];
  }
}

// Featured books from API
const featuredBooks = ref([]);

// Hero stats (dynamic from API)
const ebooksCount = ref(0);
const physicalCount = ref(0);

function formatShortCount(n) {
  if (n === null || n === undefined) return "0";
  if (n >= 1000000) return Math.round(n / 1000000) + "M+";
  if (n >= 1000) return Math.round(n / 1000) + "K+";
  return String(n);
}

async function loadHeroStats() {
  try {
    const res = await axios.get(`${API_BASE}/api/buku`);
    const books = res.data || [];

    let ebooks = 0;
    let physical = 0;

    for (const b of books) {
      const candidates = [b.jenisFisik, b.jenis_fisik, b.fisik, b.jenis, b.type, b.tipe, b.kategori, b.format];
      const raw = (candidates.find((c) => c !== undefined && c !== null) || "").toString().toLowerCase();

      if (/(ebook|e-?book|e book|digital)/.test(raw)) {
        ebooks += 1;
        continue;
      }

      if (/(fisik|physical|hardcover|paperback|print|hard cover|paper back)/.test(raw)) {
        physical += 1;
        continue;
      }

      // fallback: infer from title/kategori
      const title = (b.judul || "").toString().toLowerCase();
      const kategori = (b.kategori || "").toString().toLowerCase();
      if (/(fisik|physical|hardcover|paperback|print)/.test(title) || /(fisik|physical|hardcover|paperback|print)/.test(kategori)) {
        physical += 1;
      } else {
        // default to ebook for unknown
        ebooks += 1;
      }
    }

    ebooksCount.value = ebooks;
    physicalCount.value = physical;
  } catch (err) {
    console.error("Failed to load hero stats:", err);
    ebooksCount.value = 0;
    physicalCount.value = 0;
  }
}

const categories = ref([
  { name: "Teknologi & IT", desc: "Programming, AI, Cybersecurity", icon: "💻", color: "#3b82f6", bg: "#eff6ff" },
  { name: "Sains & Penelitian", desc: "Biologi, Kimia, Fisika", icon: "⚗️", color: "#10b981", bg: "#ecfdf5" },
  { name: "Kesehatan & Medis", desc: "Kedokteran, Farmasi, Keperawatan", icon: "♥", color: "#ef4444", bg: "#fef2f2" },
  { name: "Ilmu Sosial", desc: "Sosiologi, Psikologi, Antropologi", icon: "⚖", color: "#8b5cf6", bg: "#f5f3ff" },
  { name: "Bisnis & Ekonomi", desc: "Manajemen, Keuangan, Marketing", icon: "📈", color: "#f59e0b", bg: "#fffbeb" },
  { name: "Seni & Budaya", desc: "Seni Rupa, Musik, Sastra", icon: "🎨", color: "#ec4899", bg: "#fdf2f8" },
  { name: "Pendidikan", desc: "Pedagogik, Kurikulum, Metodologi", icon: "🎓", color: "#6366f1", bg: "#eef2ff" },
  { name: "Hukum", desc: "Konstitusi, Pidana, Perdata", icon: "⚖", color: "#64748b", bg: "#f8fafc" },
]);

// New additions: load from API, show only first N, hide load-more when not needed
const allNewBooks = ref([]);
const visibleCount = ref(4);
const showLoadMore = ref(false);

const visibleNewBooks = computed(() => allNewBooks.value.slice(0, visibleCount.value));

async function loadNewAdditions() {
  try {
    const res = await axios.get(`${API_BASE}/api/buku`);
    const books = res.data || [];

    allNewBooks.value = books.map((b) => ({
      id: b.idBuku, // Ensure the ID is mapped correctly
      title: b.judul || "Untitled",
      author: (b.pengarang || "") + (b.tahunTerbit ? " • " + b.tahunTerbit : ""),
      desc: b.abstrak || "",
      rating: 0,
      downloads: "",
      views: "",
      publisher: b.penerbit || "",
      tags: [],
      time: "",
    }));

    showLoadMore.value = allNewBooks.value.length > 4;
  } catch (err) {
    console.error("Failed to load new additions:", err);
    allNewBooks.value = [];
    showLoadMore.value = false;
  }
}

function loadMoreNew() {
  // tampilkan semua buku yang sudah dimuat; tombol hanya bersifat tampilan
  visibleCount.value = allNewBooks.value.length;
  showLoadMore.value = false;
}
</script>

<style scoped>
@import url("https://fonts.googleapis.com/css2?family=Inter:wght@300;400;500;600;700&display=swap");

/* ================= GLOBAL RESET & UTILS ================= */
.library-home {
  font-family: "Inter", sans-serif;
  color: #111827;
  background-color: #ffffff;
  line-height: 1.5;
}

* {
  box-sizing: border-box;
  margin: 0;
  padding: 0;
}

.container {
  max-width: 1280px;
  margin: 0 auto;
  padding: 0 24px;
}

button {
  cursor: pointer;
  font-family: inherit;
}

/* Buttons */
.btn-primary {
  background-color: #0b0f19;
  color: white;
  padding: 10px 20px;
  border: none;
  border-radius: 6px;
  font-size: 14px;
  font-weight: 500;
  display: inline-flex;
  align-items: center;
  gap: 8px;
}

.btn-outline {
  background-color: transparent;
  color: #111827;
  border: 1px solid #e5e7eb;
  padding: 10px 20px;
  border-radius: 6px;
  font-size: 14px;
  font-weight: 500;
}

.btn-outline-sm {
  background-color: white;
  color: #374151;
  border: 1px solid #e5e7eb;
  padding: 6px 12px;
  border-radius: 6px;
  font-size: 12px;
  font-weight: 500;
}

.btn-text {
  background: none;
  border: none;
  font-weight: 600;
  font-size: 14px;
  color: #111827;
}

.btn-black {
  background-color: #0b0f19;
  color: white;
  padding: 8px 16px;
  border-radius: 6px;
  font-size: 12px;
  border: none;
}

.btn-block {
  width: 100%;
  background-color: #0b0f19;
  color: white;
  padding: 10px;
  border-radius: 6px;
  border: none;
  font-size: 13px;
  font-weight: 500;
  margin-top: 16px;
}

/* ================= NAVBAR ================= */
.navbar {
  border-bottom: 1px solid #f3f4f6;
  padding: 16px 0;
  position: sticky;
  top: 0;
  background: white;
  z-index: 100;
}

.nav-inner {
  display: flex;
  align-items: center;
  justify-content: space-between;
}

.nav-left,
.logo {
  display: flex;
  align-items: center;
  gap: 12px;
}

.logo-icon {
  width: 32px;
  height: 32px;
  background-color: #0b0f19;
  border-radius: 50%;
}

.logo-text {
  display: flex;
  flex-direction: column;
  line-height: 1.2;
}

.lt-main {
  font-weight: 700;
  font-size: 16px;
}

.lt-main.clickable {
  cursor: pointer;
}
.lt-main.clickable:hover {
  text-decoration: underline;
}

.lt-sub {
  font-size: 10px;
  color: #6b7280;
}

.nav-links {
  display: flex;
  list-style: none;
  gap: 24px;
  margin-left: 32px;
}

.nav-links a {
  text-decoration: none;
  color: #6b7280;
  font-size: 14px;
  font-weight: 500;
}

.nav-links a.active {
  color: #111827;
  font-weight: 600;
}

.search-wrapper {
  background-color: #f9fafb;
  border-radius: 8px;
  padding: 8px 16px;
  display: flex;
  align-items: center;
  width: 320px;
}

.search-wrapper input {
  border: none;
  background: transparent;
  outline: none;
  width: 100%;
  margin-left: 8px;
  font-size: 13px;
}

.search-icon {
  font-size: 14px;
  color: #9ca3af;
}

.nav-right {
  display: flex;
  gap: 16px;
  align-items: center;
}

/* ================= HERO ================= */
.hero-section {
  padding: 60px 0;
  background: #fafafa; /* Slight off-white for hero bg */
}

.hero-inner {
  display: flex;
  align-items: center;
  gap: 48px;
}

.hero-text {
  flex: 1;
}

.hero-text h1 {
  font-size: 42px;
  font-weight: 700;
  line-height: 1.2;
  margin-bottom: 16px;
  color: #111827;
}

.hero-desc {
  color: #4b5563;
  font-size: 16px;
  margin-bottom: 32px;
  max-width: 500px;
}

.hero-actions {
  display: flex;
  gap: 16px;
  margin-bottom: 48px;
}

.hero-stats {
  display: flex;
  gap: 48px;
  border-top: 1px solid #e5e7eb;
  padding-top: 24px;
}

.stat-item strong {
  display: block;
  font-size: 24px;
  font-weight: 700;
}

.stat-item span {
  font-size: 12px;
  color: #6b7280;
}

.hero-image-wrapper {
  flex: 1;
  position: relative;
}

.hero-img-container {
  position: relative;
  border-radius: 16px;
  overflow: hidden;
}

.main-hero-img {
  width: 100%;
  height: 400px;
  object-fit: cover;
  border-radius: 16px;
}

.float-card {
  position: absolute;
  background: white;
  padding: 12px 16px;
  border-radius: 12px;
  box-shadow: 0 10px 15px -3px rgba(0, 0, 0, 0.1);
  display: flex;
  align-items: center;
  gap: 12px;
  min-width: 160px;
}

.card-1 {
  top: 40px;
  left: -20px;
}

.card-2 {
  bottom: 40px;
  right: -20px;
}

.fc-icon {
  font-size: 20px;
}

.fc-text strong {
  display: block;
  font-size: 12px;
  color: #111827;
}

.fc-text span {
  font-size: 10px;
  color: #6b7280;
}

/* ================= FILTER BAR ================= */
.filter-bar {
  padding: 24px 0;
  border-bottom: 1px solid #f3f4f6;
}

.fb-top {
  display: flex;
  justify-content: space-between;
  margin-bottom: 16px;
  position: relative;
}

.dropdown-wrapper {
  position: relative;
  display: inline-block;
}

.dropdown {
  border: 1px solid #e5e7eb;
  padding: 8px 12px;
  border-radius: 6px;
  font-size: 13px;
  display: flex;
  align-items: center;
  gap: 8px;
  cursor: pointer;
  background: #f9fafb;
  min-width: 140px;
}

.sort-control {
  /* make label centered while keeping the chevron visible */
  padding-left: 12px;
  padding-right: 8px;
}
.sort-control .sort-label {
  flex: 1 1 auto;
  text-align: center;
}

.dropdown-menu {
  position: absolute;
  top: calc(100% + 6px);
  left: 0;
  background: #ffffff;
  border: 1px solid #e5e7eb;
  box-shadow: 0 10px 20px rgba(0, 0, 0, 0.08);
  border-radius: 8px;
  padding: 6px 0;
  min-width: 180px;
  max-width: 220px;
  z-index: 10;
}

.dropdown-item {
  width: 100%;
  text-align: left;
  padding: 8px 14px;
  border: none;
  background: transparent;
  font-size: 13px;
  color: #111827;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}

.dropdown-item:hover {
  background: #f3f4f6;
}

.fb-controls {
  display: flex;
  gap: 12px;
}

.view-toggle {
  display: flex;
  border: 1px solid #e5e7eb;
  border-radius: 6px;
  overflow: hidden;
}

.vt-btn {
  padding: 6px 10px;
  background: white;
  border: none;
  border-right: 1px solid #e5e7eb;
  cursor: pointer;
}

.vt-btn:last-child {
  border-right: none;
}

.vt-btn.active {
  background: #f3f4f6;
}

.fb-tags {
  display: flex;
  gap: 8px;
  align-items: center;
}

.fb-label {
  font-size: 12px;
  color: #6b7280;
  margin-right: 8px;
}

.tag {
  background: #f3f4f6;
  border: none;
  padding: 4px 10px;
  border-radius: 4px;
  font-size: 11px;
  color: #374151;
  font-weight: 500;
}

.tag-clear {
  background: none;
  border: none;
  font-size: 11px;
  color: #ef4444;
  margin-left: auto;
  cursor: pointer;
}

/* ================= FEATURED ================= */
.section {
  padding: 48px 0;
}

.section-header {
  display: flex;
  justify-content: space-between;
  align-items: flex-end;
  margin-bottom: 32px;
}

.section-header.center {
  text-align: center;
  display: block;
}

h2 {
  font-size: 20px;
  font-weight: 600;
  margin-bottom: 8px;
}

.section-sub {
  font-size: 13px;
  color: #6b7280;
}

.link-all {
  font-size: 13px;
  color: #111827;
  text-decoration: none;
  font-weight: 500;
}

.grid-3 {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: 24px;
}

.book-card {
  border: 1px solid #e5e7eb;
  border-radius: 12px;
  padding: 16px;
  transition: box-shadow 0.2s;
}

.book-card:hover {
  box-shadow: 0 10px 15px -3px rgba(0, 0, 0, 0.05);
}

.bc-cover {
  position: relative;
  height: 180px;
  border-radius: 8px;
  overflow: hidden;
  margin-bottom: 16px;
}

.bc-cover img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.badge-oa {
  position: absolute;
  top: 8px;
  left: 8px;
  background: rgba(255, 255, 255, 0.9);
  padding: 4px 8px;
  border-radius: 4px;
  font-size: 10px;
  font-weight: 600;
  color: #111827;
  border: 1px solid #e5e7eb;
}

.bc-title {
  font-size: 15px;
  font-weight: 600;
  margin-bottom: 4px;
  line-height: 1.4;
}

.bc-author {
  font-size: 11px;
  color: #6b7280;
  margin-bottom: 12px;
}

.bc-meta {
  display: flex;
  gap: 12px;
  font-size: 11px;
  color: #4b5563;
  margin-bottom: 12px;
}

.rating {
  color: #f59e0b;
  font-weight: 600;
}

.bc-tags {
  display: flex;
  gap: 6px;
}

.bc-tags span {
  font-size: 10px;
  background: #f3f4f6;
  padding: 2px 6px;
  border-radius: 4px;
  color: #374151;
}

/* ================= CATEGORIES ================= */
.grid-4 {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: 24px;
}

.cat-card {
  border: 1px solid #e5e7eb;
  border-radius: 12px;
  padding: 20px;
  background: white;
  transition: border-color 0.2s;
}

.cat-card:hover {
  border-color: #d1d5db;
}

.cat-top {
  display: flex;
  justify-content: space-between;
  align-items: flex-start;
  margin-bottom: 16px;
}

.cat-icon {
  width: 40px;
  height: 40px;
  border-radius: 8px;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 20px;
}

.cat-count {
  font-size: 11px;
  color: #6b7280;
}

.cat-title {
  font-size: 14px;
  font-weight: 600;
  margin-bottom: 4px;
}

.cat-desc {
  font-size: 11px;
  color: #6b7280;
  line-height: 1.4;
}

/* ================= NEW LIST ================= */
.list-layout {
  display: flex;
  flex-direction: column;
  gap: 16px;
}

.list-item {
  border: 1px solid #e5e7eb;
  border-radius: 12px;
  padding: 20px;
  display: flex;
  justify-content: space-between;
  align-items: flex-start;
}

.li-content {
  flex: 1;
}

.li-title {
  font-size: 16px;
  font-weight: 600;
  margin-bottom: 4px;
}

.li-author {
  font-size: 12px;
  color: #6b7280;
  margin-bottom: 8px;
}

.li-desc {
  font-size: 13px;
  color: #4b5563;
  margin-bottom: 12px;
  max-width: 600px;
}

.li-meta {
  font-size: 11px;
  color: #6b7280;
  display: flex;
  gap: 12px;
  margin-bottom: 12px;
}

.li-tags {
  display: flex;
  gap: 8px;
}

.tag-sm {
  background: #f3f4f6;
  padding: 4px 8px;
  border-radius: 4px;
  font-size: 10px;
  border: 1px solid #e5e7eb;
}

.li-actions {
  display: flex;
  flex-direction: column;
  align-items: flex-end;
  gap: 16px;
}

.time-ago {
  font-size: 11px;
  color: #9ca3af;
}

.btn-group {
  display: flex;
  gap: 8px;
}

.center-btn-wrapper {
  text-align: center;
  margin-top: 32px;
}

/* ================= FOOTER ================= */
.footer {
  border-top: 1px solid #e5e7eb;
  padding-top: 60px;
  background: #f9fafb;
}

.footer-inner {
  display: grid;
  grid-template-columns: 2fr 1fr 1fr 2fr;
  gap: 48px;
  margin-bottom: 48px;
}

.brand-col .footer-desc {
  font-size: 12px;
  color: #6b7280;
  margin: 16px 0;
  line-height: 1.6;
  max-width: 300px;
}

.socials {
  display: flex;
  gap: 12px;
}

.socials span {
  font-size: 12px;
  font-weight: 600;
  color: #4b5563;
  cursor: pointer;
}

.footer-col h4 {
  font-size: 13px;
  font-weight: 600;
  margin-bottom: 20px;
  color: #111827;
}

.footer-col ul {
  list-style: none;
}

.footer-col ul li {
  font-size: 12px;
  color: #6b7280;
  margin-bottom: 12px;
  cursor: pointer;
}

.contact-info p {
  font-size: 12px;
  color: #6b7280;
  margin-bottom: 10px;
  display: flex;
  gap: 8px;
}

.newsletter {
  margin-top: 24px;
}

.newsletter p {
  font-size: 12px;
  font-weight: 600;
  margin-bottom: 8px;
}

.input-group {
  display: flex;
}

.input-group input {
  border: 1px solid #e5e7eb;
  padding: 8px 12px;
  border-radius: 6px 0 0 6px;
  font-size: 12px;
  outline: none;
  flex: 1;
}

.input-group button {
  background: #0b0f19;
  color: white;
  border: none;
  padding: 8px 12px;
  border-radius: 0 6px 6px 0;
  font-size: 12px;
}

.footer-bottom {
  border-top: 1px solid #e5e7eb;
  padding: 24px;
  display: flex;
  justify-content: space-between;
  font-size: 11px;
  color: #9ca3af;
}

.fb-links a {
  color: #9ca3af;
  text-decoration: none;
  margin: 0 4px;
}
</style>
