import { createRouter, createWebHistory } from "vue-router";
import DS from "../pages/DS.vue";
import QuestForm from "../pages/questForm.vue";

const routes = [
  {
    path: "/ds",
    name: "DS",
    component: DS, 
  },
  {
    path: "/quest",
    name: "QuestForm",
    component: QuestForm,
  }
];

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes,
});

export default router;
