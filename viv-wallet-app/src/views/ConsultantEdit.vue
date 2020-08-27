<template>
    <div class="consultantEdit">
        <h2 v-if="consultantId == 'add'">Ajouter consultant</h2>
        <h2 v-else>Editer consultant</h2>
        <loading v-bind:loading="loading" v-bind:errored="errored">
            <form class="consultant-form">
                <div class="element-block">
                    <label id="input-fullName-1" for="fullName-1">Nom</label>
                    <input
                        v-if="consultantId != 'add'"
                        id="fullName-1"
                        type="text"
                        v-model="consultant.fullName"
                        placeholder="Nom"
                        readonly="true"
                    />
                    <select v-else id="fullName-1" v-model="consultant.id">
                        <option v-for="user in usersNotAlreadyInExpertise" v-bind:key="user.id" v-bind:value="user.id">
                            {{ user.fullName }}
                        </option>
                    </select>
                </div>

                <div class="element-block">
                    <label id="input-email-1" label-for="email-1">Email</label>
                    <input id="email-1" type="text" v-model="consultant.email" placeholder="Email" readonly="true" />
                </div>

                <div class="element-block">
                    <label id="input-status-1" label-for="status-1">Status du consultant</label>
                    <select id="status-1" v-model="consultant.status">
                        <option
                            v-bind:key="option.value"
                            v-for="option in options"
                            v-bind:value="option.value"
                            v-bind:disabled="option.disabled"
                        >
                            {{ option.text }}
                        </option>
                    </select>
                </div>

                <div class="element-block">
                    <label id="input-startDate-1" label-for="startDate-1">Arrivé</label>
                    <input id="startDate-1" type="date" v-model="consultant.startDate" placeholder="Date d'arrivé" />
                </div>

                <div class="element-block">
                    <label id="input-endDate-1" label-for="endDate-1">Départ</label>
                    <input id="endDate-1" type="date" v-model="consultant.endDate" placeholder="Date de départ" />
                </div>

                <div class="buttons">
                    <button class="primary-button" v-on:click="confirm">Confirmer</button>
                    <router-link class="secondary-button" v-bind:to="`/members/${expertiseName}`" tag="button"
                        >Cancel</router-link
                    >
                </div>
            </form>
        </loading>
    </div>
</template>

<script lang="ts">
import { Component, Prop, Vue, Watch } from "vue-property-decorator";
import { Consultant, ConsultantStatus, toString } from "../models/consultant";
import { ConsultantsService } from "../services/consultants";
import { UsersService } from "../services/users";
import { User } from "../models/user";
import Loading from "../components/Loading.vue";

const ConsultantEditProps = Vue.extend({
    props: {
        expertiseName: String,
        consultantId: String
    }
});

@Component({
    name: "consultant",
    components: { Loading }
})
export default class ConsultantEdit extends ConsultantEditProps {
    consultantsService;
    usersService;
    consultant: Consultant = {};
    usersNotAlreadyInExpertise: User[] = [];
    loading = false;
    errored = false;
    options: { text: string; value: string; disabled: boolean }[] = [
        { text: "Choisissez une option", value: "", disabled: true }
    ];

    constructor() {
        super();
        this.consultantsService = new ConsultantsService(this.expertiseName);
        this.usersService = new UsersService();

        for (const [key, value] of Object.entries(ConsultantStatus)) {
            if (isNaN(Number(key))) {
                // .entries contains either the constant names and indexes
                this.options.push({
                    text: toString(value as ConsultantStatus),
                    value: value as string,
                    disabled: false
                });
            }
        }
    }

    async mounted() {
        try {
            this.loading = true;
            if (this.consultantId !== "add") {
                this.loading = true;
                this.consultant = await this.consultantsService.getConsultant(this.consultantId);
            } else {
                this.loading = true;
                const consultantsInExpertise = await this.consultantsService.getConsultants();
                const allUsers = await this.usersService.getUsers();

                this.usersNotAlreadyInExpertise = allUsers.filter(
                    u => !consultantsInExpertise.find(u1 => u1.id === u.id)
                );
                this.consultant.startDate = new Date().toLocaleDateString("en-CA"); // the format should be YYYY-MM-DD
                this.consultant.status = ConsultantStatus.CONSULTANT_SENIOR_IN_ONBOARDING;
            }
        } catch (ex) {
            this.errored = true;
        } finally {
            this.loading = false;
        }
    }

    async confirm() {
        try {
            this.loading = true;
            if (this.consultant) {
                await this.consultantsService.saveConsultant(this.consultant);
                this.$router.push({ path: `/members/${this.expertiseName}` });
            }
        } catch (ex) {
            this.errored = true;
        } finally {
            this.loading = false;
        }
    }

    @Watch("consultant.id")
    async onSelectedUserChanged(value: string) {
        const selectedUser = this.usersNotAlreadyInExpertise.find(u => u.id === value);
        if (selectedUser) {
            this.consultant.user = selectedUser.user;
            this.consultant.fullName = selectedUser.fullName;
            this.consultant.email = selectedUser.email;
        }
    }
}
</script>

<style scoped lang="scss">
@import "../styles/form.scss";
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
</style>
