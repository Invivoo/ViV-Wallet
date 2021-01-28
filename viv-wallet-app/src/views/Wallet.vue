<template>
    <div class="main">
        <loading v-bind:loading="loading" v-bind:errored="errored">
            <check-roles v-bind:roles="myWalletRoles">
                <div class="header">
                    <balance-card
                        v-bind:fullName="user.fullName"
                        v-bind:expertise="(user.expertise && user.expertise.expertiseName) || ''"
                        v-bind:consultantStatus="formatConsultantStatus(user.status)"
                        v-bind:vivBalance="balance"
                    />
                    <illustration />
                </div>
                <check-roles v-bind:roles="adminOnly">
                    <div>
                        <router-link
                            class="primary-button payment-btn"
                            v-bind:to="{ path: `/payment/${user.id}` }"
                            v-if="shouldDisplayPayButton()"
                            tag="button"
                            >Payer maintenant</router-link
                        >
                    </div>
                </check-roles>
                <actions-block v-bind:actions="actions" />
                <payment-history-block v-bind:payments="payments" />
            </check-roles>
        </loading>
    </div>
</template>

<script lang="ts">
import { Component, Vue, Watch } from "vue-property-decorator";
import { Action } from "../models/action";
import BalanceCard from "../components/BalanceCard.vue";
import Illustration from "../components/Illustration.vue";
import ActionsBlock from "../components/ActionsBlock.vue";
import PaymentHistoryBlock from "../components/PaymentHistoryBlock.vue";
import { Payment } from "../models/payment";
import { WalletService } from "../services/wallet";
import Loading from "../components/Loading.vue";
import { adminOnly, myWalletRoles, Role } from "../models/role";
import { LoginService } from "../services/login";
import { UsersService } from "../services/users";
import { ConsultantStatus, toString } from "../models/consultant";
import CheckRoles from "../components/CheckRoles.vue";

@Component({
    name: "wallet",
    components: { BalanceCard, Illustration, ActionsBlock, PaymentHistoryBlock, Loading, CheckRoles },
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
    adminOnly = adminOnly;
    myWalletRoles = myWalletRoles;

    formatConsultantStatus(status?: string) {
        if (status) {
            return toString(ConsultantStatus[status]);
        }
        return "";
    }

    shouldDisplayPayButton() {
        return this.userRoles && this.userRoles.indexOf(Role.COMPANY_ADMINISTRATOR) !== -1;
    }

    @Watch("$route", { immediate: true, deep: true })
    async onUrlChange() {
        try {
            if (this.$route.params.consultantId) {
                this.userId = this.$route.params.consultantId;
            } else {
                this.userId = (await this.loginService.getUserId()) || "1"; // TODO to avoid crash (no authorization service yet)
            }

            [this.balance, this.actions, this.payments, this.userRoles, this.user] = await Promise.all([
                this.walletService.getUserBalance(this.userId),
                this.walletService.getUserActions(this.userId),
                this.walletService.getUserPayments(this.userId),
                this.loginService.getRoles(),
                this.usersService.getUser(this.userId),
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
