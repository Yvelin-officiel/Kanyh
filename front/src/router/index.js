import { createRouter, createWebHistory } from "vue-router";
import Home from "../pages/Home.vue";
import DS from "../pages/DS.vue";
import QuestDashboard from "../pages/QuestDashboard.vue";
import AdventurersView from "../pages/AdventurersView.vue";
import Login from "../pages/Login.vue";
import Register from "../pages/Register.vue";

const routes = [
  {
    path: "/",
    name: "Home",
    component: Home,
  },
  {
    path: "/quest",
    name: "QuestDashboard",
    component: QuestDashboard,
  },
  {
    path: "/ds",
    name: "DS",
    component: DS,
  },
  {
    path: "/adventurers",
    name: "AdventurersView",
    component: AdventurersView,
  },
  {
    path: "/login",
    name: "Login",
    component: Login,
  },
  {
    path: "/register",
    name: "Register",
    component: Register,
  },
];

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes,
});

export default router;
