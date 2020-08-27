<template>
<div class="consultantEdit">
  <h2>Editer consultant</h2>
  <loading v-bind:loading="loading" v-bind:errored="errored">
    <form class="consultant-form">
      <div class="element-block">
        <label id="input-fullname-1" for="fullname-1">Nom</label>
        <input
          id="fullname-1"
          type="text"
          v-model="consultant.fullname"
          placeholder="Nom"
          readonly="true"
          />
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
        <router-link class="secondary-button" to="/consultants" tag="button">Cancel</router-link>
      </div>
    </form>
  </loading>
</div>
</template>

<script lang="ts">
import { Component, Prop, Vue } from "vue-property-decorator";
import { Consultant, ConsultantStatus, toString } from "../models/consultant";
import { ConsultantsService } from "../services/consultants";
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
    consultant: Consultant = { id: "", user: "", email: "", fullname: "", status: ConsultantStatus.MANAGER };
    loading = false;
    errored = false;
    options: { text: string; value: string; disabled: boolean }[] = [
        { text: "Choisissez une option", value: "", disabled: true }
    ];

    constructor() {
        super();
        this.consultantsService = new ConsultantsService(this.expertiseName);
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
            this.consultant = await this.consultantsService.getConsultant(this.consultantId);
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
