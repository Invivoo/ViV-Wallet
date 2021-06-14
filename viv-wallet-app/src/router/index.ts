import { createRouter, createWebHistory, RouteRecordRaw } from "vue-router";
import Home from "../views/Home.vue";

require("focus-visible");

export const routes: Array<RouteRecordRaw> = [
    {
        path: "/",
        name: "home",
        component: Home,
    },
    {
        path: "/wallet",
        name: "wallet",
        component: () => import(/* webpackChunkName: "wallets" */ "../views/Wallet.vue"),
    },
    {
        path: "/actions",
        name: "actions",
        component: () => import(/* webpackChunkName: "actions" */ "../views/History.vue"),
    },
    {
        path: "/members",
        name: "members",
        component: () => import(/* webpackChunkName: "members" */ "../views/Consultants.vue"),
    },
    {
        path: "/members/:id",
        name: "membersByExpertise",
        component: () => import(/* webpackChunkName: "members" */ "../views/Consultants.vue"),
    },
    {
        path: "/members/:expertiseName/:consultantId",
        name: "memberEdit",
        component: () => import(/* webpackChunkName: "members" */ "../views/ConsultantEdit.vue"),
        props: true,
    },
    {
        path: "/payment/:id",
        name: "payment",
        component: () => import(/* webpackChunkName: "user" */ "../views/Payment.vue"),
        props: true,
    },
    {
        path: "/wallets",
        name: "wallets",
        component: () => import(/* webpackChunkName: "wallets" */ "../views/Wallets.vue"),
    },
    {
        path: "/wallets/:consultantId",
        name: "walletView",
        component: () => import(/* webpackChunkName: "wallets" */ "../views/Wallet.vue"),
        props: true,
    },
];

const router = createRouter({
    history: createWebHistory(process.env.BASE_URL),
    routes,
});

export default router;
