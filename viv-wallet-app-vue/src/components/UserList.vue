<template>
    <div>
        <b-table striped hover :items="users"></b-table>
    </div>
</template>

<script lang="ts">
import { Component, Prop, Vue } from "vue-property-decorator";
import axios from "axios";
import { User } from "../models/user";

@Component
export default class UserList extends Vue {
    @Prop() users!: User[];
}
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped></style>
