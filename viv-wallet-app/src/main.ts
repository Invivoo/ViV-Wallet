import { createApp } from "vue";
import { applyPolyfills, defineCustomElements } from "x4b-ui/loader";
import getConfigValue from "@/utils/configUtils";
import App from "./App.vue";
import Maintenance from "./Maintenance.vue";
import router from "./router";

applyPolyfills().then(() => {
    defineCustomElements();
});

if (getConfigValue("VUE_APP_MAINTENANCE_MODE") === "true") {
    createApp(Maintenance).mount("#app");
} else {
    createApp(App).use(router).mount("#app");
}
