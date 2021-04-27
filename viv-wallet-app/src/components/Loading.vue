<template>
    <div v-if="errored">
        <p>Une erreur s'est produite lors de la récupération des données. Veuillez réessayer plus tard.</p>
    </div>
    <div v-else>
        <div v-if="loading">Chargement...</div>
        <slot v-else />
    </div>
</template>

<script lang="ts">
import { defineComponent } from "vue";

export default defineComponent({
    name: "Loading",
    props: {
        loading: {
            default: true,
            type: Boolean,
        },
        errored: {
            default: false,
            type: Boolean,
        },
    },
});
</script>
