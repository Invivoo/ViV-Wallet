<template>
    <router-link :to="to" v-slot="{ route, href, navigate }">
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
import { Component, Vue, Prop } from "vue-property-decorator";

@Component
export default class CustomRouterLink extends Vue {
    @Prop() to!: string;
}
</script>

<style />
