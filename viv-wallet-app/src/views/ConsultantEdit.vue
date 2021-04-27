<template>
    <div class="consultantEdit">
        <h2 v-if="consultantId == 'add'">Ajouter consultant</h2>
        <h2 v-else>Editer consultant</h2>
        <loading :loading="loading" :errored="errored">
            <form class="consultant-form">
                <div class="element-block">
                    <label id="input-fullName-1" for="fullName-1">Nom</label>
                    <div v-if="consultantId == 'add'" :class="`select ${consultant.id ? '' : 'select-error'}`">
                        <select id="fullName-1" v-model="consultant.id" required="true">
                            <option value disabled="true">Choisissez une option</option>
                            <option v-for="user in usersNotAlreadyInExpertise" :key="user.id" :value="user.id">
                                {{ user.fullName }}
                            </option>
                            <span class="select-focus"></span>
                        </select>
                    </div>
                    <input
                        v-else
                        id="fullName-1"
                        v-model="consultant.fullName"
                        type="text"
                        placeholder="Nom"
                        readonly="true"
                    />
                </div>

                <div class="element-block">
                    <label id="input-status-1" for="status-1">Status du consultant</label>
                    <div class="select">
                        <select id="status-1" v-model="consultant.status">
                            <option
                                v-for="option in options"
                                :key="option.value"
                                :value="option.value"
                                :disabled="option.disabled"
                            >
                                {{ option.text }}
                            </option>
                        </select>
                        <span class="select-focus"></span>
                    </div>
                </div>

                <div class="element-block">
                    <label id="input-startDate-1" for="startDate-1">Arrivé</label>
                    <input id="startDate-1" v-model="consultant.startDate" type="date" placeholder="Date d'arrivé" />
                </div>

                <div class="element-block">
                    <label id="input-endDate-1" for="endDate-1">Départ</label>
                    <input id="endDate-1" v-model="consultant.endDate" type="date" placeholder="Date de départ" />
                </div>

                <div class="buttons">
                    <button class="primary-button" :disabled="submitButtonDisabled" @click="confirm">Confirmer</button>
                    <router-link class="secondary-button" :to="`/members/${expertiseName}`">Cancel</router-link>
                </div>
            </form>
        </loading>
    </div>
</template>

<script lang="ts">
import { defineComponent } from "vue";
import { Expertise } from "@/models/expertise";
import Loading from "../components/Loading.vue";
import { Consultant, ConsultantStatus, toString } from "../models/consultant";
import { User } from "../models/user";
import { ConsultantsService } from "../services/consultants";
import { UsersService } from "../services/users";

export default defineComponent({
    name: "Consultant",
    components: { Loading },
    props: {
        expertiseName: {
            required: true,
            type: String,
        },
        consultantId: {
            required: true,
            type: String,
        },
    },
    data() {
        return {
            consultantsService: new ConsultantsService(),
            usersService: new UsersService(),
            consultant: {} as Consultant,
            usersNotAlreadyInExpertise: [] as User[],
            loading: false,
            errored: false,
            submitButtonDisabled: true,
            options: [{ text: "Choisissez une option", value: "", disabled: true }],
        };
    },
    watch: {
        "consultant.id": async function (value: string) {
            const selectedUser = this.usersNotAlreadyInExpertise.find((u) => u.id === value);
            if (selectedUser) {
                this.consultant.user = selectedUser.user;
                this.consultant.fullName = selectedUser.fullName;
                this.submitButtonDisabled = false;
            }
        },
    },
    created() {
        for (const [key, value] of Object.entries(ConsultantStatus)) {
            if (Number.isNaN(Number(key))) {
                // .entries contains either the constant names and indexes
                this.options.push({
                    text: toString(value as ConsultantStatus),
                    value: value as string,
                    disabled: false,
                });
            }
        }
    },
    async mounted() {
        try {
            this.loading = true;
            if (this.consultantId !== "add") {
                this.loading = true;
                this.consultant = await this.consultantsService.getConsultant(this.consultantId);
                this.submitButtonDisabled = false;
            } else {
                this.submitButtonDisabled = true;
                this.loading = true;
                const consultantsInExpertise = await this.consultantsService.getConsultants(this.expertiseName);
                const allUsers = await this.usersService.getUsers();

                this.usersNotAlreadyInExpertise = allUsers.filter(
                    (u) => !consultantsInExpertise.some((u1) => u1.id === u.id)
                );
                this.consultant.startDate = new Date().toLocaleDateString("en-CA"); // the format should be YYYY-MM-DD
                this.consultant.status = ConsultantStatus.CONSULTANT_SENIOR_IN_ONBOARDING;
            }
        } catch {
            this.errored = true;
        } finally {
            this.loading = false;
        }
    },
    methods: {
        async confirm() {
            try {
                this.loading = true;
                if (this.consultant) {
                    this.consultant.expertise = new Expertise(this.expertiseName, "");
                    await this.consultantsService.saveExpertise(this.consultant);
                    this.$router.push({ path: `/members/${this.expertiseName}` });
                }
            } catch {
                this.errored = true;
            } finally {
                this.loading = false;
            }
        },
    },
});
</script>

<style scoped lang="scss">
@import "../styles/form.scss";
@import "../styles/select.scss";
@import "../styles/buttons.scss";

.consultantEdit {
    width: 50%;
    margin: auto;
}

h2 {
    text-align: center;
    font-size: $text-2xl;
    color: $gray-700;
    font-weight: 600;
    margin: $m-6 0 $m-3 0;
}

.consultant-form {
    width: 400px;
    margin: $m-5 auto;
    padding: $m-5 $m-6;
}

.buttons {
    display: flex;
    margin-top: $m-5;
    justify-content: flex-end;
    button {
        margin-right: $m-2;
    }
    button:last-child {
        margin-right: 0;
    }
}

div.select {
    background: $gray-100;
    box-shadow: 0 1px 3px 0 rgba(0, 0, 0, 0.1), 0 1px 2px 0 rgba(0, 0, 0, 0.06);
}

.select-error {
    border: solid 1px $red-400;
}
</style>
