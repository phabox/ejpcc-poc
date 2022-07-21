import Vue from "vue";
import VueRouter from "vue-router";

Vue.use(VueRouter);

const routes = [
  { path: "/", redirect: "/draws" },
  {
    path: "/draws",
    name: "DrawOverview",
    component: () => import("../views/DrawOverview.vue"),
  },
  {
    path: "/draws/:drawId/",
    name: "DrawDetails",
    component: () => import("../views/DrawDetails.vue"),
    props: true,
  },
  {
    path: "/companies",
    name: "Companies",
    component: () => import("../views/Companies.vue"),
  },
  {
    path: "/about",
    name: "About",
    component: () => import("../views/About.vue"),
  },
];

const router = new VueRouter({
  routes,
});

export default router;
