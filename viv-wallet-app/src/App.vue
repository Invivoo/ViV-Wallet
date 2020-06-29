<template>
<div id="app">
  <x4b-ui application="scenario"
          appsServiceUrl="http://apps.master.496a8aa9ae8c48329209.northeurope.aksapp.io/applications"
          version="version"
          color="#5e2572">
    <Banner></Banner>
    <div id="nav"><router-link to="/">Home</router-link> | <router-link to="/users">Users</router-link></div>
    <router-view />
  </x4b-ui>
</div>
</template>
<script lang="ts">
import { Component, Vue } from "vue-property-decorator";
import Banner from "./components/Banner.vue";

@Component({ components: { Banner } })
export default class App extends Vue {}
</script>

<style>
#app {
    font-family: "Avenir", Helvetica, Arial, sans-serif;
    -webkit-font-smoothing: antialiased;
    -moz-osx-font-smoothing: grayscale;
    text-align: center;
    color: #2c3e50;
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
