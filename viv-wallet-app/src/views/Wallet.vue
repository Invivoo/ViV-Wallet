<template>
    <div class="main">
        <loading v-bind:loading="loading" v-bind:errored="errored">
            <div class="header">
                <balance-card
                    v-bind:fullName="userFullName"
                    expertise="Expertise front-end"
                    consultantStatus="Consultant sénior"
                    v-bind:vivBalance="balance"
                />
                <illustration />
            </div>
            <div>
                <router-link
                    class="primary-button payment-btn"
                    v-bind:to="{ path: '/payment/2'}"
                    v-if="shouldDisplayPayButton()"
                    tag="button"
                >Payer maintenant</router-link>
            </div>
            <actions-block v-bind:actions="actions" />
            <payment-history-block v-bind:payments="payments" />
        </loading>
    </div>
</template>

<script lang="ts">
import { Component, Prop, Vue } from "vue-property-decorator";
import { Action, PaymentStatus } from "../models/action";
import BalanceCard from "../components/BalanceCard.vue";
import Illustration from "../components/Illustration.vue";
import ActionsBlock from "../components/ActionsBlock.vue";
import PaymentHistoryBlock from "../components/PaymentHistoryBlock.vue";
import { Payment } from "../models/payment";
import { WalletService } from "../services/wallet";
import Loading from "../components/Loading.vue";
import { Role } from "../models/role";
import { LoginService } from "../services/login";

@Component({
    name: "wallet",
    components: { BalanceCard, Illustration, ActionsBlock, PaymentHistoryBlock, Loading }
})
export default class wallet extends Vue {
    userRoles: Role[] | null | undefined = null;
    actions: Action[] = [];
    payments: Payment[] = [];
    loading = true;
    errored = false;
    walletService = new WalletService();
    loginService = new LoginService();

    balance = 0;
    userId = "1";
    userFullName = "Théophile Montgommery";

    shouldDisplayPayButton() {
        return this.userRoles && this.userRoles.indexOf(Role.COMPANY_ADMINISTRATOR) !== -1;
    }

    async mounted() {
        try {
            [
                this.balance,
                this.actions,
                this.payments,
                this.userRoles,
                this.userId,
                this.userFullName
            ] = await Promise.all([
                this.walletService.getBalance(this.userId),
                this.walletService.getActions(this.userId),
                this.walletService.getPayments(this.userId),
                this.loginService.getRoles(),
                this.loginService.getUserId(),
                this.loginService.getUserFullName()
            ]);
        } catch (ex) {
            this.errored = true;
        } finally {
            this.loading = false;
        }
    }
}
</script>

<style lang="scss" scoped>
@import "../styles/buttons.scss";
.main {
    max-width: 900px;
    margin: 0 auto;
    padding: $m-3 $m-5;
}

.header {
    display: flex;
    justify-content: space-between;
}

.payment-btn {
    float: right;
    margin-top: $m-6;
}
</style>
