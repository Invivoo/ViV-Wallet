<template>
    <div id="app">
        <x4b-ui
            application="viv-wallet"
            :apps-service-url="appsUrl"
            :version="appVersion"
            disable-fake-elements="true"
            languages="fr"
            :color="primaryColor"
            @menuToggleButtonClicked="handleMenuToggleButtonClicked"
            @startupFinished="handleStartupFinished"
        >
            <div v-if="isBannerInitialized" class="root">
                <div v-bind:class="['menu', isMenuOpen ? '' : 'hidden']">
                    <custom-router-link to="/wallet">Mon wallet</custom-router-link>
                    <check-roles v-bind:roles="extendedRoles">
                        <custom-router-link to="/members">Expertises</custom-router-link>
                        <custom-router-link to="/wallets">Wallets</custom-router-link>
                        <custom-router-link to="/actions">Historique des actions</custom-router-link>
                    </check-roles>
                </div>
                <div class="content">
                    <router-view />
                </div>
            </div>
            <div v-else />
        </x4b-ui>
    </div>
</template>
<script lang="ts">
import { Component, Vue } from "vue-property-decorator";
import "x4b-ui/dist/x4b-ui/x4b-ui.css";
import { version } from "../package.json";
import CustomRouterLink from "./components/CustomRouterLink.vue";
import CheckRoles from "./components/CheckRoles.vue";
import { adminOnly, extendedRoles, Role } from "./models/role";

@Component({ components: { CustomRouterLink, CheckRoles } })
export default class App extends Vue {
    private appsUrl: string = process.env.VUE_APP_APPS_URL;
    private appVersion: string = version;
    private primaryColor: string = require("./styles/index.scss").primaryColor;
    isMenuOpen = false;
    isBannerInitialized = false;
    adminOnly = adminOnly;
    extendedRoles = extendedRoles;

    handleMenuToggleButtonClicked(evt) {
        this.isMenuOpen = evt.detail;
    }

    handleStartupFinished() {
        this.isBannerInitialized = true;
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
