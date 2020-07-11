import Vue from "vue";
import App from "./App.vue";
import router from "./router";
import axios from "axios";
import { LoginService } from "@/services/login";

import { applyPolyfills, defineCustomElements } from "x4b-ui/loader";

Vue.config.ignoredElements = [/x4b-\w*/];
applyPolyfills().then(() => {
    defineCustomElements();
});

Vue.config.productionTip = false;
new Vue({
    router,
    render: h => h(App)
}).$mount("#app");

axios.interceptors.request.use(function(config) {
    return { ...config, headers: { Authorization: new LoginService().getJwtToken() } };
});
