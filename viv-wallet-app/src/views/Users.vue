<template>
    <div class="users">
        <loading v-bind:loading="loading" v-bind:errored="errored">
            <UserList v-bind:users="users" />
        </loading>
    </div>
</template>

<script lang="ts">
import { Component, Prop, Vue } from "vue-property-decorator";
import { User } from "../models/user";
import { UsersService } from "@/services/users";
import Loading from "../components/Loading.vue";

import UserList from "@/components/UserList.vue";

@Component({
    name: "users",
    components: { UserList, Loading }
})
export default class Users extends Vue {
    users: User[] = [];
    loading = true;
    errored = false;
    usersService = new UsersService();

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
