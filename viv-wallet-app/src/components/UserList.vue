<template>
    <section>
        <h2>Users management</h2>
        <div id="users-list-container">
            <div id="users-list-actions">
                <b-button id="add-user-button" variant="primary" to="/users/add">
                    Add
                </b-button>
            </div>
            <b-table
                id="users-list"
                striped
                hover
                selectable
                primary-key="id"
                :items="users"
                @row-selected="onRowSelected"
            >
            </b-table>
        </div>
    </section>
</template>

<script lang="ts">
import { Component, Prop, Vue } from "vue-property-decorator";
import axios from "axios";
import { User } from "../models/user";

@Component
export default class UserList extends Vue {
    @Prop() users!: User[];

    onRowSelected(items: User[]) {
        this.$router.push({ path: "users/" + items[0].id });
    }
}
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped>
#users-list-container {
    display: flex;
    flex-direction: column;
    align-items: center;
    width: 90%;
}

#users-list {
    top: 3em;
}

#users-list-actions {
    margin-bottom: 1.5em;
    margin-top: 1.5em;
    min-width: 100%;
    text-align: right;
}

h2 {
    text-align: center;
}

section {
    display: flex;
    align-items: center;
    flex-direction: column;
}
</style>
