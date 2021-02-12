<template>
    <section>
        <h2>Users management</h2>
        <div id="users-list-container">
            <table>
                <colgroup>
                    <col style="width: 8%" />
                    <col style="width: 92%" />
                </colgroup>
                <thead>
                    <tr>
                        <th class="right">ID</th>
                        <th>NOM</th>
                    </tr>
                </thead>
                <tbody>
                    <tr v-for="user in users" :key="user.id">
                        <td class="right">{{ user.id }}</td>
                        <td>{{ user.fullName }}</td>
                    </tr>
                </tbody>
            </table>
        </div>
    </section>
</template>

<script lang="ts">
import { Component, Prop, Vue } from "vue-property-decorator";
import { User } from "../models/user";

@Component
export default class UserList extends Vue {
    @Prop() users!: User[];
    onRowSelected(item: User) {
        this.$router.push({ path: "users/" + item.id });
    }
}
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped lang="scss">
@import "../styles/table.scss";
@import "../styles/buttons.scss";

#users-list-container {
    display: flex;
    flex-direction: column;
    align-items: center;
    width: 100%;
    margin-top: $m-4;
}

h2 {
    text-align: center;
}

tbody > tr {
    &:hover td {
        background: $gray-300;
    }
}

td {
    white-space: nowrap;
    overflow: hidden;
    text-overflow: ellipsis;
}

section {
    display: flex;
    align-items: center;
    flex-direction: column;
}

button {
    align-self: flex-end;
    margin-bottom: $m-3;
}
</style>
