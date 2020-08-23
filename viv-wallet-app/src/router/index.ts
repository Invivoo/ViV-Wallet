import Vue from "vue";
import VueRouter from "vue-router";
import Home from "../views/Home.vue";
require("focus-visible");

Vue.use(VueRouter);

const routes = [
    {
        path: "/",
        name: "home",
        component: Home
    },
    {
        path: "/wallet",
        name: "wallet",
        component: () => import(/* webpackChunkName: "wallet" */ "../views/Wallet.vue")
    },
    {
        path: "/users",
        name: "users",
        component: () => import(/* webpackChunkName: "users" */ "../views/Users.vue")
    },
    {
        path: "/members",
        name: "members",
        component: () => import(/* webpackChunkName: "members" */ "../views/Consultants.vue")
    },
    {
        path: "/members/:id",
        name: "membersByExpertise",
        component: () => import(/* webpackChunkName: "members" */ "../views/Consultants.vue")
    },
    {
        path: "/members/:expertiseName/:consultantId",
        name: "memberEdit",
        component: () => import(/* webpackChunkName: "members" */ "../views/ConsultantEdit.vue"),
        props: true
    },
    {
        path: "/users/:id",
        name: "userEdit",
        component: () => import(/* webpackChunkName: "users" */ "../views/UserEdit.vue"),
        props: true
    }
];

const router = new VueRouter({
    mode: "history",
    base: process.env.BASE_URL,
    routes
});

export default router;
