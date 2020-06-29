<template>
    <header>
        <div id="user">
            <p>User: {{ user.fullname }}</p>
            <p>{{ currentRoleName }}</p>
        </div>
    </header>
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
                this.currentRoleName = "System Administrator";
                break;
            default:
                this.currentRoleName = "UNKNOWN";
                break;
        }
    }
}
</script>

<style scoped>
header {
    display: flex;
    justify-content: space-between;
}

#user {
    margin-top: 2em;
    text-align: left;
    margin-left: 1em;
}

#logout {
    margin-right: 1em;
}
</style>
