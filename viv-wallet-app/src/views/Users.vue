<template>
    <div class="users">
        <loading v-bind:loading="loading" v-bind:errored="errored">
            <check-roles v-bind:roles="adminOnly" v-bind:withErrorMessage="true">
                <UserList v-bind:users="users" />
            </check-roles>
        </loading>
    </div>
</template>

<script lang="ts">
import Vue from "vue";
import { User } from "../models/user";
import { UsersService } from "@/services/users";
import Loading from "../components/Loading.vue";

import UserList from "@/components/UserList.vue";
import { adminOnly } from "../models/role";
import CheckRoles from "../components/CheckRoles.vue";

export default Vue.extend({
    name: "users",
    components: { UserList, Loading, CheckRoles },
    data() {
        return {
            users: [] as User[],
            loading: true,
            errored: false,
            usersService: new UsersService(),
            adminOnly: adminOnly,
        };
    },
    async mounted() {
        try {
            this.users = await this.usersService.getUsers();
        } catch (ex) {
            this.errored = true;
        } finally {
            this.loading = false;
        }
    },
});
</script>

<style scoped lang="scss">
.users {
    max-width: 900px;
    margin: 0 auto;
    padding: $m-3 $m-5;
}
</style>
