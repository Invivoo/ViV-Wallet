<template>
    <div class="consultants">
        <loading v-bind:loading="loading" v-bind:errored="errored">
            <ConsultantList v-bind:consultants="consultants" />
        </loading>
    </div>
</template>

<script lang="ts">
import { Component, Prop, Vue } from "vue-property-decorator";
import { Consultant } from "../models/consultant";
import { ConsultantsService } from "@/services/consultants";
import Loading from "../components/Loading.vue";

import ConsultantList from "@/components/ConsultantList.vue";

@Component({
    name: "members",
    components: { ConsultantList, Loading }
})
export default class Consultants extends Vue {
    consultants: Consultant[] = [];
    loading = true;
    errored = false;

    // TODO: add dropdown to select expertise
    consultantsService = new ConsultantsService("csharp");

    async mounted() {
        try {
            this.consultants = await this.consultantsService.getConsultants();
        } catch (ex) {
            this.errored = true;
        } finally {
            this.loading = false;
        }
    }
}
</script>

<style scoped lang="scss">
.users {
    max-width: 900px;
    margin: 0 auto;
    padding: $m-3 $m-5;
}
</style>
