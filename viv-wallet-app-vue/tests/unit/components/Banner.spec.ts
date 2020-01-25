import Banner from "@/components/Banner.vue";
import { shallowMount } from "@vue/test-utils";

describe("Banner", () => {
    it("renders logout button", () => {
        const wrapper = shallowMount(Banner);
        expect(wrapper.text().toLowerCase()).toContain("logout");
    });

    it("renders user name", async () => {
        const wrapper = shallowMount(Banner);
        expect(wrapper.text().toLowerCase()).toContain("montgomery");
    });

    it("renders user's role", async () => {
        const wrapper = shallowMount(Banner);
        await wrapper.vm.$nextTick();
        expect(wrapper.text().toLowerCase()).toContain("administrator");
    });
});
