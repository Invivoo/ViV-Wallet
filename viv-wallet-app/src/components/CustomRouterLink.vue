<template>
    <router-link v-slot="{ route, href, navigate }" :to="to" custom>
        <a
            :href="href"
            :class="isActive() ? 'router-link-active' : ''"
            :aria-current="isActive() && route.path"
            @click="navigate"
        >
            <slot></slot>
        </a>
    </router-link>
</template>

<script lang="ts">
import { defineComponent } from "vue";
import { useRoute } from "vue-router";

export default defineComponent({
    name: "CustomRouterLink",
    props: {
        to: {
            type: String,
            required: true,
        },
    },
    setup(props) {
        const route = useRoute();
        const isActive = () => {
            // I add a / character to avoid path conflicts such as /wallet and /wallets
            return `${route.path}/`.startsWith(`${props.to}/`);
        };
        return {
            isActive,
        };
    },
});
</script>

<style />
