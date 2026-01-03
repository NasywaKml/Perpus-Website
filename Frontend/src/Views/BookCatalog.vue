<template>
  <div class="book-catalog">
    <header class="bc-header">
      <div class="bc-titlebar">
        <div class="left-group">
          <button class="back-btn" @click="goHome">
            <span class="icon">←</span>
            Back to Home
          </button>
        </div>

        <h1 class="bc-title">Pustaka</h1>

        <div class="right-group">
          <!-- notification icon removed -->
        </div>
      </div>
      <div class="bc-searchbar">
        <input v-model="search" type="text" placeholder="Search title, author..." />
      </div>
      <div class="bc-filters">
        <button :class="['filter-btn', { active: !activeTag }]" @click="setTag('')">All</button>
        <button v-for="tag in tags" :key="tag" :class="['filter-btn', { active: activeTag === tag }]" @click="setTag(tag)">{{ tag }}</button>
      </div>
    </header>

    <section class="bc-new-arrivals">
      <h2>New Arrivals</h2>
      <div class="bc-new-list">
        <div v-for="book in newArrivals" :key="book.id" class="bc-new-card">
          <img v-if="book.image" :src="book.image" class="bc-new-img" />
          <div v-else class="bc-new-img placeholder"></div>
          <div class="bc-new-info">
            <h3>{{ book.title }}</h3>
            <p class="bc-author">{{ book.author }}</p>
          </div>
        </div>
      </div>
    </section>

    <section class="bc-collection">
      <h2>Browse Collection</h2>
      <div class="bc-list">
        <div v-for="book in filteredBooks" :key="book.id" class="bc-list-item">
          <img v-if="book.image" :src="book.image" class="bc-list-img" />
          <div v-else class="bc-list-img placeholder"></div>
          <div class="bc-list-info">
            <h3>{{ book.title }}</h3>
            <p class="bc-author">by {{ book.author }}</p>
            <div class="bc-tags">
              <span v-for="tag in book.tags" :key="tag" class="bc-tag">{{ tag }}</span>
            </div>
          </div>
          <div class="bc-list-status">
            <span :class="['bc-status', book.status === 'Available' ? 'available' : 'borrowed']">
              {{ book.status }}
            </span>
            <button v-if="book.status === 'Available'" class="bc-action-btn borrow" :disabled="!userId" @click="goToBorrow(book.id)">Borrow</button>
            <button v-else class="bc-action-btn rent" disabled>Rent</button>
            <div v-if="!userId" style="color: #e74c3c; font-size: 0.9em; margin-top: 4px">Login to borrow</div>
          </div>
        </div>
      </div>
    </section>
  </div>
</template>

<script setup>
import { ref, computed, onMounted, watch } from "vue";
import { useRouter, useRoute } from "vue-router";
import axios from "axios";

const router = useRouter();
const route = useRoute();
const search = ref("");
const tags = ref([]);
const activeTag = ref("");
const books = ref([]);
const newArrivals = ref([]);
const userId = sessionStorage.getItem("idUser");

function setTag(tag) {
  activeTag.value = tag;
}

const filteredBooks = computed(() => {
  let filtered = books.value;
  if (search.value) {
    const s = search.value.toLowerCase();
    filtered = filtered.filter((b) => b.title.toLowerCase().includes(s) || b.author.toLowerCase().includes(s) || (b.isbn && b.isbn.toLowerCase().includes(s)));
  }
  if (activeTag.value) {
    filtered = filtered.filter((b) => b.tags && b.tags.includes(activeTag.value));
  }
  return filtered;
});

// Load user borrowing history once
async function loadUserBorrowHistory() {
  try {
    if (!userId) return [];
    
    const historyRes = await axios.get("/api/peminjaman/history");
    return historyRes.data || [];
  } catch (err) {
    // Jika 401/403, user mungkin belum login atau tidak punya akses
    if (err.response?.status === 401 || err.response?.status === 403) {
      return [];
    }
    console.warn("Failed to load user borrow history:", err);
    return [];
  }
}

async function loadBooks() {
  try {
    
    const res = await axios.get("/api/buku");
    const data = res.data || [];

    // Load user borrow history
    const userHistory = await loadUserBorrowHistory();

    // Build lookup map untuk cek apakah user sudah pinjam buku tertentu
    const userBorrowedTitles = new Set();
    userHistory.forEach((item) => {
      const status = (item.status || "").toString().toUpperCase();
      if (status === "DIPINJAM" || status === "PINJAM" || status === "BORROWED") {
        const title = (item.judulBuku || "").trim();
        if (title) userBorrowedTitles.add(title);
      }
    });

    books.value = data.map((b) => {
      const tagsArr = b.kategori ? b.kategori.split(",").map((t) => t.trim()) : [];
      const stock = Number(b.jumlahStok || 0);
      const title = (b.judul || "").trim();
      const userBorrowed = userBorrowedTitles.has(title);

      let status = "Available";
      if (userBorrowed) {
        status = "Borrowed";
      } else if (stock <= 0) {
        status = "Unavailable";
      }

      return {
        id: b.idBuku,
        title: b.judul || "",
        author: b.pengarang || "",
        isbn: b.isbn || "",
        image: b.urlGambarSampul || "",
        tags: tagsArr,
        status,
      };
    });

    // Tags
    const tagSet = new Set();
    books.value.forEach((b) => (b.tags || []).forEach((t) => tagSet.add(t)));
    tags.value = Array.from(tagSet);

    // New Arrivals (show all loaded books)
    newArrivals.value = books.value.slice(0);
  } catch (err) {
    console.error("Error loading books:", err);
    books.value = [];
    newArrivals.value = [];
    tags.value = [];
  }
}

