<template>
  <div class="library-home">
    <nav class="navbar">
      <div class="container nav-inner">
        <div class="nav-left">
          <div class="logo">
            <div class="logo-icon"></div>
            <div class="logo-text">
              <span class="lt-main">LibrarySys</span>
              <span class="lt-sub">Library Management System</span>
            </div>
          </div>
          <ul class="nav-links">
            <li><a href="#" class="active">Beranda</a></li>
            <li><a href="#">Borrowing Page</a></li>
          </ul>
        </div>

        <div class="nav-center">
          <div class="search-wrapper">
            <span class="search-icon">🔍</span>
            <input type="text" placeholder="Cari buku, jurnal, atau artikel..." />
          </div>
        </div>

        <div class="nav-right">
          <button class="btn-text">Masuk</button>
          <button class="btn-primary small">Daftar</button>
        </div>
      </div>
    </nav>

    <header class="hero-section">
      <div class="container hero-inner">
        <div class="hero-text">
          <h1>Platform Digital untuk Sumber Bacaan Anda</h1>
          <p class="hero-desc">
            Akses ribuan jurnal, buku, dan artikel penelitian dari berbagai disiplin ilmu. Dukung penelitian Anda dengan koleksi digital terlengkap.
          </p>
          <div class="hero-actions">
            <button class="btn-primary">Mulai Baca <span>→</span></button>
            <button class="btn-outline">Jelajahi Koleksi</button>
          </div>
          
          <div class="hero-stats">
            <div class="stat-item">
              <strong>50K+</strong>
              <span>Buku Digital</span>
            </div>
            <div class="stat-item">
              <strong>25K+</strong>
              <span>Jurnal Ilmiah</span>
            </div>
            <div class="stat-item">
              <strong>100K+</strong>
              <span>Artikel</span>
            </div>
          </div>
        </div>

        <div class="hero-image-wrapper">
          <div class="hero-img-container">
            <img 
                src="https://images.unsplash.com/photo-1481627834876-b7833e8f5570?ixlib=rb-4.0.3&auto=format&fit=crop&w=1000&q=80" 
                alt="Library" 
                class="main-hero-img" 
            />
                        
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
          <div class="dropdown">
            <span>Semua Kategori</span>
            <span class="chev">⌄</span>
          </div>
          <div class="fb-controls">
            <div class="dropdown">
              <span>Paling Relevan</span>
              <span class="chev">⌄</span>
            </div>
            <div class="view-toggle">
              <button class="vt-btn active">▦</button>
              <button class="vt-btn">☰</button>
            </div>
          </div>
        </div>
        <div class="fb-tags">
          <span class="fb-label">Filter oleh:</span>
          <button class="tag">Open Access ×</button>
          <button class="tag">Peer Reviewed ×</button>
          <button class="tag">Recent ×</button>
          <button class="tag-clear">Hapus semua</button>
        </div>
      </div>
    </div>

    <section class="section container">
      <div class="section-header">
        <div>
          <h2>Koleksi Unggulan</h2>
          <p class="section-sub">Buku dan jurnal terpopuler untuk penelitian</p>
        </div>
        <a href="#" class="link-all">Lihat Semua</a>
      </div>

      <div class="grid-3">
        <div v-for="(book, index) in featuredBooks" :key="index" class="book-card">
          <div class="bc-cover">
            <span class="badge-oa">Open Access</span>
            <img :src="book.image" :alt="book.title" />
          </div>
          <div class="bc-info">
            <h3 class="bc-title">{{ book.title }}</h3>
            <p class="bc-author">{{ book.author }}</p>
            <div class="bc-meta">
              <span class="rating">★ {{ book.rating }}</span>
              <span class="views">👁 {{ book.views }}</span>
              <span class="pages">📄 {{ book.pages }}p</span>
            </div>
            <div class="bc-tags">
              <span v-for="fmt in book.formats" :key="fmt">{{ fmt }}</span>
            </div>
            <button class="btn-block">
              <span class="icon">👁</span> Baca Sekarang
            </button>
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
        <button class="btn-outline-sm">↻ Lihat Semua</button>
      </div>

      <div class="list-layout">
        <div v-for="(item, index) in newBooks" :key="index" class="list-item">
          <div class="li-content">
            <h3 class="li-title">{{ item.title }}</h3>
            <p class="li-author">{{ item.author }}</p>
            <p class="li-desc">{{ item.desc }}</p>
            <div class="li-meta">
              <span class="rating">★ {{ item.rating }}</span>
              <span class="stats">📥 {{ item.downloads }}</span>
              <span class="stats">👁 {{ item.views }}</span>
              <span class="publisher"> • {{ item.publisher }}</span>
            </div>
            <div class="li-tags">
              <span v-for="tag in item.tags" :key="tag" class="tag-sm">{{ tag }}</span>
            </div>
          </div>
          <div class="li-actions">
            <span class="time-ago">🕒 {{ item.time }}</span>
            <div class="btn-group">
              <button class="btn-black sm">Baca</button>
              <button class="btn-outline sm">Download</button>
            </div>
          </div>
        </div>
      </div>
      
      <div class="center-btn-wrapper">
        <button class="btn-outline">Muat Lebih Banyak</button>
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
          <p class="footer-desc">
            Platform perpustakaan digital terdepan untuk penelitian akademik dengan akses ke ribuan buku, jurnal, dan artikel dari berbagai disiplin ilmu.
          </p>
          <div class="socials">
            <span>f</span> <span>t</span> <span>in</span> <span>yt</span>
          </div>
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
          <a href="#">Kebijakan Privasi</a> • 
          <a href="#">Syarat Penggunaan</a> • 
          <a href="#">Bantuan</a> • 
          <a href="#">Sitemap</a>
        </div>
      </div>
    </footer>
  </div>
