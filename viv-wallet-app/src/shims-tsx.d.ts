/* eslint-disable unicorn/no-abusive-eslint-disable */
/* eslint-disable */
import Vue, { VNode } from "vue";

declare global {
    namespace JSX {
        // tslint:disable no-empty-interface
        type Element = VNode;
        // tslint:disable no-empty-interface
        type ElementClass = Vue;
        interface IntrinsicElements {
            [elem: string]: any;
        }
    }
}

declare module "@vue/runtime-core" {
    export interface ComponentCustomProperties {
        config: { isCustomElement: (tag: string) => boolean };
    }
}
