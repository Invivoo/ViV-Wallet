<template>
    <table v-if="consultants.length > 0">
        <colgroup>
            <col style="width: 30%" />
            <col style="width: 30%" />
            <col style="width: 20%" />
            <col style="width: 20%" />
        </colgroup>
        <thead>
            <tr>
                <th>NOM</th>
                <th>EXPERTISE</th>
                <th>
                    <span class="status-header">STATUT</span>
                </th>
                <th />
            </tr>
        </thead>
        <tbody>
            <tr v-for="consultant in consultants" :key="consultant.id">
                <td>
                    <div>{{ consultant.fullName }}</div>
                </td>
                <td>
                    <div>{{ consultant.expertise ? consultant.expertise.expertiseName : "" }}</div>
                </td>
                <td>
                    <status-badge :type="getConsultantStatusType(consultant.status)">{{
                        formatConsultantStatus(consultant.status)
                    }}</status-badge>
                </td>
                <td class="no-padding">
                    <slot :consultantId="consultant.id"></slot>
                </td>
            </tr>
        </tbody>
    </table>
    <p v-else class="none">Aucun consultant dans cette expertise !</p>
</template>

<script lang="ts">
import { defineComponent, PropType } from "vue";
import StatusBadge from "../components/StatusBadge.vue";
import { Consultant, ConsultantStatus, toString } from "../models/consultant";

export default defineComponent({
    name: "ConsultantList",
    components: { StatusBadge },
    props: {
        consultants: {
            default: () => [],
            type: Array as PropType<Consultant[]>,
        },
    },
    methods: {
        formatConsultantStatus: function (status: ConsultantStatus) {
            return toString(status);
        },
        getConsultantStatusType: function (status: ConsultantStatus) {
            switch (status) {
                case ConsultantStatus.CONSULTANT_SENIOR:
                case ConsultantStatus.MANAGER:
                    return "green";
                case ConsultantStatus.CONSULTANT_SENIOR_IN_ONBOARDING:
                    return "yellow";
                default:
                    return "red";
            }
        },
    },
});
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped lang="scss">
@import "../styles/table.scss";
@import "../styles/buttons.scss";

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

.status-header {
    padding-left: $m-3;
}

td.no-padding {
    padding: 0;
}
.update-button {
    margin-left: $m-1;
}

p.none {
    margin-left: $m-5;
    margin-top: $m-6;
}
</style>
