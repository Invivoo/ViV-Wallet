import { shallowMount, createLocalVue } from "@vue/test-utils";
import VueRouter from "vue-router";

const localVue = createLocalVue();
localVue.use(VueRouter);
const router = new VueRouter();
export const factory = (component) => (values = {}) => {
    return shallowMount(component, {
        localVue,
        router,
        propsData: {
            ...values,
        },
    });
};
