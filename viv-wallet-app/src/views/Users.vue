<template>
    <div class="users">
        <loading v-bind:loading="loading" v-bind:errored="errored">
            <check-roles v-bind:roles="adminOnly" withErrorMessage="true">
                <UserList v-bind:users="users" />
            </check-roles>
        </loading>
    </div>
</template>

<script lang="ts">
import { Component, Prop, Vue } from "vue-property-decorator";
import { User } from "../models/user";
import { UsersService } from "@/services/users";
import Loading from "../components/Loading.vue";

import UserList from "@/components/UserList.vue";
import { adminOnly, Role } from "../models/role";
import CheckRoles from "../components/CheckRoles.vue";

@Component({
    name: "users",
    components: { UserList, Loading, CheckRoles },
})
export default class Users extends Vue {
    users: User[] = [];
    loading = true;
    errored = false;
    usersService = new UsersService();
    adminOnly = adminOnly;

    async mounted() {
        try {
            this.users = await this.usersService.getUsers();
        } catch (ex) {
            this.errored = true;
        } finally {
            this.loading = false;
        }
    }
}
</script>

<style scoped lang="scss">
.users {
    max-width: 900px;
    margin: 0 auto;
    padding: $m-3 $m-5;
}
</style>
