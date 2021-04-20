<template>
    <div class="consultants">
        <loading v-bind:loading="loading" v-bind:errored="errored">
            <check-roles v-bind:roles="expertisesRoles" v-bind:withErrorMessage="true">
                <section>
                    <h2>Consultants</h2>
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
                                    >
                                        {{ expertise.expertiseName }}
                                    </option>
                                </select>
                                <span class="select-focus"></span>
                            </div>
                        </div>
                        <div class="spacer" />
                        <router-link
                            v-if="selectedExpertiseId"
                            class="primary-button"
                            v-bind:to="`/members/${selectedExpertiseId}/add`"
                            >Ajouter</router-link
                        >
                    </div>
                    <consultant-list v-bind:consultants="consultants">
                        <template v-slot="{ consultantId }">
                            <router-link
                                v-bind:to="`/members/${selectedExpertiseId}/${consultantId}`"
                                class="tertiary-button update-button"
                                >Mettre à jour</router-link
                            >
                        </template>
                    </consultant-list>
                </section>
            </check-roles>
        </loading>
    </div>
</template>

<script lang="ts">
import { defineComponent } from "vue";
import ConsultantList from "@/components/ConsultantList.vue";
import { ConsultantsService } from "@/services/consultants";
import { ExpertisesService } from "@/services/expertises";
import CheckRoles from "../components/CheckRoles.vue";
import Loading from "../components/Loading.vue";
import { Consultant } from "../models/consultant";
import { Expertise } from "../models/expertise";
import { expertisesRoles } from "../models/role";

export default defineComponent({
    name: "members",
    components: { ConsultantList, Loading, CheckRoles },
    data() {
        return {
            consultants: [] as Consultant[],
            expertises: [] as Expertise[],
            selectedExpertiseId: "",
            loading: true,
            errored: false,
            expertisesRoles: expertisesRoles,
            expertisesService: new ExpertisesService(),
        };
    },
    async mounted() {
        try {
            this.expertises = await this.expertisesService.getExpertises();
            if (this.$route.params.id) {
                this.selectedExpertiseId = this.$route.params.id as string;
                await this.updateConsultants();
            } else {
                if (this.expertises.length > 0) {
                    this.$router.push(`/members/${this.expertises[0].id}`);
                }
            }
        } catch {
            this.errored = true;
        } finally {
            this.loading = false;
        }
    },
    methods: {
        expertiseChanged: function () {
            this.$router.push(`/members/${this.selectedExpertiseId}`);
        },
        updateConsultants: async function () {
            const consultantsService = new ConsultantsService();
            this.consultants = await consultantsService.getConsultants(this.selectedExpertiseId);
        },
    },
    watch: {
        "$route.params": async function () {
            if (this.$route.params.id) {
                this.selectedExpertiseId = this.$route.params.id as string;
                await this.updateConsultants();
            }
        },
    },
});
</script>

<style scoped lang="scss">
@import "../styles/buttons.scss";
@import "../styles/select.scss";

.consultants {
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