function goToBorrow(bookId) {
  if (!sessionStorage.getItem("idUser")) {
    // redirect to login if not logged in
    router.push("/LoginPage");
    return;
  }
  // Open the BookDetails page for the selected book so user can see details before borrowing
  router.push(`/BookDetails/${bookId}`);
}

function goHome() {
  router.push("/");
}

onMounted(() => {
  loadBooks();
  // Initialize search from query parameter `q` if present
  if (route?.query?.q) {
    search.value = String(route.query.q || "");
  }
});

// Keep search in sync when route query changes
watch(() => route.query.q, (val) => {
  search.value = val ? String(val) : "";
});
</script>

<style scoped>
.book-catalog {
  font-family: "Inter", sans-serif;
  background: #fafbfc;
  min-height: 100vh;
  color: #222;
}
.bc-header {
  background: #fff;
  padding: 16px 0 0 0;
  border-bottom: 1px solid #eee;
}
.bc-titlebar {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 0 32px 8px 32px;
}
.bc-titlebar {
  position: relative;
}
.bc-title {
  position: absolute;
  left: 50%;
  transform: translateX(-50%);
  margin: 0;
  font-size: 20px;
}
.left-group {
  display: flex;
  align-items: center;
  gap: 8px;
}
.right-group {
  display: flex;
  align-items: center;
  gap: 8px;
}

.bc-searchbar {
  padding: 0 32px 16px 32px;
}
.bc-searchbar input {
  width: 100%;
  padding: 10px 16px;
  border-radius: 8px;
  border: 1px solid #e5e7eb;
  font-size: 15px;
  background: #f5f6fa;
}
.bc-filters {
  padding: 0 32px 16px 32px;
  display: flex;
  gap: 8px;
  flex-wrap: wrap;
}
.filter-btn {
  background: #f5f6fa;
  border: none;
  border-radius: 16px;
  padding: 6px 16px;
  font-size: 13px;
  color: #555;
  cursor: pointer;
  transition: background 0.2s, color 0.2s;
}
.filter-btn.active {
  background: #222;
  color: #fff;
}
.bc-new-arrivals {
  padding: 24px 32px 0 32px;
}
.bc-new-list {
  display: flex;
  gap: 16px;
  margin-top: 8px;
  overflow-x: auto;
  flex-wrap: nowrap;
  -webkit-overflow-scrolling: touch;
  padding-bottom: 8px;
}
.bc-new-list::-webkit-scrollbar {
  height: 8px;
}
.bc-new-list::-webkit-scrollbar-thumb {
  background: rgba(0, 0, 0, 0.08);
  border-radius: 8px;
}
.bc-new-card {
  background: #fff;
  border-radius: 12px;
  box-shadow: 0 1px 4px rgba(0, 0, 0, 0.04);
  min-width: 220px;
  width: auto;
  min-height: 220px;
  display: flex;
  flex-direction: column;
  align-items: flex-start;
  overflow: hidden;
  padding: 0;
  scroll-snap-align: start;
}
.bc-new-img {
  width: 100%;
  height: 120px;
  object-fit: cover;
  background: #f3f3f3;
}
.bc-new-img.placeholder {
  background: #f3f3f3;
  width: 100%;
  height: 120px;
}
.bc-new-info {
  padding: 12px 12px 8px 12px;
}
.bc-author {
  font-size: 12px;
  color: #888;
}
.bc-collection {
  padding: 32px;
}
.bc-list {
  display: flex;
  flex-direction: column;
  gap: 16px;
}
.bc-list-item {
  background: #fff;
  border-radius: 12px;
  box-shadow: 0 1px 4px rgba(0, 0, 0, 0.04);
  display: flex;
  align-items: center;
  padding: 16px;
  gap: 16px;
}
.bc-list-img {
  width: 60px;
  height: 80px;
  object-fit: cover;
  border-radius: 8px;
  background: #f3f3f3;
}
.bc-list-img.placeholder {
  background: #f3f3f3;
  width: 60px;
  height: 80px;
}
.bc-list-info {
  flex: 1;
}
.bc-list-info h3 {
  font-size: 16px;
  margin-bottom: 2px;
}
.bc-tags {
  margin-top: 4px;
  display: flex;
  gap: 6px;
}
.bc-tag {
  background: #f5f6fa;
  border-radius: 8px;
  font-size: 11px;
  color: #555;
  padding: 2px 8px;
}
.bc-list-status {
  display: flex;
  flex-direction: column;
  align-items: flex-end;
  gap: 8px;
  min-width: 90px;
}
.bc-status {
  font-size: 13px;
  font-weight: 500;
  margin-bottom: 4px;
}
.bc-status.available {
  color: #22c55e;
}
.bc-status.borrowed {
  color: #ef4444;
}
.bc-action-btn {
  border: none;
  border-radius: 8px;
  padding: 6px 18px;
  font-size: 13px;
  font-weight: 500;
  cursor: pointer;
  transition: background 0.2s, color 0.2s;
}
.bc-action-btn.borrow {
  background: #ffe066;
  color: #222;
}
.bc-action-btn.rent {
  background: #f3f3f3;
  color: #aaa;
  cursor: not-allowed;
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
}
</style>
