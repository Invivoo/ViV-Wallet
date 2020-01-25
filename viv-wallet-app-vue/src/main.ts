import Vue from "vue";
import App from "./App.vue";
import router from "./router";
import BootstrapVue from "bootstrap-vue";
import "bootstrap/dist/css/bootstrap.css";
import "bootstrap-vue/dist/bootstrap-vue.css";
import axios from "axios";
import { LoginService } from "@/services/login";

Vue.config.productionTip = false;
Vue.use(BootstrapVue);
new Vue({
    router,
    render: h => h(App)
}).$mount("#app");

axios.interceptors.request.use(function(config) {
    return { ...config, headers: { Authorization: new LoginService().getJwtToken() } };
});
