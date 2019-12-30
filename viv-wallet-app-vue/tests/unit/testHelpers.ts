import { VueConstructor } from "vue/types/umd";
import { Vue } from "vue-property-decorator";
import { shallowMount } from "@vue/test-utils";

export const factory = (component: VueConstructor<Vue>) => (values = {}) => {
    return shallowMount(component, {
        propsData: {
            ...values
        }
    });
};
