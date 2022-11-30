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
        not: {
            default: false,
            type: Boolean,
        },
        withErrorMessage: {
            default: false,
            type: Boolean,
        },
    },
    setup(props) {
        const loginService = new LoginService();
        const isAuthorized = () => {
            const userRoles = loginService.getRoles();
            for (const role of props.roles) {
                if (userRoles.includes(role)) {
                    return !props.not;
                }
            }
            return props.not;
        };
        return {
            isAuthorized,
        };
    },
});
</script>

<style scoped lang="scss"></style>
