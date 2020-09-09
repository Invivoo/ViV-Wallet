<template>
    <Section>
        <div class="filter">
            <label for="filter">Filtrer par utilisateur</label>
            <input ref="filter" type="text" id="filter" v-model="filterValue" />
        </div>
        <table v-if="actions.length > 0">
            <colgroup>
                <col style="width:25%" />
                <col style="width:42%" />
                <col style="width:8%" />
                <col style="width:25%" />
            </colgroup>
            <thead>
                <tr>
                    <th>UTILISATEUR</th>
                    <th>ACTION</th>
                    <th class="right">VIV</th>
                    <th>
                        <span class="status-header">STATUS</span>
                    </th>
                </tr>
            </thead>
            <tbody>
                <tr v-for="action in filteredActions" :key="action.id">
                    <td>
                        <div>
                            <div class="user">{{ action.userfullname }}</div>
                            <div class="creation-date">{{ action.creationDate.toDateString() }}</div>
                        </div>
                    </td>
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
                        </div>
                    </td>
                </tr>
            </tbody>
        </table>
        <p v-else class="none">Aucun historique trouvé !</p>
    </Section>
</template>

<script lang="ts">
import { Component, Vue, Prop, Watch } from "vue-property-decorator";
import { ActionForHistory, PaymentStatus } from "../models/action";
import StatusBadge from "../components/StatusBadge.vue";

@Component({
    name: "action-history",
    components: { StatusBadge }
})
export default class ActionsHistory extends Vue {
    @Prop({ default: [] }) actions!: ActionForHistory[];
    filteredActions = this.actions;
    filterValue = "";

    mounted() {
        (this.$refs.filter as HTMLElement).focus();
    }

    isPaymentPaid(action: ActionForHistory) {
        return action.status === PaymentStatus.Paid;
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

    @Watch("filterValue")
    filterChanged() {
        this.filteredActions = this.actions.filter(action =>
            action.userfullname!.toLowerCase().includes(this.filterValue.toLowerCase())
        );
    }
}
</script>

<style lang="scss" scoped>
@import "../styles/table.scss";
@import "../styles/form.scss";
@import "../styles/buttons.scss";

.filter {
    display: flex;
    align-items: baseline;
    margin-bottom: $m-3;
    label {
        white-space: nowrap;
        margin: 0 $m-2 0 $m-5;
    }
    input {
        width: 40ch;
    }
}

.payment-date {
    color: $gray-600;
    font-weight: 400;
    margin-top: $m-2;
    padding-left: $m-3;
}

.creation-date {
    color: $gray-600;
    font-weight: 400;
    margin-top: $m-2;
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

.type,
.user {
    font-weight: 600;
    color: $gray-700;
}
</style>
