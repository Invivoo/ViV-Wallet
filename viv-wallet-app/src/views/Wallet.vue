<template>
    <div class="main">
        <loading v-bind:loading="loading" v-bind:errored="errored">
            <div class="header">
                <balance-card
                    v-bind:fullName="user.fullName"
                    expertise="Expertise front-end"
                    consultantStatus="Consultant sénior"
                    v-bind:vivBalance="balance"
                />
                <illustration />
            </div>
            <actions-block v-bind:actions="actions" v-bind:user-roles="userRoles" />
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
import { UsersService } from "../services/users";

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
    usersService = new UsersService();

    balance = 0;
    userId = "";
    user = {};

    async mounted() {
        try {
            if (this.$route.params.consultantId) {
                this.userId = this.$route.params.consultantId;
            } else {
                this.userId = await this.loginService.getUserId();
            }

            [this.balance, this.actions, this.payments, this.userRoles, this.user] = await Promise.all([
                this.walletService.getBalance(this.userId),
                this.walletService.getActions(this.userId),
                this.walletService.getPayments(this.userId),
                this.loginService.getRoles(),
                this.usersService.getUser(this.userId)
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
