<template>
    <div class="main">
        <loading v-bind:loading="loading" v-bind:errored="errored">
            <div class="header">
                <balance-card
                    fullName="Théophile Montgommery"
                    expertise="Expertise front-end"
                    consultantStatus="Consultant sénior"
                    v-bind:vivBalance="balance"
                />
                <illustration />
            </div>
            <actions-block v-bind:actions="actions" />
            <payment-history-block v-bind:payments="payments" />
        </loading>
    </div>
</template>

<script lang="ts">
import { Component, Prop, Vue } from "vue-property-decorator";
import { Action, ValidationStatus } from "../models/action";
import BalanceCard from "../components/BalanceCard.vue";
import Illustration from "../components/Illustration.vue";
import ActionsBlock from "../components/ActionsBlock.vue";
import PaymentHistoryBlock from "../components/PaymentHistoryBlock.vue";
import { Payment } from "../models/payment";
import { WalletService } from "../services/wallet";
import Loading from "../components/Loading.vue";

@Component({
    name: "wallet",
    components: { BalanceCard, Illustration, ActionsBlock, PaymentHistoryBlock, Loading }
})
export default class wallet extends Vue {
    actions: Action[] = [];

    payments: Payment[] = [
        {
            id: "1",
            date: new Date(),
            viv: 1000,
            amount: 2200
        },
        {
            id: "2",
            date: new Date(),
            viv: 500,
            amount: 1100
        },
        {
            id: "3",
            date: new Date(),
            viv: 100,
            amount: 220
        }
    ];
    loading = true;
    errored = false;
    walletService = new WalletService();

    balance = 0;
    userId = "1";

    async mounted() {
        try {
            this.balance = await this.walletService.getBalance(this.userId);
            this.actions = await this.walletService.getActions(this.userId);
        } catch (ex) {
            this.errored = true;
        } finally {
            this.loading = false;
        }
    }
}
</script>

<style lang="scss" scoped>
.main {
    max-width: 900px;
    margin: 0 auto;
    padding: $m-3 $m-5;
}

.header {
    display: flex;
    justify-content: space-between;
}
</style>
