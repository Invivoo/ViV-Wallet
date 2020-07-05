<template>
    <Section>
        <h2>Actions</h2>
        <table>
            <colgroup>
                <col style="width:25%" />
                <col style="width:40%" />
                <col style="width:10%" />
                <col style="width:25%" />
            </colgroup>
            <thead>
                <tr class="table-header">
                    <th class="right">DATE DE CREATION</th>
                    <th>ACTION</th>
                    <th class="right">VIV</th>
                    <th>STATUS</th>
                </tr>
            </thead>
            <tbody>
                <tr v-for="action in actions" :key="action.id">
                    <td class="right">{{ action.creationDate.toDateString() }}</td>
                    <td>
                        <div>
                            <div>{{ action.type }} / {{ action.expertise }}</div>
                            <div>{{ action.comment }}</div>
                        </div>
                    </td>
                    <td class="right">{{ action.payment }}</td>
                    <td>
                        <div>
                            <div>{{ formatValidationStatus(action.status) }}</div>
                            <div>{{ action.validationDate ? action.validationDate.toDateString() : "" }}</div>
                        </div>
                    </td>
                </tr>
            </tbody>
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

<style lang="scss" scoped>
h2 {
    font-size: $text-xl;
    color: $gray-700;
    text-align: left;
    font-weight: 600;
    margin: $m-6 0 $m-3 0;
}

table {
    border-collapse: separate;
    border-spacing: 0;
    table-layout: fixed;
    width: 100%;
    border: none;
    border-radius: $rounded-md;
    box-shadow: 0 4px 6px -1px rgba(0, 0, 0, 0.1), 0 2px 4px -1px rgba(0, 0, 0, 0.06);

    * {
        border: none;
    }
    tr:nth-child(even) {
        background: $gray-200;
    }

    .table-header {
        background: $gray-200;
        color: $gray-600;
        font-size: $text-sm;
        font-weight: 500;
        text-align: left;
    }

    td {
        color: $gray-900;
        text-align: left;
        padding: $m-5 $m-5;
        vertical-align: middle;
    }

    th {
        padding: $m-3 $m-5;
        vertical-align: middle;
    }

    td.right,
    th.right {
        text-align: right;
    }
}
</style>
