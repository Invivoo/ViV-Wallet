<template>
    <div class="wallets">
        <loading v-bind:loading="loading" v-bind:errored="errored">
            <section>
                <h2>Wallets</h2>
                <div class="buttons-container">
                    <div class="selector-container">
                        <label class="expertise-label" for="select-expertise">Expertise :</label>
                        <div class="select">
                            <select
                                id="select-expertise"
                                v-model="selectedExpertiseId"
                                v-on:change="expertiseChanged"
                            >
                                <option disabled value>Choisissez</option>
                                <option
                                    v-for="expertise in expertises"
                                    :key="expertise.id"
                                    v-bind:value="expertise.id"
                                >{{ expertise.expertiseName }}</option>
                            </select>
                            <span class="select-focus"></span>
                        </div>
                    </div>
                </div>
                <consultant-list
                    v-bind:consultants="consultants"
                    v-bind:expertise="selectedExpertiseId"
                >
                    <template v-slot="{ consultantId }">
                        <router-link
                            v-bind:to="`/wallets/${selectedExpertiseId}/${consultantId}`"
                            class="tertiary-button update-button"
                            tag="a"
                        >Voir le wallet</router-link>
                    </template>
                </consultant-list>
            </section>
        </loading>
    </div>
</template>

<script lang="ts">
import { Component, Prop, Vue, Watch } from "vue-property-decorator";
import { Consultant } from "../models/consultant";
import { Expertise } from "../models/expertise";
import { ConsultantsService } from "@/services/consultants";
import { ExpertisesService } from "@/services/expertises";
import Loading from "../components/Loading.vue";

import ConsultantList from "@/components/ConsultantList.vue";

@Component({
    name: "wallets",
    components: { ConsultantList, Loading }
})
export default class Wallets extends Vue {
    consultants: Consultant[] = [];
    expertises: Expertise[] = [];
    selectedExpertiseId = "";
    loading = true;
    errored = false;

    expertisesService = new ExpertisesService();

    async mounted() {
        try {
            this.expertises = await this.expertisesService.getExpertises();
            if (this.$route.params.id) {
                this.selectedExpertiseId = this.$route.params.id;
                await this.updateConsultants();
            } else {
                if (this.expertises.length > 0) {
                    this.$router.push(`/wallets/${this.expertises[0].id}`);
                }
            }
        } catch (ex) {
            this.errored = true;
        } finally {
            this.loading = false;
        }
    }

    @Watch("$route")
    async routeChanged() {
        if (this.$route.params.id) {
            this.selectedExpertiseId = this.$route.params.id;
            await this.updateConsultants();
        } else {
            this.expertiseChanged();
        }
    }

    expertiseChanged() {
        this.$router.push(`/wallets/${this.selectedExpertiseId}`);
    }

    async updateConsultants() {
        const consultantsService = new ConsultantsService(this.selectedExpertiseId);
        this.consultants = await consultantsService.getConsultants();
    }
}
</script>

<style scoped lang="scss">
@import "../styles/buttons.scss";
@import "../styles/select.scss";

.wallets {
    max-width: 900px;
    margin: 0 auto;
    padding: $m-3 $m-5;
}
h2 {
    text-align: center;
    font-size: $text-2xl;
    color: $gray-700;
    font-weight: 600;
    margin: $m-6 0 $m-7 0;
}

.expertise-label {
    font-weight: bold;
    color: $gray-600;
    font-size: $text-base;
    margin-right: $m-2;
    margin-left: $m-5;
    white-space: nowrap;
}

.buttons-container {
    display: flex;
    align-items: baseline;
    width: 100%;
    margin-top: $m-4;
    margin-bottom: $m-3;
}
.selector-container {
    display: flex;
    align-items: baseline;
}
.spacer {
    flex: 1 1 auto;
}
</style>