</template>

<script setup>
import { ref } from 'vue';

// DUMMY DATA FOR FEATURED BOOKS (Koleksi Unggulan)
const featuredBooks = ref([
  {
    title: "Artificial Intelligence: A Modern Approach",
    author: "Stuart Russell, Peter Norvig • 2023",
    rating: 4.8,
    views: "25K+",
    pages: 1152,
    formats: ["PDF", "EPUB"],
    image: "https://images.unsplash.com/photo-1535905557558-afc4877a26fc?auto=format&fit=crop&w=400&q=80" // Dummy book cover
  },
  {
    title: "Principles of Modern Chemistry",
    author: "David W. Oxtoby, H. Pat Gillis • 2024",
    rating: 4.6,
    views: "12K+",
    pages: 840,
    formats: ["PDF"],
    image: "https://images.unsplash.com/photo-1532012197267-da84d127e765?auto=format&fit=crop&w=400&q=80"
  },
  {
    title: "Digital Marketing Strategy",
    author: "Simon Kingsnorth • 2024",
    rating: 4.4,
    views: "12K+",
    pages: 384,
    formats: ["PDF", "EPUB"],
    image: "https://images.unsplash.com/photo-1589829085413-56de8ae18c73?auto=format&fit=crop&w=400&q=80"
  }
]);

// DUMMY DATA FOR CATEGORIES
const categories = ref([
  { name: "Teknologi & IT", desc: "Programming, AI, Cybersecurity", count: "12,150", icon: "💻", color: "#3b82f6", bg: "#eff6ff" },
  { name: "Sains & Penelitian", desc: "Biologi, Kimia, Fisika", count: "8,750", icon: "⚗️", color: "#10b981", bg: "#ecfdf5" },
  { name: "Kesehatan & Medis", desc: "Kedokteran, Farmasi, Keperawatan", count: "6,500", icon: "♥", color: "#ef4444", bg: "#fef2f2" },
  { name: "Ilmu Sosial", desc: "Sosiologi, Psikologi, Antropologi", count: "9,200", icon: "⚖", color: "#8b5cf6", bg: "#f5f3ff" },
  { name: "Bisnis & Ekonomi", desc: "Manajemen, Keuangan, Marketing", count: "7,580", icon: "📈", color: "#f59e0b", bg: "#fffbeb" },
  { name: "Seni & Budaya", desc: "Seni Rupa, Musik, Sastra", count: "4,320", icon: "🎨", color: "#ec4899", bg: "#fdf2f8" },
  { name: "Pendidikan", desc: "Pedagogik, Kurikulum, Metodologi", count: "5,870", icon: "🎓", color: "#6366f1", bg: "#eef2ff" },
  { name: "Hukum", desc: "Konstitusi, Pidana, Perdata", count: "3,890", icon: "⚖", color: "#64748b", bg: "#f8fafc" }
]);

