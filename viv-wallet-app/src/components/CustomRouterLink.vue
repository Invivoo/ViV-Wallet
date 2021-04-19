<template>
    <router-link :to="to" custom v-slot="{ route, href, navigate }">
        <a
            :href="href"
            @click="navigate"
            :class="route.path === $route.path ? 'router-link-active' : ''"
            :aria-current="route.path === $route.path && route.path"
        >
            <slot></slot>
        </a>
    </router-link>
</template>

<script lang="ts">
import { defineComponent } from "vue";

export default defineComponent({
    name: "custom-router-link",
    props: {
        to: {
            type: String,
        },
    },
});
</script>

<style />
