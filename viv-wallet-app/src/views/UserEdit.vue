<template>
    <div class="userEdit">
        <section v-if="errored">
            <p>We're sorry, we're not able to retrieve this information at the moment, please try back later</p>
        </section>
        <section v-else>
            <h2>Edit User</h2>
            <loading v-bind:loading="loading" v-bind:errored="errored">
                <form class="user-form">
                    <div class="element-block">
                        <label id="input-fullname-1" for="fullname-1">Nom</label>
                        <input id="fullname-1" type="text" v-model="user.fullName" placeholder="Nom" />
                    </div>

                    <div class="element-block">
                        <label id="input-user-1" for="user-1">Identifiant</label>
                        <input id="user-1" type="text" v-model="user.user" placeholder="Identifiant" />
                    </div>

                    <div class="element-block">
                        <label id="input-email-1" for="email-1">Email</label>
                        <input id="email-1" type="text" v-model="user.email" placeholder="Email" />
                    </div>

                    <div class="buttons">
                        <button class="primary-button" v-on:click="confirmUser">Confirmer</button>
                        <router-link class="secondary-button" to="/users" tag="button">Cancel</router-link>
                        <button class="secondary-button" v-on:click="deleteUser">Supprimer</button>
                    </div>
                </form>
            </loading>
        </section>
    </div>
</template>

<script lang="ts">
import { Component, Prop, Vue } from "vue-property-decorator";
import { User } from "../models/user";
import { UsersService } from "@/services/users";
import Loading from "../components/Loading.vue";

const UserEditProps = Vue.extend({
    props: {
        id: String,
    },
});

@Component({
    name: "user",
    components: { Loading },
})
export default class UserEdit extends UserEditProps {
    user: User | null = { id: "", fullName: "", user: "", email: "" };
    loading = false;
    errored = false;
    usersService = new UsersService();

    async mounted() {
        try {
            if (this.id !== "add") {
                this.loading = true;
                this.user = await this.usersService.getUser(this.id);
            }
        } catch (ex) {
            this.errored = true;
        } finally {
            this.loading = false;
        }
    }
    async confirmUser() {
        try {
            this.loading = true;
            if (this.user) {
                await this.usersService.saveUser(this.user);
                this.$router.push({ path: "/users" });
            }
        } catch (ex) {
            this.errored = true;
        } finally {
            this.loading = false;
        }
    }
    async deleteUser() {
        try {
            if (this.user) {
                this.loading = true;
                await this.usersService.deleteUser(this.user);
                this.$router.push({ path: "/users" });
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
