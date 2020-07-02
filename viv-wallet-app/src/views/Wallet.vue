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
    </div>
</template>

<script lang="ts">
import { Component, Prop, Vue } from "vue-property-decorator";
import { Illustration, BalanceCard } from "../components";

@Component({
    name: "wallet",
    components: { BalanceCard, Illustration }
})
export default class wallet extends Vue {}
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
