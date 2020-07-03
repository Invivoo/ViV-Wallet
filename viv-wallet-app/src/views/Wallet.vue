<template>
    <div class="main">
        <div class="header">
            <balance-card
                fullName="Théophile Montgommery"
                expertise="Expertise front-end"
                consultantStatus="Consultant sénior"
                vivBalance="220"
            />
            <illustration />
        </div>
        <actions-block v-bind:actions="actions" />
    </div>
</template>

<script lang="ts">
import { Component, Prop, Vue } from "vue-property-decorator";
import { Illustration, BalanceCard, ActionsBlock } from "../components";
import { Action, ValidationStatus } from "../models/action";

@Component({
    name: "wallet",
    components: { BalanceCard, Illustration, ActionsBlock }
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
        }
    ];
}
</script>

<style lang="scss" scoped>
.main {
    max-width: 900px;
    margin: 0 auto;
}

.header {
    display: flex;
    justify-content: space-between;
}
</style>
