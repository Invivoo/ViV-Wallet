import { VueConstructor } from "vue/types/umd";
import { Vue } from "vue-property-decorator";
import { shallowMount, createLocalVue } from "@vue/test-utils";
import VueRouter from "vue-router";

const localVue = createLocalVue();
localVue.use(VueRouter);
const router = new VueRouter();

export const factory = (component: VueConstructor<Vue>) => (values = {}) => {
    return shallowMount(component, {
        localVue,
        router,
        propsData: {
            ...values,
        },
    });
};
