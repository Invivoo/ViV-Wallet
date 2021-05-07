<template>
    <label class="checkbox">
        <span class="checkbox__wrapper">
            <input
                type="checkbox"
                class="native"
                :aria-label="ariaLabel"
                :modelValue="modelValue"
                :checked="modelValue"
                @input="$emit('update:modelValue', $event.target.checked)"
            />
            <div class="checkbox__icon">
                <svg
                    focusable="false"
                    aria-hidden="true"
                    fill="none"
                    stroke="currentColor"
                    viewBox="0 0 24 24"
                    xmlns="http://www.w3.org/2000/svg"
                >
                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="3" d="M5 13l4 4L19 7"></path>
                </svg>
            </div>
        </span>
        <div v-if="label" class="checkbox__label">{{ label }}</div>
    </label>
</template>

<script lang="ts">
import { defineComponent } from "vue";

export default defineComponent({
    name: "Checkbox",
    props: {
        ariaLabel: {
            type: String,
            required: true,
        },
        label: {
            type: String,
            default: "",
        },
        modelValue: {
            type: Boolean,
            default: false,
        },
    },
    emits: ["update:modelValue"],
});
</script>

<style lang="scss" scoped>
.checkbox {
    display: flex;
    align-items: center;
    color: inherit;
    font-size: inherit;
    width: min-content;
    margin: 0;
}

.checkbox__label {
    font-weight: 400;
    padding-left: 0.5rem;
}

.checkbox__wrapper {
    display: inline-block;
    position: relative;
    height: 1em;
    width: 1em;
}
.native {
    display: inline-block;
    height: 100%;
    width: 100%;
    opacity: 0;
    margin: 0;
}

.checkbox__icon {
    position: absolute;
    top: 0;
    left: 0;
    height: 1em;
    width: 1em;
    border: 0.1em solid currentColor;
    border-radius: 0.25em;
    svg {
        display: block;
        transition: transform 0.1s ease-in 25ms;
        transform: scale(0);
        transform-origin: bottom left;
    }
    input:checked + & {
        svg {
            transform: scale(1);
        }
    }
    input:focus + & {
        box-shadow: 0 0 0 0.05em #fff, 0 0 0.15em 0.1em currentColor;
    }
    input:focus + & {
        box-shadow: 0 0 0 0.05em #fff, 0 0 0.15em 0.1em currentColor;
    }
    input:disabled + & {
        color: #959495;
    }
}
</style>
