<template>
    <div class="card-container">
        <div class="heading">
            <div>
                <h2 class="user">{{ fullName }}</h2>
            </div>
            <div class="balance">{{ vivBalance }} VIV</div>
        </div>
        <div class="user-details">
            <div>{{ expertise }}</div>
            <div>{{ consultantStatus }}</div>
        </div>
    </div>
</template>

<script lang="ts">
import { Component, Prop, Vue } from "vue-property-decorator";

@Component({
    name: "balanceCard",
    components: {}
})
export default class BalanceCard extends Vue {
    @Prop() fullName!: string;
    @Prop() expertise!: string;
    @Prop() consultantStatus!: string;
    @Prop() vivBalance!: string;
}
</script>

<style lang="scss" scoped>
.card-container {
    background: $white;
    color: $primary-900;
    border-radius: $rounded-md;
    box-shadow: 0 4px 6px -1px rgba(0, 0, 0, 0.1), 0 2px 4px -1px rgba(0, 0, 0, 0.06);
    min-width: 500px;
    padding: $m-6;
    text-align: left;
}
.heading {
    display: flex;
    flex-direction: row;
    justify-content: space-between;
    align-items: baseline;
}

.balance {
    background: $green-100;
    border: 1px solid $green-200;
    border-radius: $rounded-lg;
    color: $green-800;
    font-weight: 600;
    font-size: $text-3xl;
    padding: $m-2 $m-3;
}

.user {
    font-weight: 600;
    font-size: $text-2xl;
}

.user-details {
    margin-top: -10px;
    color: $gray-700;
    font-size: $text-base;
    font-weight: 400;
}
</style>
