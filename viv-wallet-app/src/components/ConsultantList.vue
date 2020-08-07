<template>
    <section>
        <h2>Consultants</h2>
        <div id="consultant-list-container">
            <router-link class="primary-button add-button" to="/members/add" tag="button">Ajouter</router-link>
            <table>
                <colgroup>
                    <col style="width:28%" />
                    <col style="width:35%" />
                    <col style="width:22%" />
                    <col style="width:15%" />
                </colgroup>
                <thead>
                    <tr>
                        <th>NOM</th>
                        <th>EMAIL</th>
                        <th>
                            <span class="status-header">STATUS</span>
                        </th>
                        <th />
                    </tr>
                </thead>
                <tbody>
                    <tr v-for="consultant in consultants" :key="consultant.id">
                        <td>
                            <div>{{ consultant.fullname }}</div>
                            <div class="username">{{ consultant.user }}</div>
                        </td>
                        <td class="email">{{ consultant.email }}</td>
                        <td>
                            <status-badge :type="getConsultantStatusType(consultant.status)">{{
                                formatConsultantStatus(consultant.status)
                            }}</status-badge>
                        </td>
                        <td class="no-padding">
                            <router-link
                                v-bind:to="`/members/${expertise}/${consultant.id}`"
                                class="tertiary-button pay-button"
                                tag="button"
                                >Mettre à jour</router-link
                            >
                        </td>
                    </tr>
                </tbody>
            </table>
        </div>
    </section>
</template>

<script lang="ts">
import { Component, Prop, Vue } from "vue-property-decorator";
import { Consultant, ConsultantStatus } from "../models/consultant";
import StatusBadge from "../components/StatusBadge.vue";

@Component({
    name: "consultant-list",
    components: { StatusBadge }
})
export default class ConsultantList extends Vue {
    @Prop() consultants!: Consultant[];
    // TODO get it from combobox :)
    expertise: string = "csharp";

    formatConsultantStatus(status: ConsultantStatus) {
        switch (status) {
            case ConsultantStatus.CONSULTANT_SENIOR:
                return "Senior";
            case ConsultantStatus.MANAGER:
                return "Manager";
            case ConsultantStatus.CONSULTANT_SENIOR_IN_ONBOARDING:
                return "Onboarding";
            default:
                return "Inconnu";
        }
    }

    getConsultantStatusType(status: ConsultantStatus) {
        switch (status) {
            case ConsultantStatus.CONSULTANT_SENIOR:
            case ConsultantStatus.MANAGER:
                return "green";
            case ConsultantStatus.CONSULTANT_SENIOR_IN_ONBOARDING:
                return "yellow";
            default:
                return "red";
        }
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

td {
    white-space: nowrap;
    overflow: hidden;
    text-overflow: ellipsis;
}

.username {
    color: $gray-600;
    font-weight: 400;
    margin-top: $m-1;
}

.email {
    color: $gray-700;
    font-size: $text-base;
}

section {
    display: flex;
    align-items: center;
    flex-direction: column;
}

.add-button {
    align-self: flex-end;
    margin-bottom: $m-3;
}
.status-header {
    padding-left: $m-3;
}

td.no-padding {
    padding: 0;
}
</style>
