<template>
    <div class="consultantEdit">
        <section v-if="errored">
            <p>We're sorry, we're not able to retrieve this information at the moment, please try back later</p>
        </section>
        <section v-else>
            <h2>Mettre à jour consultant</h2>
            <div v-if="loading">Loading...</div>
            <form v-else class="consultant-form">
                <div class="element-block">
                    <label id="input-fullname-1" for="fullname-1">Nom</label>
                    <input id="fullname-1" type="text" v-model="consultant.fullname" placeholder="Nom" />
                </div>

                <div class="element-block">
                    <label id="input-consultant-1" label-for="consultant-1">Identifiant</label>
                    <input id="consultant-1" type="text" v-model="consultant.consultant" placeholder="Identifiant" />
                </div>

                <div class="element-block">
                    <label id="input-email-1" label-for="email-1">Email</label>
                    <input id="email-1" type="text" v-model="consultant.email" placeholder="Email" />
                </div>

                <div class="buttons">
                    <button class="primary-button" v-on:click="confirm">Confirmer</button>
                    <router-link class="secondary-button" to="/consultants" tag="button">Cancel</router-link>
                </div>
            </form>
        </section>
    </div>
</template>

<script lang="ts">
import { Component, Prop, Vue } from "vue-property-decorator";
import { Consultant } from "../models/consultant";
import { ConsultantsService } from "../services/consultants";

const ConsultantEditProps = Vue.extend({
    props: {
        expertiseName: String,
        consultantId: String
    }
});

@Component({
    name: "consultant"
})
export default class ConsultantEdit extends ConsultantEditProps {
    consultantsService;
    consultant: Consultant | undefined;
    loading = false;
    errored = false;

    constructor() {
        super();
        this.consultantsService = new ConsultantsService(this.expertiseName);
    }
    
    async mounted() {
        try {
            this.loading = true;
            this.consultant = await this.consultantsService.getConsultant(this.consultantId);
        } catch (ex) {
            this.errored = true;
        } finally {
            this.loading = false;
        }
    }
    async confirm() {
        // try {
        //     this.loading = true;
        //     if (this.user) {
        //         await this.usersService.saveUser(this.user);
        //         this.$router.push({ path: "/users" });
        //     }
        // } catch (ex) {
        //     this.errored = true;
        // } finally {
        //     this.loading = false;
        // }
    }
}
</script>

<style scoped lang="scss">
@import "../styles/form.scss";
@import "../styles/buttons.scss";

.userEdit {
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

.user-form {
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
