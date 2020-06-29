<template>
<div id="app">
  <x4b-ui application="scenario"
          apps-service-url="http://apps.master.496a8aa9ae8c48329209.northeurope.aksapp.io"
          version="version"
          disable-fake-elements="true"
          color="#5e2572">
    <div class="content">
    <Banner></Banner>
        <div id="nav"><router-link to="/">Home</router-link> | <router-link to="/users">Users</router-link></div>
            <router-view />
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
            console.log('menuToggleButtonClicked ', e.detail);
        });

        if (!getToken()) {
            login('http://login.master.496a8aa9ae8c48329209.northeurope.aksapp.io');
        }
    }
}
</script>

<style>
html, body {
    height: 100%;
}

.content {
    height: 100%;
}

#app {
    font-family: "Avenir", Helvetica, Arial, sans-serif;
    -webkit-font-smoothing: antialiased;
    -moz-osx-font-smoothing: grayscale;
    text-align: center;
    color: #2c3e50;
    width: 100%;
    height: 100%;
    overflow: hidden;
}

#nav {
    padding: 30px;
}

#nav a {
    font-weight: bold;
    color: #2c3e50;
}

#nav a.router-link-exact-active {
    color: #42b983;
}
</style>
