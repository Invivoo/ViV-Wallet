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
import axios from "axios";
import { User } from "../models/user";
import { BACKEND_BASE_URL, REQUEST_TIMEOUT_MS } from "../config/constants";
// @ is an alias to /src
import UserList from "@/components/UserList.vue";

@Component({
    name: "users",
    components: { UserList }
})
export default class Users extends Vue {
    users: User[] = [];
    loading = true;
    errored = false;

    async mounted() {
        try {
            var response = await axios.get<User[]>(`${BACKEND_BASE_URL}/users`, { timeout: REQUEST_TIMEOUT_MS });
            this.users = response.data;
        } catch (ex) {
            this.errored = true;
        } finally {
            this.loading = false;
        }
    }
}
</script>
