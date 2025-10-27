import { createRouter, createWebHistory } from "vue-router";
import DS from "../pages/DS.vue";
import QuestDashboard from "../pages/QuestDashboard.vue";

const routes = [
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
];

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes,
});

export default router;
