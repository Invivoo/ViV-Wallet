import Banner from "@/components/Banner.vue";
import { shallowMount } from "@vue/test-utils";

describe("Banner", () => {
    it("renders user's role", async () => {
        const wrapper = shallowMount(Banner);
        await wrapper.vm.$nextTick();
        expect(wrapper.text().toLowerCase()).toContain("admin");
    });
});
