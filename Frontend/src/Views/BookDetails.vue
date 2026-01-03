<template>
  <div class="page">
    <!-- Loading -->
    <div v-if="loading" class="loading">Loading book details...</div>

    <!-- Error -->
    <div v-else-if="error" class="error">{{ error }}</div>

    <!-- No Data -->
    <div v-else-if="!book" class="error">Book not found.</div>

    <!-- MAIN CONTENT -->
    <div v-else>
      <!-- Sticky Header -->
      <header class="sticky-header">
        <div class="header-inner">
          <div class="left-group">
            <button class="back-btn" @click="goHome">
              <span class="icon">←</span>
              Back to Catalog
            </button>
            <div class="mini-book">
              <div class="mini-cover">
                <img :src="book.urlGambarSampul" alt="cover" />
              </div>
              <div class="mini-meta">
                <div class="mini-title">{{ book.judul }}</div>
                <div class="mini-author">by {{ book.pengarang }}</div>
              </div>
            </div>
          </div>

          <div class="header-actions">
            <button class="btn btn-primary btn-sm borrowed-list-btn" @click="goToBorrowHistory">
              <svg class="btn-icon" width="18" height="18" viewBox="0 0 24 24" fill="none">
                <rect x="4" y="4" width="16" height="16" rx="2" stroke="white" stroke-width="2" />
                <line x1="8" y1="9" x2="16" y2="9" stroke="white" stroke-width="2" />
                <line x1="8" y1="13" x2="16" y2="13" stroke="white" stroke-width="2" />
                <line x1="8" y1="17" x2="16" y2="17" stroke="white" stroke-width="2" />
              </svg>
              Show My Borrowed List
            </button>
          </div>
        </div>
      </header>

      <!-- Main Layout -->
      <main class="container">
        <div class="grid">
          <!-- LEFT CONTENT -->
          <div class="left-col">
            <!-- Hero -->
            <section class="hero">
              <div class="hero-cover">
                <img :src="book.urlGambarSampul" alt="cover" />
              </div>

              <div class="hero-info">
                <h1 class="hero-title">{{ book.judul }}</h1>
                <div class="hero-author">by {{ book.pengarang }}</div>
                <div class="hero-published">Published {{ book.tahunTerbit }}</div>

                <div class="price-stock">
                  <div class="price">IDR {{ formatCurrency(book.hargaSewa) }}</div>
                </div>

                <div class="hero-actions">
                  <button class="btn btn-primary borrow-btn" :disabled="!canBorrow" @click="goToBorrowPage">
                    <svg class="btn-icon" width="18" height="18" viewBox="0 0 24 24" fill="none">
                      <rect x="4" y="3" width="16" height="18" rx="2" stroke="white" stroke-width="2" />
                      <line x1="4" y1="9" x2="20" y2="9" stroke="white" stroke-width="2" />
                    </svg>
                    Borrow
                  </button>

                  <span class="stock-badge">{{ book.jumlahStok }} Pcs</span>
                </div>
              </div>
            </section>

            <!-- Description -->
            <section class="block">
              <h3 class="block-title">Description</h3>

              <p class="desc-text" :class="{ clamp: !descExpanded }">
                {{ book.abstrak }}
              </p>

              <button class="link-btn" @click="descExpanded = !descExpanded">
                {{ descExpanded ? "Read less" : "Read more" }}
              </button>
            </section>

            <div class="divider"></div>

            <!-- Category -->
            <section class="block">
              <h3 class="block-title">Category</h3>
              <div class="chips">
                <span v-for="c in parsedCategories" :key="c" class="chip">{{ c }}</span>
              </div>
            </section>

            <div class="divider"></div>

            <!-- Details -->
            <section class="block">
              <button class="accordion-head" @click="showAllDetails = !showAllDetails">
                <span>Show All Details</span>
                <span class="chev" :class="{ open: showAllDetails }">⌄</span>
              </button>

              <div v-show="showAllDetails" class="details-grid">
                <div class="detail-col">
                  <div class="detail-item">
                    <div class="detail-label">ISBN</div>
                    <div class="detail-value">{{ book.isbn }}</div>
                  </div>

                  <div class="detail-item">
                    <div class="detail-label">Publisher</div>
                    <div class="detail-value">{{ book.penerbit }}</div>
                  </div>

                  <div class="detail-item">
                    <div class="detail-label">Rental Price</div>
                    <div class="detail-value">IDR {{ formatCurrency(book.hargaSewa) }}</div>
                  </div>

                  <div class="detail-item">
                    <div class="detail-label">Fine per Day</div>
                    <div class="detail-value">IDR {{ formatCurrency(book.dendaPerHari) }}</div>
                  </div>
                </div>

                <div class="detail-col">
                  <div class="detail-item">
                    <div class="detail-label">Type</div>
                    <div class="detail-value">{{ book.jenis }}</div>
                  </div>

                  <div class="detail-item">
                    <div class="detail-label">Shelf Number</div>
                    <div class="detail-value">{{ book.noRak }}</div>
                  </div>

                  <div class="detail-item">
                    <div class="detail-label">Status</div>
                    <div class="detail-value">{{ book.status }}</div>
                  </div>
                </div>
              </div>
            </section>

            <div class="divider"></div>

            <!-- Reviews removed -->
          </div>

          <!-- RIGHT SIDEBAR (removed author card) -->
          <div class="right-col">
          </div>
        </div>
      </main>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, computed } from "vue";
