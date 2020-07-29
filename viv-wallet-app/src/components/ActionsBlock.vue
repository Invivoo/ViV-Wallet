<template>
    <Section>
        <h2>Actions</h2>
        <table v-if="actions.length > 0">
            <colgroup>
                <col style="width:25%" />
                <col style="width:42%" />
                <col style="width:8%" />
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
                            <div class="comment" v-bind:title="action.comment">{{ action.comment }}</div>
                        </div>
                    </td>
                    <td class="right payment">{{ action.payment }}</td>
                    <td>
                        <div>
                            <div>
                                <status-badge :type="getPaymentStatusType(action.status)">
                                    {{ formatPaymentStatus(action.status) }}
                                </status-badge>
                            </div>
                            <div v-if="isPaymentPaid(action)" class="payment-date">
                                {{ action.paymentDate ? action.paymentDate.toDateString() : "" }}
                            </div>
                            <div v-else>
                                <button v-if="shouldDisplayPayButton()" class="tertiary-button pay-button">
                                    Payer maintenant
                                </button>
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
import { Action, PaymentStatus } from "../models/action";
import StatusBadge from "../components/StatusBadge.vue";
import { Role } from "../models/role";

@Component({
    name: "actions-block",
    components: { StatusBadge }
})
export default class ActionsBlock extends Vue {
    @Prop({ default: [] }) actions!: Action[];
    @Prop() userRole?: Role;

    isPaymentPaid(action: Action) {
        return action.status === PaymentStatus.Paid;
    }

    shouldDisplayPayButton() {
        return this.userRole && this.userRole === Role.Admin;
    }

    formatPaymentStatus(status: PaymentStatus) {
        switch (status) {
            case PaymentStatus.Paid:
                return "Payé";
            case PaymentStatus.Unpaid:
            default:
                return "Non payé";
        }
    }

    getPaymentStatusType(status: PaymentStatus) {
        switch (status) {
            case PaymentStatus.Paid:
                return "green";
            case PaymentStatus.Unpaid:
            default:
                return "red";
        }
    }
}
</script>

<style lang="scss" scoped>
@import "../styles/table.scss";
@import "../styles/buttons.scss";

.payment-date {
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
    white-space: nowrap;
    overflow: hidden;
    text-overflow: ellipsis;
}

.type {
    font-weight: 600;
    color: $gray-700;
}

.pay-button {
    margin-top: $m-1;
    margin-bottom: -0.5rem;
}
</style>
