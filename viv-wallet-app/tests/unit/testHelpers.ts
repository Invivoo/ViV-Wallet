import { render } from "@testing-library/vue";
import router from "@/router";

const customRender = async (Component: unknown, options = {}) => {
    const renderResult = render(Component, {
        global: {
            plugins: [router],
        },
        ...options,
    });
    await router.isReady();
    return renderResult;
};

export * from "@testing-library/vue";

export { customRender as render };
