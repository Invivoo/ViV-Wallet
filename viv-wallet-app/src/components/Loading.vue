<template>
    <fragment>
        <div v-if="errored">
            <p>Une erreur s'est produite lors de la récupération des données. Veuillez réessayer plus tard.</p>
        </div>
        <div v-else>
            <div v-if="loading">Chargement...</div>
            <slot v-else />
        </div>
    </fragment>
</template>

<script lang="ts">
import Vue from "vue";

export default Vue.extend({
    name: "loading",
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
