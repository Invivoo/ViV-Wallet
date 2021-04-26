<template>
    <div class="history">
        <loading :loading="loading" :errored="errored">
            <check-roles :roles="historyRoles" :with-error-message="true">
                <section>
                    <h2>Historique des actions</h2>
                    <action-history :actions="actions" />
                </section>
            </check-roles>
        </loading>
    </div>
</template>

<script lang="ts">
import { defineComponent } from "vue";
import ActionHistory from "../components/ActionHistory.vue";
import CheckRoles from "../components/CheckRoles.vue";
import Loading from "../components/Loading.vue";
import { ActionForHistory } from "../models/action";
import { historyRoles } from "../models/role";
import { WalletService } from "../services/wallet";

export default defineComponent({
    name: "History",
    components: { ActionHistory, Loading, CheckRoles },
    data() {
        return {
            actions: [] as ActionForHistory[],
            loading: true,
            errored: false,
            walletService: new WalletService(),
            historyRoles: historyRoles,
        };
    },
    async mounted() {
        try {
            this.actions = (await this.walletService.getAllActions()).sort(
                (a, b) => b.creationDate.getTime() - a.creationDate.getTime()
            );
        } catch {
            this.errored = true;
        } finally {
            this.loading = false;
        }
    },
});
</script>

<style scoped lang="scss">
@import "../styles/buttons.scss";
@import "../styles/select.scss";

.history {
    max-width: 900px;
    margin: 0 auto;
    padding: $m-3 $m-5;
}
h2 {
    text-align: center;
    font-size: $text-2xl;
    color: $gray-700;
    font-weight: 600;
    margin: $m-6 0 $m-6 0;
}
</style>
