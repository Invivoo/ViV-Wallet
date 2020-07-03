<template>
    <Section>
        <h2>Actions</h2>
        <table>
            <tr>
                <td>DATE DE CREATION</td>
                <td>ACTION</td>
                <td>VIV</td>
                <td>STATUS</td>
            </tr>
            <tr v-for="action in actions" :key="action.id">
                <td>{{ action.creationDate.toLocaleDateString() }}</td>
                <td>
                    <div>
                        <div>{{ action.type }} / {{ action.expertise }}</div>
                        <div>{{ action.comment }}</div>
                    </div>
                </td>
                <td>{{ action.payment }}</td>
                <td>
                    <div>
                        <div>{{ formatValidationStatus(action.status) }}</div>
                        <div>{{ action.validationDate ? action.validationDate.toLocaleDateString() : "" }}</div>
                    </div>
                </td>
            </tr>
        </table>
    </Section>
</template>

<script lang="ts">
import { Component, Vue, Prop } from "vue-property-decorator";
import { Action, ValidationStatus } from "../models/action";

@Component
export default class ActionsBlock extends Vue {
    @Prop({ default: [] }) actions!: Action[];

    formatValidationStatus(status: ValidationStatus) {
        return ValidationStatus[status];
    }
}
</script>

<style lang="scss" scoped></style>