import { useRoute, useRouter } from "vue-router";
import axios from "axios";

const route = useRoute();
const router = useRouter();

const bookId = route.params.id;

const book = ref(null);
const loading = ref(true);
const error = ref(null);
const isBorrowedByUser = ref(false);

// UI State
const descExpanded = ref(false);
const parsedCategories = ref([]);
const showAllDetails = ref(false);

// ===== Currency Formatter =====
function formatCurrency(v) {
  if (!v) return "0";
  return Number(v).toLocaleString("id-ID");
}

// ===== FETCH DATA =====
onMounted(async () => {
  try {
    const res = await axios.get(`/api/buku/${bookId}`);

    book.value = res.data;

    // After fetching book details, check whether current user already borrowed it
    // and mark `isBorrowedByUser` accordingly.
    try {
      const token = sessionStorage.getItem("token");
      if (token) {
        const hres = await axios.get("/api/peminjaman/history");
        const history = hres.data || [];
        const title = (book.value?.judul || "").trim();
        const activeBorrow = history.find((item) => {
          const status = (item.status || "").toString().toUpperCase();
          const isActive = status === "DIPINJAM" || status === "PINJAM" || status === "BORROWED";
          const itemTitle = (item.judulBuku || "").trim();
          return isActive && itemTitle && itemTitle === title;
        });
        isBorrowedByUser.value = !!activeBorrow;
      } else {
        isBorrowedByUser.value = false;
      }
    } catch (err) {
      console.warn("Failed to check user borrow history:", err);
      isBorrowedByUser.value = false;
    }

    parsedCategories.value = res.data.kategori ? res.data.kategori.split(",").map((c) => c.trim()) : [];
  } catch (err) {
    error.value = err.response?.data?.message || err.message || "Book not found";
  } finally {
    loading.value = false;
  }
});

const soldOut = computed(() => {
  const s = Number(book.value?.jumlahStok ?? 0);
  return isNaN(s) ? false : s <= 0;
});

const canBorrow = computed(() => {
  return !soldOut.value && !isBorrowedByUser.value;
});

// ===== GO TO BORROW PAGE =====
function goToBorrowPage() {
  const token = sessionStorage.getItem("token");

  if (!token) {
    alert("Please login first.");
    return router.push("/LoginPage");
  }

  router.push(`/BorrowPage/${bookId}?tab=borrow`);
}

// ===== GO TO BORROW HISTORY =====
function goToBorrowHistory() {
  const token = sessionStorage.getItem("token");

  if (!token) return router.push("/LoginPage");

  // Navigate to BorrowPage history view without passing the current book id
  // to avoid accidentally triggering the borrow confirmation modal.
  router.push(`/BorrowPage?tab=history`);
}

function goHome() {
  router.push("/BookCatalog");
}
</script>

<style scoped>
/* ===== Base ===== */
.page {
  background: #fff;
  min-height: 100vh;
  color: #0a0a0a;
  font-family: "Times New Roman", serif;
}

.borrow-btn {
  font-size: 12px;
}

/* Disabled state for borrow button */
.borrow-btn:disabled,
.btn:disabled {
  opacity: 0.6;
  cursor: not-allowed;
  pointer-events: none;
}

.stock-badge {
  display: inline-flex;
  align-items: center;
  justify-content: center;

  padding: 10px 24px;
  font-size: 12px;
  font-weight: 700;

  background: #ffffff;
  color: #1c7c31; /* hijau seperti gambar */

  border: 1px solid #e5e5e5;
  border-radius: 12px;

  margin-left: 14px;
  white-space: nowrap;

  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.05); /* soft shadow */
}

.loading,
.error {
  padding: 30px;
  text-align: center;
}
.error {
  color: red;
}

.container {
  max-width: 1440px;
  margin: 0 auto;
  padding: 0 73px;
  padding-top: 103px;
  padding-bottom: 28px;
}

.grid {
  display: grid;
  grid-template-columns: 1fr 420px;
  gap: 28px;
}

.left-col {
  display: flex;
  flex-direction: column;
  gap: 28px;
}

