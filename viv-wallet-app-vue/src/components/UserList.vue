<template>
    <div>
        <section v-if="errored">
            <p>We're sorry, we're not able to retrieve this information at the moment, please try back later</p>
        </section>
        <section v-else>
            <div v-if="loading">Loading...</div>
            <ol v-else>
                <li v-for="user in users" :key="user.id">{{ user.login }}</li>
            </ol>
        </section>
    </div>
</template>

<script lang="ts">
import { Component, Prop, Vue } from "vue-property-decorator";
import axios from "axios";
import { User } from "../models/user";
import { BACKEND_BASE_URL, REQUEST_TIMEOUT_MS } from "../config/constants";

@Component
export default class UserList extends Vue {
    users: User[] = [];
    loading = true;
    errored = false;

    async mounted() {
        try {
            var response = await axios.get<User[]>(`${BACKEND_BASE_URL}/users`, { timeout: REQUEST_TIMEOUT_MS });
            this.users = response.data;
        } catch (ex) {
            console.error(ex);
            this.errored = true;
        } finally {
            this.loading = false;
        }
    }
}
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped></style>
