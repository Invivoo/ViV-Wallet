<template>
    <div id="app">
        <x4b-ui
            application="viv-wallet"
            :apps-service-url="appsUrl"
            :login-service-url="loginUrl"
            :version="appVersion"
            disable-fake-elements="true"
            languages="fr"
            :color="primaryColor"
            @menuToggleButtonClicked="handleMenuToggleButtonClicked"
        >
            <div class="root">
                <div v-bind:class="['menu', isMenuOpen ? '' : 'hidden']">
                    <custom-router-link to="/users">Utilisateurs</custom-router-link>
                    <custom-router-link to="/wallet">Mon wallet</custom-router-link>
                    <custom-router-link to="/members">Mon expertise</custom-router-link>
                    <custom-router-link to="/wallets">Wallets</custom-router-link>
                    <custom-router-link to="/actions">Historique des actions</custom-router-link>
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
import "x4b-ui/dist/x4b-ui/x4b-ui.css";
import { version } from "../package.json";
import CustomRouterLink from "./components/CustomRouterLink.vue";
import { LoginService } from "./services/login";

@Component({ components: { CustomRouterLink } })
export default class App extends Vue {
    private appsUrl: string = process.env.VUE_APP_APPS_URL;
    private loginUrl: string = process.env.VUE_APP_LOGIN_URL;
    private appVersion: string = version;
    private primaryColor: string = require("./styles/index.scss").primaryColor;
    private loginService: LoginService = new LoginService();
    isMenuOpen = false;

    handleMenuToggleButtonClicked(evt) {
        this.isMenuOpen = evt.detail;
    }

    mounted() {
        const ui = document.querySelector("x4b-ui");
        this.loginService.ensureLoggedIn();
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
    background-color: $gray-100;
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
    text-align: left;
    width: 270px;
    a {
        padding: $m-3 $m-5 $m-3 $m-6;
        color: $primary-200;
        outline: none;
        text-decoration: none;
        font-size: $text-lg;
        font-weight: 400;
        &:hover {
            background-color: $primary-600;
            color: $white;
        }
    }
}

a.router-link-active {
    background-color: $primary-600;
    color: $white;
    font-weight: 600;
}

.menu.hidden {
    display: none;
}

#app {
    font-family: "Open Sans", sans-serif;
    font-size: 16px;
    -webkit-font-smoothing: antialiased;
    -moz-osx-font-smoothing: grayscale;
    text-align: left;
    color: $black;
    background-color: $gray-100;
    width: 100%;
    height: 100vh;
    overflow: hidden;
    margin: 0;
}
</style>
