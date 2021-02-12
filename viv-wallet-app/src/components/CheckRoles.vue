<template>
    <fragment>
        <slot v-if="isAuthorized()"></slot>
        <p v-else-if="withErrorMessage">Not authorized</p>
    </fragment>
</template>

<script lang="ts">
import { Component, Prop, Vue } from "vue-property-decorator";
import { Role } from "../models/role";
import { LoginService } from "../services/login";

@Component({
    name: "check-roles",
})
export default class CheckRoles extends Vue {
    @Prop({ default: [] }) roles!: Role[];
    @Prop({ default: false }) withErrorMessage!: boolean;
    loginService = new LoginService();

    isAuthorized() {
        const userRoles = this.loginService.getRoles();
        for (let role of this.roles) {
            if (userRoles.includes(role)) {
                return true;
            }
        }
        return false;
    }
}
</script>

<style scoped lang="scss"></style>