.right-col {
  display: flex;
  flex-direction: column;
}

/* ===== Sticky Header ===== */
.sticky-header {
  position: fixed;
  inset: 0 0 auto 0;
  background: rgba(255, 255, 255, 0.95);
  border-bottom: 1px solid rgba(0, 0, 0, 0.1);
  z-index: 50;
}
.header-inner {
  max-width: 1440px;
  margin: 0 auto;
  padding: 10.5px 75px;
  display: flex;
  align-items: center;
  justify-content: space-between;
}
.left-group {
  display: flex;
  align-items: center;
  gap: 12px;
}
.back-btn {
  font-size: 12px;
  background: transparent;
  border: 0;
  display: flex;
  gap: 6px;
  align-items: center;
  color: #111827;
  cursor: pointer;
  padding: 4px 0;
  margin-right: 8px;
}
.mini-book {
  display: flex;
  align-items: center;
  gap: 14px;
}
.mini-cover {
  width: 42px;
  height: 56px;
  border-radius: 3.5px;
  overflow: hidden;
  box-shadow: 0 1px 3px rgba(0, 0, 0, 0.1);
}
.mini-cover img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}
.mini-meta {
  display: flex;
  flex-direction: column;
}
.mini-title {
  font-size: 15px;
}
.mini-author {
  color: #717182;
  font-size: 12px;
}

/* ===== Hero ===== */
.hero {
  display: flex;
  gap: 28px;
}
.hero-cover {
  width: 224px;
  height: 336px;
  border-radius: 8px;
  overflow: hidden;
  box-shadow: 0 10px 15px rgba(0, 0, 0, 0.12);
}
.hero-cover img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}
.hero-info {
  flex: 1;
  display: flex;
  flex-direction: column;
  gap: 12px;
}
.hero-title {
  font-size: 31px;
  margin: 0;
}
.hero-author {
  font-size: 17px;
  color: #717182;
}
.hero-published {
  font-size: 14px;
  color: #717182;
}

.price-stock {
  display: flex;
  gap: 14px;
  align-items: center;
}
.price {
  font-size: 20px;
  font-weight: bold;
}
.stock {
  background: #e6ffe6;
  padding: 4px 10px;
  border-radius: 8px;
  color: #0d7a0d;
  font-size: 14px;
}

/* ===== Buttons ===== */
.btn {
  height: 35px;
  padding: 0 18px;
  border-radius: 7px;
  border: 1px solid transparent;
  cursor: pointer;
  display: inline-flex;
  align-items: center;
  gap: 6px;
}
.btn-primary {
  background: #030213;
  color: white;
}
.btn-outline {
  border: 1px solid rgba(0, 0, 0, 0.15);
}
.btn-soft {
  background: #eceef2;
}
.btn-sm {
  height: 28px;
  padding: 0 12px;
}
.btn-icon {
  margin-right: 6px;
  display: inline-block;
}

/* ===== Sections ===== */
.block-title {
  font-size: 18px;
  margin-bottom: 12px;
}
.divider {
  height: 1px;
  background: rgba(0, 0, 0, 0.1);
  display: block;
  width: 100%;
}

/* On wide layouts make the divider extend into the right column so it reaches
   the container's right edge while keeping the left edge aligned. The values
   below match the right column width (420px) plus the grid gap (28px). */
@media (min-width: 1100px) {
  .divider {
    width: calc(100% + 448px); /* 420px right-col + 28px grid gap */
  }
}

/* Description */
.desc-text {
  font-family: Arial, sans-serif !important;
  line-height: 22px;
  font-size: 13px;
}
.desc-text.clamp {
  -webkit-line-clamp: 3;
  -webkit-box-orient: vertical;
  display: -webkit-box;
  overflow: hidden;
}
.link-btn {
  background: none;
  border: none;
  color: #030213;
  cursor: pointer;
  margin-top: 6px;
  font-family: Arial, sans-serif !important;
}

/* Category */
.chips {
  display: flex;
  gap: 7px;
  flex-wrap: wrap;
}
.chip {
  background: #f3f4f6;
  padding: 5px 10px;
  border-radius: 8px;
  font-size: 12px;
}

/* Accordion */
.accordion-head {
  width: 100%;
  background: none;
  border: none;
  padding: 14px 0;
  display: flex;
  justify-content: space-between;
  cursor: pointer;
}
.chev {
  transition: transform 0.2s ease;
}
.chev.open {
  transform: rotate(180deg);
}

.details-grid {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 24px;
}
.detail-label {
  color: #717182;
  font-size: 12px;
}
.detail-value {
  font-size: 14px;
}

/* Author card removed */

/* Reviews styles removed */

/* Responsive */
@media (max-width: 1100px) {
  .grid {
    grid-template-columns: 1fr;
  }
}
</style>
