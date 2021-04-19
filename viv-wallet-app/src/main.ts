import Vue, { createApp } from "vue";
import App from "./App.vue";
import router from "./router";

import { applyPolyfills, defineCustomElements } from "x4b-ui/loader";

applyPolyfills().then(() => {
    defineCustomElements();
});

createApp(App).use(router).mount("#app");
