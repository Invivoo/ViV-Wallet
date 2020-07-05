<template>
    <div id="app">
        <x4b-ui
            application="viv-wallet"
            :apps-service-url="appsUrl"
            :version="appVersion"
            disable-fake-elements="true"
            :color="primaryColor"
        >
            <div class="root">
                <div class="menu">
                    <Banner></Banner>
                    <router-link to="/">Home</router-link>
                    <router-link to="/users">Users</router-link>
                    <router-link to="/wallet">Wallet</router-link>
                </div>
                <div class="content">
                    <router-view />
                </div>
            </div>
        </x4b-ui>
    </div>
</template>
<script lang="ts">
import { Component, Vue } from "vue-property-decorator";
import Banner from "./components/Banner.vue";
import "x4b-ui/dist/x4b-ui/x4b-ui.css";
import { getToken, login } from "x4b-ui";
import { LOGIN_URL, APPS_URL } from "./config/constants";
import { version } from "../package.json";

@Component({ components: { Banner } })
export default class App extends Vue {
    private appsUrl: string = APPS_URL;
    private appVersion: string = version;
    private primaryColor: string = require("./styles/index.scss").primaryColor;

    mounted() {
        const ui = document.querySelector("x4b-ui");
        ui &&
            ui.addEventListener("menuToggleButtonClicked", e => {
                const menu = document.querySelector(".menu");
                menu && menu.classList.toggle("hidden");
            });

        if (!getToken()) {
            login(LOGIN_URL);
        }
    }
}
</script>

<style lang="scss">
html,
body {
    height: 100%;
    box-sizing: content-box;
}

.content {
    height: 100%;
    padding-left: $m-6;
    padding-top: $m-6;
    padding-right: $m-6;
    flex-grow: 1;
    flex-shrink: 1;
    overflow-y: auto;
}

.root {
    display: flex;
    flex-wrap: nowrap;
    justify-content: space-between;
    width: 100%;
    height: 100%;
    overflow: hidden;
}

.menu {
    flex-grow: 0;
    flex-shrink: 0;
    background-color: $primary-700;
    display: flex;
    flex-direction: column;
    color: $white;
}

.menu.hidden {
    display: none;
}

.menu a {
    padding: $m-3 $m-5;
    color: $green-200;
}

.menu a:hover {
    background-color: $primary-400;
}

.menu a.router-link-exact-active {
    background-color: $primary-200;
    color: $green-700;
}

#app {
    font-family: "Open Sans", sans-serif;
    font-size: 16px;
    -webkit-font-smoothing: antialiased;
    -moz-osx-font-smoothing: grayscale;
    text-align: center;
    color: $black;
    background-color: $gray-100;
    width: 100%;
    height: 100vh;
    overflow: hidden;
    margin: 0;
}
</style>
