import { createApp } from "vue";
import { applyPolyfills, defineCustomElements } from "x4b-ui/loader";
import App from "./App.vue";
import router from "./router";

applyPolyfills().then(() => {
    defineCustomElements();
});

createApp(App).use(router).mount("#app");
