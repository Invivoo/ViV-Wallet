<template>
    <div id="app">
        <x4b-ui
            ref="x4bui"
            application="viv-wallet"
            :apps-service-url="appsUrl"
            :version="appVersion"
            disable-fake-elements="true"
            languages="fr"
            :color="primaryColor"
        >
            <div v-if="isBannerInitialized" class="root">
                <div :class="['menu', isMenuOpen ? '' : 'hidden']">
                    <check-roles :roles="myWalletRoles">
                        <custom-router-link to="/wallet">Mon wallet</custom-router-link>
                    </check-roles>
                    <check-roles :roles="expertisesRoles">
                        <custom-router-link to="/members">Expertises</custom-router-link>
                    </check-roles>
                    <check-roles :roles="walletsRoles">
                        <custom-router-link to="/wallets">Wallets</custom-router-link>
                    </check-roles>
                    <check-roles :roles="historyRoles">
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
import "x4b-ui/dist/x4b-ui/x4b-ui.css";
import { defineComponent } from "vue";
import CheckRoles from "./components/CheckRoles.vue";
import CustomRouterLink from "./components/CustomRouterLink.vue";
import { expertisesRoles, historyRoles, myWalletRoles, walletsRoles } from "./models/role";
import getConfigValue from "./utils/configUtils";

export default defineComponent({
    name: "App",
    components: {
        "check-roles": CheckRoles,
        "custom-router-link": CustomRouterLink,
    },
    data() {
        return {
            appsUrl: getConfigValue("VUE_APP_APPS_URL"),
            appVersion: getConfigValue("VUE_APP_PRODUCT_VERSION") || "[PRODUCT_VERSION]",
            primaryColor: "#4c51bf",
            isMenuOpen: false,
            isBannerInitialized: false,
            myWalletRoles,
            expertisesRoles,
            walletsRoles,
            historyRoles,
        };
    },
    mounted() {
        (this.$refs.x4bui as HTMLElement).addEventListener("startupFinished", this.handleStartupFinished);
        (this.$refs.x4bui as HTMLElement).addEventListener(
            "menuToggleButtonClicked",
            this.handleMenuToggleButtonClicked
        );
    },
    unmounted() {
        (this.$refs.x4bui as HTMLElement).removeEventListener("startupFinished", this.handleStartupFinished);
        (this.$refs.x4bui as HTMLElement).removeEventListener(
            "menuToggleButtonClicked",
            this.handleMenuToggleButtonClicked
        );
    },
    methods: {
        handleMenuToggleButtonClicked(evt: Event) {
            this.isMenuOpen = (evt as CustomEvent<boolean>).detail;
        },
        handleStartupFinished() {
            this.isBannerInitialized = true;
        },
    },
});
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