// DUMMY DATA FOR NEW ADDITIONS (Penambahan Terbaru)
const newBooks = ref([
  {
    title: "Quantum Computing: An Applied Approach",
    author: "Hidary, Jack D. • 2024",
    desc: "Latest developments in quantum computing theory and applications.",
    rating: 4.9,
    downloads: "2.1K+",
    views: "5K+",
    publisher: "Springer",
    tags: ["Open Access", "PDF", "Peer Reviewed"],
    time: "2 hari lalu"
  },
  {
    title: "Sustainable Energy Systems",
    author: "Smith, Jennifer • 2024",
    desc: "Comprehensive guide to renewable energy technologies.",
    rating: 4.7,
    downloads: "1.8K+",
    views: "4K+",
    publisher: "Academic Press",
    tags: ["PDF", "Peer Reviewed"],
    time: "5 hari lalu"
  },
  {
    title: "Modern Data Analytics",
    author: "Chen, Wei; Rodriguez, Maria • 2024",
    desc: "Advanced techniques in big data processing and machine learning.",
    rating: 4.8,
    downloads: "3.2K+",
    views: "7K+",
    publisher: "MIT Press",
    tags: ["Open Access", "PDF", "Peer Reviewed"],
    time: "1 minggu lalu"
  },
  {
    title: "Clinical Psychology Methods",
    author: "Johnson, David • 2024",
    desc: "Contemporary approaches to psychological assessment and therapy.",
    rating: 4.6,
    downloads: "1.1K+",
    views: "3K+",
    publisher: "Oxford University Press",
    tags: ["PDF", "Peer Reviewed"],
    time: "1 minggu lalu"
  }
]);
</script>

<style scoped>
@import url('https://fonts.googleapis.com/css2?family=Inter:wght@300;400;500;600;700&display=swap');

/* ================= GLOBAL RESET & UTILS ================= */
.library-home {
  font-family: 'Inter', sans-serif;
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
  background-color: #0B0F19;
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
  border: 1px solid #E5E7EB;
  padding: 10px 20px;
  border-radius: 6px;
  font-size: 14px;
  font-weight: 500;
}

.btn-outline-sm {
  background-color: white;
  color: #374151;
  border: 1px solid #E5E7EB;
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
  background-color: #0B0F19;
  color: white;
  padding: 8px 16px;
  border-radius: 6px;
  font-size: 12px;
  border: none;
}

