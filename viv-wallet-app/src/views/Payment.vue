<template>
    <div class="payment">
        <section v-if="errored">
            <p>We're sorry, we're not able to retrieve this information at the moment, please try back later</p>
        </section>
        <section v-else>
            <h2>Payment</h2>
            <div v-if="loading">Loading...</div>
            <div class="header">
                <balance-card
                    fullName="Théophile Montgommery"
                    expertise="Expertise front-end"
                    consultantStatus="Consultant sénior"
                    v-bind:vivBalance="balance"
                />
                <illustration />
            </div>
            <form class="payment-form">
                <div class="element-block">
                    <label id="input-date-1" label-for="lbl-date-1">Date</label>
                    <vc-date-picker :mode="mode" v-model="date" />
                </div>
                <div class="element-block inline-bloc w50">
                    <label id="lbl-amount-1" label-for="p-amount-1">Montant</label>
                    <div id="p-amount-1">{{ viv }}</div>
                </div>
                <div class="element-block inline-bloc w50">
                    <label id="lbl-viv-1" label-for="p-viv-1">VIV</label>
                    <div id="p-viv-1">{{ amount }}</div>
                </div>
                <actions-block v-bind:actions="actions" />
                <div class="buttons">
                    <button class="primary-button" v-on:click="AddPayment">Valider</button>
                    <router-link class="secondary-button" to="/wallet" tag="button">Cancel</router-link>
                </div>
            </form>
        </section>
    </div>
</template>

<script lang="ts">
import { Component, Prop, Vue } from "vue-property-decorator";
import { User } from "../models/user";
import { UsersService } from "@/services/users";
import { Payment, PaymentPost } from "../models/payment";
import { WalletService } from "../services/wallet";
import { Action } from "@/models/action";
import BalanceCard from "../components/BalanceCard.vue";
import Illustration from "../components/Illustration.vue";
import ActionsBlock from "../components/ActionsBlock.vue";
import Loading from "../components/Loading.vue";

const PayementProps = Vue.extend({
    props: {
        id: String
    }
});

@Component({
    name: "payment",
    components: { BalanceCard, Illustration, ActionsBlock, Loading }
})
export default class Payement extends PayementProps {
    user: User | null = { id: "", fullName: "", user: "", email: "" };
    balance: number = 0;
    date: Date = new Date();
    actions: Action[] = [];

    coeff: number = 50;
    viv: number = 0;

    loading = false;
    errored = false;
    mode = "single";

    usersService = new UsersService();
    walletService = new WalletService();

    get amount(): number {
        return this.coeff * this.viv;
    }

    async mounted() {
        try {
            this.user = await this.usersService.getUser(this.id);
            this.balance = await this.walletService.getBalance(this.id);
            this.actions = await this.walletService.getActions(this.id);
        } catch (ex) {
            this.errored = true;
        } finally {
            this.loading = false;
        }
    }
    async AddPayment() {
        try {
            this.loading = true;
            if (this.actions) {
                let payment: PaymentPost = {
                    receiver: this.id,
                    date: new Date(),
                    actions: this.actions.map(({ id }) => id)
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
</style>
