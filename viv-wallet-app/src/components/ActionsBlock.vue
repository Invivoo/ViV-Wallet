<template>
    <section>
        <div class="title-wrapper">
            <h2 class="title">Actions</h2>
            <check-roles :roles="adminOnly">
                <button class="primary-button" :disabled="!canSave" @click="saveChanges">Sauvergarder</button>
                <router-link class="primary-button payment-btn" :to="{ path: `/payment/${userId}` }"
                    >Payer maintenant</router-link
                >
            </check-roles>
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
                            <div class="comment" :title="action.comment">{{ action.comment }}</div>
                        </div>
                    </td>
                    <td class="right payment">{{ action.payment }}</td>
                    <td>
                        <div>
                            <div>
                                <check-roles :not="true" :roles="adminOnly">
                                    <status-badge :type="getPaymentStatusType(action.status)">
                                        {{ formatPaymentStatus(action.status) }}
                                    </status-badge>
                                </check-roles>
                                <check-roles :roles="adminOnly">
                                    <div class="select">
                                        <select v-model="action.status">
                                            <option disabled value>Choisissez</option>
                                            <option v-for="status in paymentStatusList" :key="status" :value="status">
                                                {{ formatPaymentStatus(status) }}
                                            </option>
                                        </select>
                                        <span class="select-focus"></span>
                                    </div>
                                </check-roles>
                            </div>
                            <div v-if="action.paymentDate" class="payment-date">
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
import { defineComponent, PropType, ref, watch } from "vue";
import paymentHelpers from "@/utils/paymentHelpers";
import { Action, PaymentStatus } from "../models/action";
import { adminOnly } from "../models/role";
import { WalletService } from "../services/wallet";
import CheckRoles from "./CheckRoles.vue";
import StatusBadge from "./StatusBadge.vue";

export default defineComponent({
    name: "ActionsBlock",
    components: { StatusBadge, CheckRoles },
    props: {
        actions: {
            default: () => [],
            type: Array as PropType<Action[]>,
        },
        userId: {
            required: true,
            type: String,
        },
    },
    emits: ["actionsSaved"],
    setup(props, { emit }) {
        const canSave = ref(false);
        const walletService = new WalletService();
        watch([props.actions], () => {
            canSave.value = true;
        });
        return {
            ...paymentHelpers,
            adminOnly,
            canSave,
            paymentStatusList: [PaymentStatus.PAYABLE, PaymentStatus.NON_PAYABLE, PaymentStatus.TO_VALIDATE],
            saveChanges: async () => {
                await walletService.saveActions(props.userId, props.actions);
                emit("actionsSaved");
                canSave.value = false;
            },
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

    display: -webkit-box;
    -webkit-box-orient: vertical;
    -webkit-line-clamp: 2;
}

.type {
    font-weight: 600;
    color: $gray-700;
}

.title {
    margin-top: 0;
    margin-bottom: 0;
    margin-right: auto;
}

.title-wrapper {
    display: flex;
    width: 100%;
    gap: $m-2;
    margin: $m-6 0 $m-3 0;
}
</style>
