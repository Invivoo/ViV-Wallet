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
import { Vue, Prop, Component } from "vue-property-decorator";

@Component
export default class Loading extends Vue {
    @Prop({ default: true }) loading!: boolean;
    @Prop({ default: false }) errored!: boolean;
}
</script>
