module.exports = {
    chainWebpack: (config) => {
        config.module
            .rule("vue")
            .use("vue-loader")
            .loader("vue-loader")
            .tap((options) => {
                return {
                    ...options,
                    compilerOptions: {
                        ...options.compilerOptions,
                        isCustomElement: (tag) => tag.startsWith("x4b-"),
                    },
                };
            });
    },
    css: {
        extract: { ignoreOrder: true },
        loaderOptions: {
            sass: {
                additionalData: `
            @import "@/styles/index.scss";
            `,
            },
        },
    },
};
