import Vue from "vue";
import App from "./App.vue";
import router from "./router";

import { applyPolyfills, defineCustomElements } from "x4b-ui/loader";
import { Plugin } from "vue-fragment";

Vue.use(Plugin);

Vue.config.ignoredElements = [/x4b-\w*/];
applyPolyfills().then(() => {
    defineCustomElements();
});

Vue.config.productionTip = false;
new Vue({
    router,
    render: h => h(App)
}).$mount("#app");
