import { VueConstructor } from "vue/types/umd";
import BootstrapVue from "bootstrap-vue";
import { Vue } from "vue-property-decorator";
import { shallowMount, createLocalVue } from "@vue/test-utils";
import VueRouter from 'vue-router'

const localVue = createLocalVue();
localVue.use(BootstrapVue);
localVue.use(VueRouter);
const router = new VueRouter();

export const factory = (component: VueConstructor<Vue>) => (values = {}) => {
    return shallowMount(component, {
        localVue,
        router,
        propsData: {
            ...values
        }
    });
};
