<template>
    <div>
        <ol>
            <li v-for="user in users" :key="user.id">
                {{ user.login }}
            </li>
        </ol>
    </div>
</template>

<script lang="ts">
import { Component, Prop, Vue } from "vue-property-decorator";
import axios from "axios";
import { User } from "../models/user";
import { BACKEND_BASE_URL } from "../config/constants";

@Component
export default class UserList extends Vue {
    users: User[] = [];

    mounted() {
        axios.get<User[]>(`${BACKEND_BASE_URL}/users`).then(response => (this.users = response.data));
    }
}
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped></style>
