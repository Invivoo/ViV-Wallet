<template>
    <div v-if="errored">
        <p role="alert">Une erreur s'est produite lors de la récupération des données. Veuillez réessayer plus tard.</p>
    </div>
    <div v-else>
        <p v-if="loading" role="alert">Chargement...</p>
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
