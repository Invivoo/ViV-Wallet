
<template>
<div id="app">
  <x4b-ui application="viv-wallet"
          apps-service-url="http://apps.master.496a8aa9ae8c48329209.northeurope.aksapp.io"
          version="version"
          disable-fake-elements="true"
          color="#4C51BF">
    <div class="root">
        <div class="menu">
          <Banner></Banner>
          <router-link to="/">Home</router-link>
          <router-link to="/users">Users</router-link>
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
import 'x4b-ui/dist/x4b-ui/x4b-ui.css';
import { getToken, login } from "x4b-ui";

@Component({ components: { Banner } })
export default class App extends Vue {
    mounted() {
        const ui = document.querySelector('x4b-ui');
        ui.addEventListener('menuToggleButtonClicked', e => {
            document.querySelector('.menu').classList.toggle('hidden');
        });

        if (!getToken()) {
            login('http://login.master.496a8aa9ae8c48329209.northeurope.aksapp.io');
        }
    }
}
</script>

<style lang="scss">
html, body {
    height: 100%;
    box-sizing: content-box;
}

.content {
    height: 100%;
    padding-left: 2rem;
    padding-top: 2rem;
    padding-right: 2rem;
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
    background-color: var(--app-primary-color);
    display: flex;
    flex-direction: column;
    color: #fff;
}

.menu.hidden {
    display: none;
}

.menu a {
    padding: 0.92857143em 1.14285714em;
    color: rgb(184, 184, 230);
}

.menu a:hover {
    background-color: #f2f2f2;
}

.menu a.router-link-exact-active {
    background-color: #6b6c99;
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
