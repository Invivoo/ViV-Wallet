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
                    <div class="payment-wrapper">
                        <div class="element-block w33">
                            <label id="lbl-date" for="payment-date">DATE</label>
                            <input id="payment-date" v-model="date" type="date" placeholder="Date de paiement" />
                        </div>
                        <div class="element-block w33">
                            <label id="lbl-viv" for="viv-total">TOTAL VIVs</label>
                            <input id="viv-total" v-model="viv" placeholder="VIVs à payer" />
                        </div>
                        <div class="element-block w33">
                            <label id="lbl-amount" for="amount">MONTANT</label>
                            <input id="amount" hidden :value="`${amount} €`" />
                            <div class="values">{{ amount }} €</div>
                        </div>
                    </div>
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
import { UsersService } from "@/services/users";
import paymentHelpers from "@/utils/paymentHelpers";
import BalanceCard from "../components/BalanceCard.vue";
import Illustration from "../components/Illustration.vue";
import Loading from "../components/Loading.vue";
import { ConsultantStatus, toString } from "../models/consultant";
import { PaymentPost } from "../models/payment";
import { User } from "../models/user";
import { WalletService } from "../services/wallet";

export default defineComponent({
    name: "Payment",
    components: { BalanceCard, Illustration, Loading },
    props: {
        id: {
            required: true,
            type: String,
        },
    },
    data() {
        return {
            user: { id: "", fullName: "", user: "", email: "" } as User | null,
            viv: "0",
            balance: 0,
            date: new Date(),
            coeff: 5,
            loading: false,
            errored: false,
            usersService: new UsersService(),
            walletService: new WalletService(),
        };
    },
    computed: {
        amount(): number {
            return this.coeff * +this.viv;
        },
        hasBalanceToPay(): boolean {
            return +this.viv > 0;
        },
    },
    async mounted() {
        try {
            this.loading = true;
            [this.user, this.balance] = await Promise.all([
                this.usersService.getUser(this.id),
                this.walletService.getUserBalance(this.id),
            ]);
            this.viv = `${this.balance}`;
        } catch {
            this.errored = true;
        } finally {
            this.loading = false;
        }
    },
    methods: {
        ...paymentHelpers,
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
                        vivAmount: +this.viv,
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
    },
});
</script>

<style scoped lang="scss">
.w50 {
    width: 50%;
}
.w33 {
    flex-basis: 0;
    flex-grow: 1;
}
.payment-wrapper {
    display: flex;
    gap: $m-4;
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

.viv-details {
    font-weight: 400;
    color: $gray-600;
    vertical-align: baseline;
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

.checkbox-header {
    font-size: $text-xl;
    color: $gray-600;
}

.no-left-padding {
    padding-left: 0px;
}
</style>
