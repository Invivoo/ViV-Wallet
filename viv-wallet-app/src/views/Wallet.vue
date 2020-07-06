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
import { Action, ValidationStatus } from "../models/action";
import BalanceCard from "../components/BalanceCard.vue";
import Illustration from "../components/Illustration.vue";
import ActionsBlock from "../components/ActionsBlock.vue";

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
