<template>
    <div class="main">
        <loading :loading="loading" :errored="errored">
            <check-roles :roles="myWalletRoles">
                <div class="header">
                    <balance-card
                        :full-name="user.fullName"
                        :expertise="(user.expertise && user.expertise.expertiseName) || ''"
                        :consultant-status="formatConsultantStatus(user.status)"
                        :viv-balance="balance"
                    />
                    <illustration />
                </div>
                <actions-block :user-id="userId" :actions="actions" @actionsSaved="onUpdate" />
                <payment-history-block :user-id="userId" @paymentDeleted="onUpdate" />
            </check-roles>
        </loading>
    </div>
</template>

<script lang="ts">
import { defineComponent } from "vue";
import ActionsBlock from "../components/ActionsBlock.vue";
import BalanceCard from "../components/BalanceCard.vue";
import CheckRoles from "../components/CheckRoles.vue";
import Illustration from "../components/Illustration.vue";
import Loading from "../components/Loading.vue";
import PaymentHistoryBlock from "../components/PaymentHistoryBlock.vue";
import { Action } from "../models/action";
import { ConsultantStatus, toString } from "../models/consultant";
import { adminOnly, myWalletRoles, Role } from "../models/role";
import { LoginService } from "../services/login";
import { UsersService } from "../services/users";
import { WalletService } from "../services/wallet";

export default defineComponent({
    name: "Wallet",
    components: { BalanceCard, Illustration, ActionsBlock, PaymentHistoryBlock, Loading, CheckRoles },
    data() {
        return {
            userRoles: undefined as Role[] | null | undefined,
            actions: [] as Action[],
            loading: true,
            errored: false,
            walletService: new WalletService(),
            loginService: new LoginService(),
            usersService: new UsersService(),
            balance: 0,
            userId: "",
            user: {},
            adminOnly,
            myWalletRoles,
        };
    },
    watch: {
        $route: {
            immediate: true,
            deep: true,
            async handler() {
                try {
                    this.userId = this.$route.params.consultantId
                        ? (this.$route.params.consultantId as string)
                        : (await this.loginService.getUserId()) || "1";

                    [this.balance, this.actions, this.userRoles, this.user] = await Promise.all([
                        this.walletService.getUserBalance(this.userId),
                        this.walletService.getUserActions(this.userId),
                        this.loginService.getRoles(),
                        this.usersService.getUser(this.userId),
                    ]);
                } catch {
                    this.errored = true;
                } finally {
                    this.loading = false;
                }
            },
        },
    },
    methods: {
        async onUpdate() {
            this.balance = await this.walletService.getUserBalance(this.userId);
            console.warn("balance updated", this.balance);
        },
        formatConsultantStatus(status?: keyof typeof ConsultantStatus) {
            if (status) {
                return toString(ConsultantStatus[status]);
            }
            console.warn(`Unknown consultant status ${status}`);
            return "";
        },
        shouldDisplayPayButton() {
            return this.userRoles && this.userRoles.includes(Role.COMPANY_ADMINISTRATOR);
        },
    },
});
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
