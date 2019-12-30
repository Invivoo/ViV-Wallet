<template>
    <div>
        <ol>
            <UserItem v-for="user in users" :key="user.id" v-bind:user="user" />
        </ol>
    </div>
</template>

<script lang="ts">
import { Component, Prop, Vue } from "vue-property-decorator";
import axios from "axios";
import { User } from "../models/user";
import UserItem from "@/components/UserItem.vue";

@Component({
    components: { UserItem }
})
export default class UserList extends Vue {
    @Prop() users!: User[];
}
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped></style>
