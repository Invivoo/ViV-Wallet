module.exports = {
    root: true,
    env: {
        browser: true,
        node: true,
        es6: true,
    },
    plugins: ["@typescript-eslint", "simple-import-sort", "testing-library", "jest-dom"],
    extends: [
        "eslint:recommended",
        "plugin:vue/vue3-recommended",
        "@vue/prettier",
        "@vue/prettier/@typescript-eslint",
        "@vue/typescript",
        "plugin:@typescript-eslint/recommended",
        "plugin:prettier/recommended",
        "plugin:security/recommended",
        "plugin:sonarjs/recommended",
        "plugin:unicorn/recommended",
        "plugin:testing-library/vue",
        "plugin:jest-dom/recommended",
        "plugin:vuejs-accessibility/recommended",
        "prettier",
    ],
    parserOptions: {
        ecmaVersion: 2020,
    },
    rules: {
        "no-debugger": process.env.NODE_ENV === "production" ? "error" : "off",
        "no-console": ["error", { allow: ["warn", "error", "debug"] }],
        "no-extra-boolean-cast": "off",
        "@typescript-eslint/explicit-function-return-type": "off",
        "@typescript-eslint/explicit-member-accessibility": "off",
        "@typescript-eslint/explicit-module-boundary-types": "off",
        "@typescript-eslint/ban-types": "off",
        "@typescript-eslint/no-var-requires": "off",
        "@typescript-eslint/no-empty-function": "off",
        "unicorn/prevent-abbreviations": "off",
        "@typescript-eslint/no-use-before-define": "off",
        "security/detect-object-injection": "off",
        "unicorn/filename-case": "off",
        "unicorn/no-useless-undefined": ["error", { checkArguments: false }],
        "unicorn/no-array-reduce": "off",
        "unicorn/no-array-for-each": "off",
        "prettier/prettier": [
            "error",
            {
                endOfLine: "auto",
            },
        ],
        "simple-import-sort/exports": "error",
        "simple-import-sort/imports": [
            "error",
            {
                // The default grouping, but with no blank lines.
                groups: [["^\\u0000", "^@?\\w", "^", "^\\."]],
            },
        ],
        "vuejs-accessibility/label-has-for": [
            "error",
            {
                required: {
                    some: ["nesting", "id"],
                },
            },
        ],
    },
    overrides: [
        {
            files: ["**/__tests__/*.{j,t}s?(x)", "**/tests/unit/**/*.spec.{j,t}s?(x)"],
            env: {
                jest: true,
            },
            rules: {
                "sonarjs/no-duplicate-string": "off",
                "sonarjs/no-identical-functions": "off",
                "unicorn/no-useless-undefined": "off",
                "@typescript-eslint/no-non-null-assertion": "off",
            },
        },
    ],
};
