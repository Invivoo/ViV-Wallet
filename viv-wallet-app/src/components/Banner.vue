<template>
    <div id="user">{{ currentRoleName }}</div>
</template>

<script lang="ts">
import { Component, Vue } from "vue-property-decorator";
import { User } from "../models/user";
import { Role } from "../models/role";
import { LoginService } from "../services/login";

@Component
export default class Banner extends Vue {
    user: User = {
        id: "7",
        login: "tmontgomery",
        fullname: "MONTGOMERY Théophile",
        email: "theophile.montgomery@invivoo.com"
    };
    currentRoleName: string = "";
    loginService: LoginService;

    constructor() {
        super();
        this.loginService = new LoginService();
    }

    mounted() {
        switch (this.loginService.getCurrentRole()) {
            case Role.Admin:
                this.currentRoleName = "Admin";
                break;
            default:
                this.currentRoleName = "UNKNOWN";
                break;
        }
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
