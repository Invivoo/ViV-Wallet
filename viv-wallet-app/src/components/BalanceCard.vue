<template>
    <div class="card-container">
        <div class="heading">
            <div>
                <h2 class="user">{{fullName}}</h2>
            </div>
            <div class="balance">{{vivBalance}} VIV</div>
        </div>
        <div class="user-details">
            <div>{{expertise}}</div>
            <div>{{consultantStatus}}</div>
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

<style scoped>
.card-container {
    background: #ffffff;
    color: #3c366b;
    border-radius: 12px;
    box-shadow: 0 4px 6px -1px rgba(0, 0, 0, 0.1), 0 2px 4px -1px rgba(0, 0, 0, 0.06);
    min-width: 500px;
    padding: 24px;
    text-align: left;
}
.heading {
    display: flex;
    flex-direction: row;
    justify-content: space-between;
    align-items: baseline;
}

.balance {
    background: #f0fff4;
    border: 1px solid #c6f6d5;
    border-radius: 16px;
    color: #276749;
    font-weight: 600;
    font-size: 28px;
    padding: 8px 12px;
}

.user {
    font-weight: 600;
    font-size: 24px;
}

.user-details {
    margin-top: -10px;
    color: #4a5568;
    font-size: 16px;
    font-weight: 400;
}
</style>