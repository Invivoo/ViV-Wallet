<template>
    <div class="payment">
        <loading v-bind:loading="loading" v-bind:errored="errored">
            <section>
                <h2>Paiement</h2>
                <div class="header">
                    <balance-card
                        :fullName="user.fullName"
                        expertise="Expertise front-end"
                        consultantStatus="Consultant sénior"
                        v-bind:vivBalance="balance"
                    />
                    <illustration />
                </div>
                <form class="payment-form">
                    <div class="element-block inline-bloc w33">
                        <label id="lbl-date" for="payment-date">DATE</label>
                        <vc-date-picker
                            mode="single"
                            id="payment-date"
                            v-model="date"
                            class="values"
                        />
                    </div>
                    <div class="element-block inline-bloc w33">
                        <label id="lbl-viv">TOTAL VIVs</label>
                        <div class="values">{{ viv }}</div>
                    </div>
                    <div class="element-block inline-bloc w33">
                        <label id="lbl-amount">MONTANT</label>
                        <div class="values">{{ amount }} €</div>
                    </div>
                    <actions-block v-bind:actions="unpaidActions" />
                    <div class="buttons">
                        <button
                            class="primary-button"
                            :disabled="!hasUnpaidActions"
                            v-on:click="AddPayment"
                        >Valider</button>
                        <router-link class="secondary-button" to="/wallet" tag="button">Cancel</router-link>
                    </div>
                </form>
            </section>
        </loading>
    </div>
</template>

<script lang="ts">
import { Component, Prop, Vue } from "vue-property-decorator";
import { User } from "../models/user";
import { UsersService } from "@/services/users";
import { PaymentPost } from "../models/payment";
import { WalletService } from "../services/wallet";
import { Action } from "@/models/action";
import BalanceCard from "../components/BalanceCard.vue";
import Illustration from "../components/Illustration.vue";
import ActionsBlock from "../components/ActionsBlock.vue";
import Loading from "../components/Loading.vue";

const PaymentProps = Vue.extend({
    props: {
        id: String
    }
});

@Component({
    name: "payment",
    components: { BalanceCard, Illustration, ActionsBlock, Loading }
})
export default class Payment extends PaymentProps {
    user: User | null = { id: "", fullName: "", user: "", email: "" };
    balance: number = 0;
    date: Date = new Date();
    unpaidActions: Action[] = [];

    coeff: number = 5;
    viv: number = 0;

    loading = false;
    errored = false;

    usersService = new UsersService();
    walletService = new WalletService();

    get amount(): number {
        return this.coeff * this.viv;
    }

    get hasUnpaidActions(): boolean {
        return this.unpaidActions && this.unpaidActions.length > 0;
    }

    async mounted() {
        try {
            [this.user, this.balance, this.unpaidActions] = await Promise.all([
                this.usersService.getUser(this.id),
                this.walletService.getBalance(this.id),
                this.walletService.getUnpaidActions(this.id)
            ]);

            this.unpaidActions.forEach(action => {
                this.viv += action.payment;
            });
        } catch (ex) {
            this.errored = true;
        } finally {
            this.loading = false;
        }
    }
    async AddPayment() {
        try {
            this.loading = true;
            if (this.hasUnpaidActions) {
                let payment: PaymentPost = {
                    receiver: this.id,
                    date: this.date,
                    actions: this.unpaidActions.map(({ id }) => id)
                };
                await this.walletService.savePayment(payment);
                this.$router.push({ path: "/wallet" });
            }
        } catch (ex) {
            this.errored = true;
        } finally {
            this.loading = false;
        }
    }
}
</script>

<style scoped lang="scss">
@import "../styles/form.scss";
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
    width: 50%;
    margin: auto;
}
h2 {
    text-align: center;
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
</style>
