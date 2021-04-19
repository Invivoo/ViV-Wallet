<template>
    <section>
        <h2>Actions</h2>
        <table v-if="actions.length > 0">
            <colgroup>
                <col style="width: 25%" />
                <col style="width: 42%" />
                <col style="width: 8%" />
                <col style="width: 25%" />
            </colgroup>
            <thead>
                <tr>
                    <th class="right">DATE DE CREATION</th>
                    <th>ACTION</th>
                    <th class="right">VIV</th>
                    <th>
                        <span class="status-header">STATUT</span>
                    </th>
                </tr>
            </thead>
            <tbody>
                <tr v-for="action in actions" :key="action.id">
                    <td class="right">{{ action.creationDate.toDateString() }}</td>
                    <td>
                        <div>
                            <div class="type">{{ action.type }}</div>
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
                        </div>
                    </td>
                </tr>
            </tbody>
        </table>
        <p v-else class="none">Aucune action trouvée !</p>
    </section>
</template>

<script lang="ts">
import { defineComponent, PropType } from "vue";
import { Action, PaymentStatus } from "../models/action";
import StatusBadge from "../components/StatusBadge.vue";

export default defineComponent({
    name: "actions-block",
    components: { StatusBadge },
    props: {
        actions: {
            default: () => [],
            type: Array as PropType<Action[]>,
        },
    },
    methods: {
        isPaymentPaid: function (action: Action) {
            return action.status === PaymentStatus.Paid;
        },

        formatPaymentStatus: function (status: PaymentStatus) {
            switch (status) {
                case PaymentStatus.Paid:
                    return "Débloqué";
                case PaymentStatus.Unpaid:
                default:
                    return "Non débloqué";
            }
        },

        getPaymentStatusType: function (status: PaymentStatus) {
            switch (status) {
                case PaymentStatus.Paid:
                    return "green";
                case PaymentStatus.Unpaid:
                default:
                    return "red";
            }
        },
    },
});
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
    overflow: hidden;
    display: -webkit-box;
    -webkit-box-orient: vertical;
    -webkit-line-clamp: 2;
}

.type {
    font-weight: 600;
    color: $gray-700;
}
</style>
