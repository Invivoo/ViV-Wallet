<template>
    <router-link v-slot="{ route, href, navigate }" :to="to" custom>
        <a
            :href="href"
            :class="route.path === $route.path ? 'router-link-active' : ''"
            :aria-current="route.path === $route.path && route.path"
            @click="navigate"
        >
            <slot></slot>
        </a>
    </router-link>
</template>

<script lang="ts">
import { defineComponent } from "vue";

export default defineComponent({
    name: "CustomRouterLink",
    props: {
        to: {
            type: String,
            required: true,
        },
    },
});
</script>

<style />
