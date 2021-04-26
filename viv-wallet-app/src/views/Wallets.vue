<template>
    <div class="wallets">
        <loading :loading="loading" :errored="errored">
            <check-roles :roles="walletsRoles" :with-error-message="true">
                <section>
                    <h2>Wallets</h2>
                    <div class="buttons-container">
                        <filter-input v-model="filterValue" />
                    </div>
                    <consultant-list :consultants="filteredConsultants">
                        <template #default="{ consultantId }">
                            <router-link :to="`/wallets/${consultantId}`" class="tertiary-button update-button" tag="a"
                                >Voir le wallet</router-link
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
import FilterInput from "../components/FilterInput.vue";
import Loading from "../components/Loading.vue";
import { Consultant } from "../models/consultant";
import { Role, walletsRoles } from "../models/role";
import { LoginService } from "../services/login";

export default defineComponent({
    name: "Wallets",
    components: { ConsultantList, Loading, CheckRoles, FilterInput },
    data() {
        return {
            consultants: [] as Consultant[],
            filteredConsultants: [] as Consultant[],
            loading: true,
            errored: false,
            expertisesService: new ExpertisesService(),
            consultantsService: new ConsultantsService(),
            loginService: new LoginService(),
            walletsRoles,
            filterValue: "",
        };
    },
    watch: {
        filterValue() {
            this.filterChanged();
        },
    },
    async mounted() {
        try {
            const [expertises, allConsultants, userRoles] = await Promise.all([
                this.expertisesService.getExpertises(),
                this.consultantsService.getConsultants(),
                this.loginService.getRoles(),
            ]);
            this.consultants =
                userRoles.includes(Role.COMPANY_ADMINISTRATOR) || userRoles.includes(Role.SENIOR_MANAGER)
                    ? allConsultants
                    : allConsultants.filter(
                          (consultant) =>
                              consultant.expertise &&
                              expertises
                                  .map((expertise) => expertise.expertiseName)
                                  .includes(consultant.expertise.expertiseName)
                      );
            this.filterChanged();
        } catch {
            this.errored = true;
        } finally {
            this.loading = false;
        }
    },
    methods: {
        filterChanged() {
            this.filteredConsultants = this.consultants.filter((consultant) =>
                consultant.fullName?.toLowerCase().includes(this.filterValue.toLowerCase())
            );
        },
    },
});
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
