<template>
    <slot v-if="isAuthorized()"></slot>
    <p v-else-if="withErrorMessage">Not authorized</p>
</template>

<script lang="ts">
import { defineComponent, PropType } from "vue";
import { Role } from "../models/role";
import { LoginService } from "../services/login";

export default defineComponent({
    name: "CheckRoles",
    props: {
        roles: {
            default: () => [],
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
        isAuthorized() {
            const userRoles = this.loginService.getRoles();
            for (const role of this.roles) {
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
