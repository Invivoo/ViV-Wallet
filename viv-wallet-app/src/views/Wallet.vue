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
import { BalanceService } from "../services/balance";
import Loading from "../components/Loading.vue";

@Component({
    name: "wallet",
    components: { BalanceCard, Illustration, ActionsBlock, PaymentHistoryBlock, Loading }
})
export default class wallet extends Vue {
    actions: Action[] = [
        {
            id: "0",
            type: "Interview",
            comment: "This is a comment",
            creationDate: new Date(),
            payment: 20,
            status: ValidationStatus.Done,
            validationDate: new Date(),
            expertise: "Front-End"
        },
        {
            id: "1",
            type: "Article",
            comment: "This is a comment",
            creationDate: new Date(),
            payment: 50,
            status: ValidationStatus.Rejected,
            validationDate: new Date(),
            expertise: "Front-End"
        },
        {
            id: "3",
            type: "Article",
            comment: "This is an other great comment",
            creationDate: new Date(),
            payment: 50,
            status: ValidationStatus.Rejected,
            validationDate: new Date(),
            expertise: "Front-End"
        }
    ];

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
    balanceService = new BalanceService();
    balance = 0;
    userId = "1";

    async mounted() {
        try {
            this.balance = await this.balanceService.getBalance(this.userId);
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
