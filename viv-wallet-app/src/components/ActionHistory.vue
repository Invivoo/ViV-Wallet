<template>
    <section>
        <filter-input v-model="filterValue" />
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
                        <span class="status-header">STATUT</span>
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
                            <div class="type">{{ action.type }}</div>
                            <div class="comment" :title="action.comment">{{ action.comment }}</div>
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
    </section>
</template>

<script lang="ts">
import { defineComponent, PropType, ref, watch } from "vue";
import paymentHelpers from "@/utils/paymentHelpers";
import { ActionForHistory } from "../models/action";
import FilterInput from "./FilterInput.vue";
import StatusBadge from "./StatusBadge.vue";

export default defineComponent({
    name: "ActionHistory",
    components: { StatusBadge, FilterInput },
    props: {
        actions: {
            default: () => [],
            type: Array as PropType<ActionForHistory[]>,
        },
    },
    setup(props) {
        const filterValue = ref("");
        const filteredActions = ref([...props.actions]);
        watch(filterValue, (currentValue) => {
            filteredActions.value = props.actions.filter((action) =>
                action.achiever?.fullName?.toLowerCase().includes(currentValue.toLowerCase())
            );
        });
        return {
            filterValue,
            filteredActions,
            ...paymentHelpers,
        };
    },
});
</script>

<style lang="scss" scoped>
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
    overflow: hidden;
    display: -webkit-box;
    -webkit-box-orient: vertical;
    -webkit-line-clamp: 2;
}

.type,
.user {
    font-weight: 600;
    color: $gray-700;
}
</style>
