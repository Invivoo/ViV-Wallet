<template>
    <span :class="type" class="common">
        <slot />
    </span>
</template>

<script lang="ts">
import { defineComponent } from "vue";

export default defineComponent({
    name: "status-badge",
    props: {
        type: {
            required: true,
            type: String,
        },
    },
});
</script>

<style lang="scss" scoped>
.common {
    font-size: $text-lg;
    font-weight: 600;
    padding: $m-1 $m-3;
    line-height: 1.2;
    border-radius: $rounded-lg;
}

.green {
    background: $green-100;
    border: 1px solid $green-200;
    color: $green-800;
}
.red {
    background: $red-100;
    border: 1px solid $red-200;
    color: $red-800;
}
.yellow {
    background: $yellow-100;
    border: 1px solid $yellow-300;
    color: $yellow-800;
}
</style>
