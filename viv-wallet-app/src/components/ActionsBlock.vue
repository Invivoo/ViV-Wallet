<template>
    <Section>
        <h2>Actions</h2>
        <table v-if="actions.length > 0">
            <colgroup>
                <col style="width:25%" />
                <col style="width:40%" />
                <col style="width:10%" />
                <col style="width:25%" />
            </colgroup>
            <thead>
                <tr>
                    <th class="right">DATE DE CREATION</th>
                    <th>ACTION</th>
                    <th class="right">VIV</th>
                    <th>
                        <span class="status-header">STATUS</span>
                    </th>
                </tr>
            </thead>
            <tbody>
                <tr v-for="action in actions" :key="action.id">
                    <td class="right">{{ action.creationDate.toDateString() }}</td>
                    <td>
                        <div>
                            <div class="type">{{ action.type }} - {{ action.expertise }}</div>
                            <div class="comment">{{ action.comment }}</div>
                        </div>
                    </td>
                    <td class="right payment">{{ action.payment }}</td>
                    <td>
                        <div>
                            <div>
                                <status-badge :type="getValidationStatusType(action.status)">
                                    {{ formatValidationStatus(action.status) }}
                                </status-badge>
                            </div>
                            <div class="validation-date">
                                {{ action.validationDate ? action.validationDate.toDateString() : "" }}
                            </div>
                        </div>
                    </td>
                </tr>
            </tbody>
        </table>
        <p v-else class="none">Aucune action trouvé !</p>
    </Section>
</template>

<script lang="ts">
import { Component, Vue, Prop } from "vue-property-decorator";
import { Action, ValidationStatus } from "../models/action";
import StatusBadge from "../components/StatusBadge.vue";

@Component({
    name: "actions-block",
    components: { StatusBadge }
})
export default class ActionsBlock extends Vue {
    @Prop({ default: [] }) actions!: Action[];

    formatValidationStatus(status: ValidationStatus) {
        switch (status) {
            case ValidationStatus.Done:
                return "Validé";
            case ValidationStatus.Rejected:
            default:
                return "Rejeté";
        }
    }

    getValidationStatusType(status: ValidationStatus) {
        switch (status) {
            case ValidationStatus.Done:
                return "green";
            case ValidationStatus.Rejected:
            default:
                return "red";
        }
    }
}
</script>

<style lang="scss" scoped>
@import "../styles/table.scss";

.validation-date {
    color: $gray-600;
    font-weight: 400;
    margin-top: $m-2;
    padding-left: $m-3;
}

.status-header {
    padding-left: $m-2;
}

.payment {
    font-weight: 600;
    color: $gray-700;
}

.comment {
    color: $gray-600;
    font-weight: 400;
    margin-top: $m-2;
}

.type {
    font-weight: 600;
    color: $gray-700;
}
</style>
