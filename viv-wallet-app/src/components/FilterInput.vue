<template>
    <div class="filter">
        <label class="sr-only" for="filter">Filtrer par utilisateur</label>
        <div class="filter-input">
            <div class="filter-icon">
                <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 100 100">
                    <path
                        d="M93.5 26.375a5 5 0 01-5 5h-77a5 5 0 01-5-5v-4.667a5 5 0 015-5h77a5 5 0 015 5v4.667zM78.5 52.333a5 5 0 01-5 5h-47a5 5 0 01-5-5v-4.666a5 5 0 015-5h47a5 5 0 015 5v4.666zM63.5 78.292a5 5 0 01-5 5h-17a5 5 0 01-5-5v-4.667a5 5 0 015-5h17a5 5 0 015 5v4.667z"
                    />
                </svg>
            </div>
            <input
                id="filter"
                type="text"
                placeholder="Filtrer par utilisateur"
                :modelValue="filterValue"
                @input="$emit('update:modelValue', $event.target.value)"
            />
        </div>
    </div>
</template>

<script lang="ts">
import { defineComponent } from "vue";

export default defineComponent({
    name: "FilterInput",
    props: {
        filterValue: {
            default: "",
            type: String,
        },
    },
    emits: ["update:modelValue"],
});
</script>

<style lang="scss" scoped>
.filter {
    display: flex;
    align-items: baseline;
    margin-bottom: $m-3;
    input {
        width: 40ch;
        padding-left: 2.4em;
    }
    .filter-input {
        position: relative;
        .filter-icon {
            display: flex;
            flex-direction: column;
            justify-content: center;
            position: absolute;
            margin: auto;
            left: $m-2;
            top: 0;
            bottom: 0;
            width: 1.4em;
            svg {
                fill: $gray-600;
            }
        }
    }
}
.sr-only {
    position: absolute;
    width: 1px;
    height: 1px;
    padding: 0;
    margin: -1px;
    overflow: hidden;
    clip: rect(0, 0, 0, 0);
    white-space: nowrap;
    border-width: 0;
}
</style>
