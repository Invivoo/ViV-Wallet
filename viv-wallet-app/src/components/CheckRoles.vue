<template>
    <fragment>
        <slot v-if="isAuthorized()"></slot>
        <p v-else-if="withErrorMessage">Not authorized</p>
    </fragment>
</template>

<script lang="ts">
import Vue, { PropType } from "vue";
import { Role } from "../models/role";
import { LoginService } from "../services/login";

export default Vue.extend({
    name: "check-roles",
    props: {
        roles: {
            default: [],
            type: Array as PropType<Role[]>,
        },
        withErrorMessage: {
            default: false,
            type: Boolean,
        },
    },
    data() {
        return {
            loginService: new LoginService(),
        };
    },
    methods: {
        isAuthorized: function () {
            const userRoles = this.loginService.getRoles();
            for (let role of this.roles) {
                if (userRoles.includes(role)) {
                    return true;
                }
            }
            return false;
        },
    },
});
</script>

<style scoped lang="scss"></style>
