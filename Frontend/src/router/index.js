import { createRouter, createWebHistory } from "vue-router";

/* =============================
   IMPORT PAGE SESUAI STRUKTUR MU
============================= */
import LoginPage from "../Views/LoginPage.vue";
import BookDetails from "../Views/BookDetails.vue";
import BorrowPage from "../Views/BorrowPage.vue";
import UserProfile from "../Views/UserProfile.vue";   // <— Tambahan baru
import AdminPage from "../Views/AdminPage.vue";
import HomePage from "../Views/HomePage.vue";
import BookCatalog from "../Views/BookCatalog.vue";

/* =============================
   ROUTES
============================= */
const routes = [
  {
    path: "/HomePage",
    name: "HomePage",
    component: HomePage,
    // HomePage biasanya public, jadi tidak perlu meta requiresAuth
  },
  {
    path: "/LoginPage",
    name: "LoginPage",
    component: LoginPage,
  },
  {
    path: "/BookDetails/:id",
    name: "BookDetails",
    component: BookDetails,
    props: true,
  },
  {
    path: "/BorrowPage/:id?",
    name: "BorrowPage",
    component: BorrowPage,
    props: true,
  },
  {
    path: "/UserProfile",
    name: "UserProfile",
    component: UserProfile,
  },
  {
    path: "/AdminPage",
    name: "AdminPage",
    component: AdminPage,
    meta: { requiresAuth: true },
  },
  {
    path: "/BookCatalog",
    name: "BookCatalog",
    component: BookCatalog,
    meta: { requiresAuth: true },
  },
  {
    path: "/",
    redirect: "/HomePage",
  },
];

/* =============================
   ROUTER INSTANCE
============================= */
const router = createRouter({
  history: createWebHistory(),
  routes,
});

router.beforeEach((to, from, next) => {
  const isLoggedIn = !!sessionStorage.getItem("idUser");

  // Jika route butuh login dan user belum login
  if (to.meta.requiresAuth && !isLoggedIn) {
    return next("/LoginPage");
  }

  // Jangan biarkan user login masuk halaman login lagi
  if (to.meta.guestOnly && isLoggedIn) {
    return next("/HomePage");
  }

  return next();
});

export default router;
