<template>
    <section>
        <h2>Consultants</h2>
        <div id="consultant-list-container">
            <router-link class="primary-button" to="/members/add" tag="button">Ajouter</router-link>
            <table>
                <colgroup>
                    <col style="width:5%" />
                    <col style="width:20%" />
                    <col style="width:20%" />
                    <col style="width:20%" />
                    <col style="width:35%" />
                </colgroup>
                <thead>
                    <tr>
                        <th class="right">ID</th>
                        <th>IDENTIFIANT</th>
                        <th>NOM</th>
                        <th>EMAIL</th>
                        <th>STATUS</th>
                    </tr>
                </thead>
                <tbody>
                    <tr v-on:click="onRowSelected(consultant)" v-for="consultant in consultants" :key="consultant.id">
                        <td class="right">{{ consultant.id }}</td>
                        <td>{{ consultant.user }}</td>
                        <td>{{ consultant.fullname }}</td>
                        <td>{{ consultant.email }}</td>
                        <td>{{ consultant.status }}</td>
                    </tr>
                </tbody>
            </table>
        </div>
    </section>
</template>

<script lang="ts">
import { Component, Prop, Vue } from "vue-property-decorator";
import { Consultant } from "../models/consultant";

@Component
export default class ConsultantList extends Vue {
    @Prop() consultants!: Consultant[];
    onRowSelected(item: Consultant) {
        // TODO get it from combobox :)
        this.$router.push({ path: "members/csharp/" + item.id });
    }
}
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped lang="scss">
@import "../styles/table.scss";
@import "../styles/buttons.scss";

#consultant-list-container {
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
    cursor: pointer;
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
