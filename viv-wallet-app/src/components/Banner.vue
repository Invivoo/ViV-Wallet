<template>
    <div id="user">{{ currentRoleNames }}</div>
</template>

<script lang="ts">
import { Component, Vue } from "vue-property-decorator";
import { User } from "../models/user";
import { LoginService } from "../services/login";

@Component
export default class Banner extends Vue {
    user: User = {
        id: "7",
        user: "tmontgomery",
        fullName: "MONTGOMERY Théophile",
        email: "theophile.montgomery@invivoo.com"
    };
    currentRoleNames: string = "";
    loginService: LoginService;

    constructor() {
        super();
        this.loginService = new LoginService();
    }

    mounted() {
        this.currentRoleNames = this.loginService.getRoles().join(", ");
    }
}
</script>

<style lang="scss" scoped>
#user {
    padding: $m-3 $m-6;
    text-align: left;
    font-size: $text-xs;
    font-style: italic;
    color: $white;
    font-weight: 400;
}
</style>
