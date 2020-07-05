import Vue from "vue";
import VueRouter from "vue-router";
import Home from "../views/Home.vue";

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
        component: () => import(/* webpackChunkName: "users" */ "../views/Wallet.vue")
    },
    {
        path: "/users",
        name: "users",
        component: () => import(/* webpackChunkName: "users" */ "../views/Users.vue")
    },
    {
        path: "/users/:id",
        name: "userEdit",
        component: () => import(/* webpackChunkName: "user" */ "../views/UserEdit.vue"),
        props: true
    }
];

const router = new VueRouter({
    mode: "history",
    base: process.env.BASE_URL,
    routes
});

export default router;
