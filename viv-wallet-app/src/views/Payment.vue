<template>
    <div class="payment">
        <loading :loading="loading" :errored="errored">
            <section>
                <h2>Paiement</h2>
                <div class="header">
                    <balance-card
                        :full-name="user.fullName"
                        :expertise="(user.expertise && user.expertise.expertiseName) || ''"
                        :consultant-status="formatConsultantStatus(user.status)"
                        :viv-balance="balance"
                    />
                    <illustration />
                </div>
                <form class="payment-form" @submit.prevent>
                    <div class="element-block inline-bloc w33">
                        <label id="lbl-date" for="payment-date">DATE</label>
                        <input id="payment-date" v-model="date" type="date" placeholder="Date de paiement" />
                    </div>
                    <div class="element-block inline-bloc w33">
                        <label id="lbl-viv" for="viv-total">TOTAL VIVs</label>
                        <input id="viv-total" hidden :value="viv" />
                        <div class="values">{{ viv }}</div>
                    </div>
                    <div class="element-block inline-bloc w33">
                        <label id="lbl-amount" for="amount">MONTANT</label>
                        <input id="amount" hidden :value="`${amount} €`" />
                        <div class="values">{{ amount }} €</div>
                    </div>
                    <section>
                        <h3>Actions</h3>
                        <table v-if="unpaidActions.length > 0" aria-label="liste des actions">
                            <colgroup>
                                <col />
                                <col style="width: 22%" />
                                <col style="width: 41%" />
                                <col style="width: 7%" />
                                <col style="width: 24%" />
                            </colgroup>
                            <thead>
                                <tr>
                                    <th class="action-checkbox" />
                                    <th class="right no-left-padding">DATE DE CREATION</th>
                                    <th>ACTION</th>
                                    <th class="right">VIV</th>
                                    <th>
                                        <span class="status-header">STATUT</span>
                                    </th>
                                </tr>
                            </thead>
                            <tbody>
                                <tr v-for="action in unpaidActions" :key="action.id" :data-testid="action.id">
                                    <td class="action-checkbox">
                                        <Checkbox
                                            v-model="action.isSelected"
                                            :aria-label="
                                                'Sélectionner l\'action du ' + action.creationDate.toDateString()
                                            "
                                        />
                                    </td>
                                    <td class="right no-left-padding">{{ action.creationDate.toDateString() }}</td>
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
                        <p v-else class="none" role="alert">Aucune action trouvée !</p>
                    </section>
                    <div class="buttons">
                        <button class="primary-button" :disabled="!hasBalanceToPay" @click="Pay">Valider</button>
                        <router-link class="secondary-button" :to="`/wallets/${id}`">Cancel</router-link>
                    </div>
                </form>
            </section>
        </loading>
    </div>
</template>

<script lang="ts">
import { defineComponent } from "vue";
import { Action, PaymentStatus } from "@/models/action";
import { UsersService } from "@/services/users";
import BalanceCard from "../components/BalanceCard.vue";
import Checkbox from "../components/Checkbox.vue";
import Illustration from "../components/Illustration.vue";
import Loading from "../components/Loading.vue";
import StatusBadge from "../components/StatusBadge.vue";
import { ConsultantStatus, toString } from "../models/consultant";
import { PaymentPost } from "../models/payment";
import { User } from "../models/user";
import { WalletService } from "../services/wallet";

export default defineComponent({
    name: "Payment",
    components: { BalanceCard, Illustration, Loading, StatusBadge, Checkbox },
    props: {
        id: {
            required: true,
            type: String,
        },
    },
    data() {
        return {
            user: { id: "", fullName: "", user: "", email: "" } as User | null,
            balance: 0,
            date: new Date(),
            unpaidActions: [] as (Action & { isSelected: boolean })[],
            coeff: 5,
            loading: false,
            errored: false,
            usersService: new UsersService(),
            walletService: new WalletService(),
        };
    },
    computed: {
        amount(): number {
            return this.coeff * this.viv;
        },
        hasBalanceToPay(): boolean {
            return this.viv > 0;
        },
        viv(): number {
            // TODO manage the initial viv difference
            return this.unpaidActions
                .filter((action) => action.isSelected)
                .reduce((acc, action) => acc + action.payment, 0);
        },
    },
    async mounted() {
        try {
            this.loading = true;
            [this.user, this.balance, this.unpaidActions] = await Promise.all([
                this.usersService.getUser(this.id),
                this.walletService.getUserBalance(this.id),
                this.walletService
                    .getUserPayableActions(this.id)
                    .then((actions) => actions.map((action) => ({ ...action, isSelected: false }))),
            ]);
        } catch {
            this.errored = true;
        } finally {
            this.loading = false;
        }
    },
    methods: {
        formatConsultantStatus(status?: keyof typeof ConsultantStatus) {
            if (status) {
                return toString(ConsultantStatus[status]);
            }
            return "";
        },
        async Pay() {
            try {
                this.loading = true;
                if (this.hasBalanceToPay) {
                    const payment: PaymentPost = {
                        receiverId: this.id,
                        date: this.date,
                        actionIds: this.unpaidActions.filter((action) => action.isSelected).map(({ id }) => id),
                        vivAmount: this.viv,
                    };
                    await this.walletService.saveUserPayment(payment);
                    this.$router.push({ path: `/wallets/${this.id}` });
                }
            } catch {
                this.errored = true;
            } finally {
                this.loading = false;
            }
        },
        isPaymentPaid(action: Action) {
            return action.status === PaymentStatus.Paid;
        },

        formatPaymentStatus(status: PaymentStatus) {
            switch (status) {
                case PaymentStatus.Paid:
                    return "Débloqué";
                case PaymentStatus.Unpaid:
                default:
                    return "Non débloqué";
            }
        },

        getPaymentStatusType(status: PaymentStatus) {
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

<style scoped lang="scss">
@import "../styles/form.scss";
@import "../styles/table.scss";
@import "../styles/buttons.scss";
.inline-bloc {
    display: inline-block;
}
.w50 {
    width: 50%;
}
.w33 {
    width: 33.3%;
}
.payment {
    max-width: 900px;
    margin: auto;
}
h2 {
    text-align: center;
    font-size: $text-2xl;
    color: $gray-700;
    font-weight: 600;
    margin: $m-6 0 $m-3 0;
}
h3 {
    text-align: left;
    font-size: $text-2xl;
    color: $gray-700;
    font-weight: 600;
    margin: $m-6 0 $m-3 0;
}
.payment-form {
    width: 100%;
    margin: $m-5 auto;
    padding: $m-5 $m-6;
}
.payment-form .element-block {
    text-align: center;
}
.buttons {
    display: flex;
    margin-top: $m-5;
    justify-content: flex-end;
    button {
        margin-right: $m-2;
    }
    button:last-child {
        margin-right: 0;
    }
}
.payment .values {
    font-weight: 600;
    color: $gray-700;
}

button:disabled,
button[disabled] {
    cursor: not-allowed;
    opacity: 0.7;
}

.payment-date {
    color: $gray-600;
    font-weight: 400;
    margin-top: $m-2;
    padding-left: $m-3;
}

.status-header {
    padding-left: $m-2;
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

.action-checkbox {
    font-size: $text-xl;
    color: $primary-600;
}

.no-left-padding {
    padding-left: 0px;
}
</style>
