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
import { Component, Prop, Vue, Watch } from "vue-property-decorator";
import { ActionForHistory, PaymentStatus } from "../models/action";
import { Consultant } from "../models/consultant";
import { ConsultantsService } from "@/services/consultants";
import { WalletService } from "../services/wallet";
import Loading from "../components/Loading.vue";
import ActionHistory from "../components/ActionHistory.vue";

@Component({
    name: "history",
    components: { ActionHistory, Loading },
})
export default class History extends Vue {
    // TODO get the expertise of the current user
    expertise = "csharp";
    actions: ActionForHistory[] = [];
    loading = true;
    errored = false;

    walletService = new WalletService();

    async mounted() {
        try {
            const consultantsService = new ConsultantsService(this.expertise);
            const consultants = await consultantsService.getConsultants();
            this.actions = (
                await Promise.all(
                    consultants.map(async (consultant) => {
                        return (await this.walletService.getActions(consultant.id!)).map((action) => ({
                            ...action,
                            userFullName: consultant.fullName,
                        }));
                    })
                )
            )
                .flat()
                .sort((a, b) => b.creationDate.getTime() - a.creationDate.getTime());
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
