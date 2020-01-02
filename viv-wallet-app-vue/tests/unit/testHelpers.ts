import { VueConstructor } from "vue/types/umd";
import BootstrapVue from "bootstrap-vue";
import { Vue } from "vue-property-decorator";
import { shallowMount, createLocalVue } from "@vue/test-utils";

const localVue = createLocalVue();
localVue.use(BootstrapVue);

export const factory = (component: VueConstructor<Vue>) => (values = {}) => {
    return shallowMount(component, {
        localVue,
        propsData: {
            ...values
        }
    });
};
