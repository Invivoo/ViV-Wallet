<template>
    <div class="history">
        <loading v-bind:loading="loading" v-bind:errored="errored">
            <section>
                <h2>Historique des actions</h2>
                <action-history v-bind:actions="actions" />
            </section>
        </loading>
    </div>
</template>

<script lang="ts">
import { Component, Vue } from "vue-property-decorator";
import { ActionForHistory } from "../models/action";
import { WalletService } from "../services/wallet";
import Loading from "../components/Loading.vue";
import ActionHistory from "../components/ActionHistory.vue";

@Component({
    name: "history",
    components: { ActionHistory, Loading }
})
export default class History extends Vue {
    actions: ActionForHistory[] = [];
    loading = true;
    errored = false;

    walletService = new WalletService();

    async mounted() {
        try {
            this.actions = (await this.walletService.getAllActions()).sort(
                (a, b) => b.creationDate.getTime() - a.creationDate.getTime()
            );
        } catch (ex) {
            this.errored = true;
        } finally {
            this.loading = false;
        }
    }
}
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
