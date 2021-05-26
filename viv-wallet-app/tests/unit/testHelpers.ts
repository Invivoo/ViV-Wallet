import { render } from "@testing-library/vue";
import { createRouter, createWebHistory } from "vue-router";
import { routes } from "@/router";

const customRender = async (Component: unknown, options = {}) => {
    const router = createRouter({
        history: createWebHistory(process.env.BASE_URL),
        routes,
    });
    const utils = render(Component, {
        global: {
            plugins: [router],
        },
        ...options,
    });
    await router.isReady();
    return { ...utils, router };
};

export * from "@testing-library/vue";

export { customRender as render };
