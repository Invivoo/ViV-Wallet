<template>
    <li>{{ user.login }}</li>
</template>

<script lang="ts">
import { Component, Prop, Vue } from "vue-property-decorator";
import { User } from "../models/user";

@Component
export default class UserItem extends Vue {
    @Prop() user!: User;
}
</script>

<style />
