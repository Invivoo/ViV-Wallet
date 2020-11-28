<template>
    <Section>
        <div class="filter">
            <label class="sr-only" for="filter">Filtrer par utilisateur</label>
            <div class="filter-input">
                <div class="filter-icon">
                    <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 100 100">
                        <path
                            d="M93.5 26.375a5 5 0 01-5 5h-77a5 5 0 01-5-5v-4.667a5 5 0 015-5h77a5 5 0 015 5v4.667zM78.5 52.333a5 5 0 01-5 5h-47a5 5 0 01-5-5v-4.666a5 5 0 015-5h47a5 5 0 015 5v4.666zM63.5 78.292a5 5 0 01-5 5h-17a5 5 0 01-5-5v-4.667a5 5 0 015-5h17a5 5 0 015 5v4.667z"
                        />
                    </svg>
                </div>
                <input type="text" id="filter" placeholder="Filtrer par utilisateur" v-model="filterValue" />
            </div>
        </div>
        <table v-if="actions.length > 0">
            <colgroup>
                <col style="width: 25%" />
                <col style="width: 42%" />
                <col style="width: 8%" />
                <col style="width: 25%" />
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
                            <div class="user">{{ action.achiever.fullName }}</div>
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
                                <status-badge :type="getPaymentStatusType(action.status)">{{
                                    formatPaymentStatus(action.status)
                                }}</status-badge>
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
    components: { StatusBadge },
})
export default class ActionsHistory extends Vue {
    @Prop({ default: [] }) actions!: ActionForHistory[];
    filteredActions = this.actions;
    filterValue = "";

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
        this.filteredActions = this.actions.filter((action) =>
            action.achiever?.fullName?.toLowerCase().includes(this.filterValue.toLowerCase())
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
    input {
        width: 40ch;
        padding-left: 2.4em;
    }
    .filter-input {
        position: relative;
        .filter-icon {
            display: flex;
            flex-direction: column;
            justify-content: center;
            position: absolute;
            margin: auto;
            left: $m-2;
            top: 0;
            bottom: 0;
            width: 1.4em;
            svg {
                fill: $gray-600;
            }
        }
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

.sr-only {
    position: absolute;
    width: 1px;
    height: 1px;
    padding: 0;
    margin: -1px;
    overflow: hidden;
    clip: rect(0, 0, 0, 0);
    white-space: nowrap;
    border-width: 0;
}
</style>
