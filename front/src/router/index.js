import { createRouter, createWebHistory } from "vue-router";
import DS from "../pages/DS.vue";

const routes = [
  {
    path: "/ds",
    name: "DS",
    component: DS,
  },
];

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes,
});

export default router;
