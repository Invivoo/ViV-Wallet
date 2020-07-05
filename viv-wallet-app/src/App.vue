<template>
    <div id="app">
        <Banner></Banner>
        <div id="nav">
            <router-link to="/">Home</router-link>| <router-link to="/users">Users</router-link>|
            <router-link to="/wallet">Wallet</router-link>
        </div>
        <router-view />
    </div>
</template>
<script lang="ts">
import { Component, Vue } from "vue-property-decorator";
import Banner from "./components/Banner.vue";

@Component({ components: { Banner } })
export default class App extends Vue {}
</script>

<style lang="scss">
#app {
    font-family: "Open Sans", sans-serif;
    font-size: 16px;
    -webkit-font-smoothing: antialiased;
    -moz-osx-font-smoothing: grayscale;
    text-align: center;
    color: $black;
    background-color: $gray-100;
    height: 100vh;
    margin: 0;
}

#nav {
    padding: 30px;
    a {
        font-weight: bold;
        color: $black;
    }

    a.router-link-exact-active {
        color: $green-700;
    }
}
</style>
