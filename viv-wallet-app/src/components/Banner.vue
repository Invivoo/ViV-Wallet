<template>
      <div id="user">
        {{ currentRoleName }}
      </div>
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

<style scoped>
#user {
    text-align: center;
    padding: 1em;
    font-size: 0.8em;
    font-style: italic;
    cursor: pointer;
}
</style>
