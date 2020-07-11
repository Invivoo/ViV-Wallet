<template>
    <div class="users">
        <section v-if="errored">
            <p>We're sorry, we're not able to retrieve this information at the moment, please try back later</p>
        </section>
        <section v-else>
            <div v-if="loading">Loading...</div>
            <UserList v-else v-bind:users="users" />
        </section>
    </div>
</template>

<script lang="ts">
import { Component, Prop, Vue } from "vue-property-decorator";
import { User } from "../models/user";
import { UsersService } from "@/services/users";

import UserList from "@/components/UserList.vue";

@Component({
    name: "users",
    components: { UserList }
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