.btn-block {
  width: 100%;
  background-color: #0B0F19;
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
  border-bottom: 1px solid #F3F4F6;
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

.nav-left, .logo {
  display: flex;
  align-items: center;
  gap: 12px;
}

.logo-icon {
  width: 32px;
  height: 32px;
  background-color: #0B0F19;
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

.lt-sub {
  font-size: 10px;
  color: #6B7280;
}

.nav-links {
  display: flex;
  list-style: none;
  gap: 24px;
  margin-left: 32px;
}

.nav-links a {
  text-decoration: none;
  color: #6B7280;
  font-size: 14px;
  font-weight: 500;
}

.nav-links a.active {
  color: #111827;
  font-weight: 600;
}

.search-wrapper {
  background-color: #F9FAFB;
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
  color: #9CA3AF;
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
  color: #4B5563;
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
  border-top: 1px solid #E5E7EB;
  padding-top: 24px;
}

.stat-item strong {
  display: block;
  font-size: 24px;
  font-weight: 700;
}

.stat-item span {
  font-size: 12px;
  color: #6B7280;
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
  color: #6B7280;
}

/* ================= FILTER BAR ================= */
.filter-bar {
  padding: 24px 0;
  border-bottom: 1px solid #F3F4F6;
}

.fb-top {
  display: flex;
  justify-content: space-between;
  margin-bottom: 16px;
}

.dropdown {
  border: 1px solid #E5E7EB;
  padding: 8px 12px;
  border-radius: 6px;
  font-size: 13px;
  display: flex;
  align-items: center;
  gap: 8px;
  cursor: pointer;
  background: #F9FAFB;
}

.fb-controls {
  display: flex;
  gap: 12px;
}

.view-toggle {
  display: flex;
  border: 1px solid #E5E7EB;
  border-radius: 6px;
  overflow: hidden;
}

.vt-btn {
  padding: 6px 10px;
  background: white;
  border: none;
  border-right: 1px solid #E5E7EB;
  cursor: pointer;
}

.vt-btn:last-child {
  border-right: none;
}

.vt-btn.active {
  background: #F3F4F6;
}

.fb-tags {
  display: flex;
  gap: 8px;
  align-items: center;
}

.fb-label {
  font-size: 12px;
  color: #6B7280;
  margin-right: 8px;
}

.tag {
  background: #F3F4F6;
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
  color: #EF4444;
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
  color: #6B7280;
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
  border: 1px solid #E5E7EB;
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
  border: 1px solid #E5E7EB;
}

.bc-title {
  font-size: 15px;
  font-weight: 600;
  margin-bottom: 4px;
  line-height: 1.4;
}

.bc-author {
  font-size: 11px;
  color: #6B7280;
  margin-bottom: 12px;
}

.bc-meta {
  display: flex;
  gap: 12px;
  font-size: 11px;
  color: #4B5563;
  margin-bottom: 12px;
}

.rating {
  color: #F59E0B;
  font-weight: 600;
}

.bc-tags {
  display: flex;
  gap: 6px;
}

.bc-tags span {
  font-size: 10px;
  background: #F3F4F6;
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
  border: 1px solid #E5E7EB;
  border-radius: 12px;
  padding: 20px;
  background: white;
  transition: border-color 0.2s;
}

.cat-card:hover {
  border-color: #D1D5DB;
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
  color: #6B7280;
}

.cat-title {
  font-size: 14px;
  font-weight: 600;
  margin-bottom: 4px;
}

.cat-desc {
  font-size: 11px;
  color: #6B7280;
  line-height: 1.4;
}

/* ================= NEW LIST ================= */
.list-layout {
  display: flex;
  flex-direction: column;
  gap: 16px;
}

.list-item {
  border: 1px solid #E5E7EB;
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
  color: #6B7280;
  margin-bottom: 8px;
}

.li-desc {
  font-size: 13px;
  color: #4B5563;
  margin-bottom: 12px;
  max-width: 600px;
}

.li-meta {
  font-size: 11px;
  color: #6B7280;
  display: flex;
  gap: 12px;
  margin-bottom: 12px;
}

.li-tags {
  display: flex;
  gap: 8px;
}

.tag-sm {
  background: #F3F4F6;
  padding: 4px 8px;
  border-radius: 4px;
  font-size: 10px;
  border: 1px solid #E5E7EB;
}

.li-actions {
  display: flex;
  flex-direction: column;
  align-items: flex-end;
  gap: 16px;
}

.time-ago {
  font-size: 11px;
  color: #9CA3AF;
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
  border-top: 1px solid #E5E7EB;
  padding-top: 60px;
  background: #F9FAFB;
}

.footer-inner {
  display: grid;
  grid-template-columns: 2fr 1fr 1fr 2fr;
  gap: 48px;
  margin-bottom: 48px;
}

.brand-col .footer-desc {
  font-size: 12px;
  color: #6B7280;
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
  color: #4B5563;
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
  color: #6B7280;
  margin-bottom: 12px;
  cursor: pointer;
}

.contact-info p {
  font-size: 12px;
  color: #6B7280;
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
  border: 1px solid #E5E7EB;
  padding: 8px 12px;
  border-radius: 6px 0 0 6px;
  font-size: 12px;
  outline: none;
  flex: 1;
}

.input-group button {
  background: #0B0F19;
  color: white;
  border: none;
  padding: 8px 12px;
  border-radius: 0 6px 6px 0;
  font-size: 12px;
}

.footer-bottom {
  border-top: 1px solid #E5E7EB;
  padding: 24px;
  display: flex;
  justify-content: space-between;
  font-size: 11px;
  color: #9CA3AF;
}

.fb-links a {
  color: #9CA3AF;
  text-decoration: none;
  margin: 0 4px;
}
</style>
