<template>
    <div class="userEdit">
        <section v-if="errored">
            <p>We're sorry, we're not able to retrieve this information at the moment, please try back later</p>
        </section>
        <section v-else>
            <h2>Edit User</h2>
            <div v-if="loading">Loading...</div>
            <b-form v-else>
              <b-form-group
                id="input-fullname-1"
                label="Name:"
                label-for="fullname-1"
                >
                <b-form-input
                  id="fullname-1"
                  type="text"
                  required
                  v-model="user.fullname"
                  placeholder="Enter user's full name"
                  ></b-form-input>
              </b-form-group>

              <b-form-group
                id="input-login-1"
                label="Login:"
                label-for="login-1"
                >
                <b-form-input
                  id="login-1"
                  type="text"
                  required
                  v-model="user.login"
                  placeholder="Enter user's login"
                  ></b-form-input>
              </b-form-group>

              <b-form-group
                id="input-email-1"
                label="Email address:"
                label-for="email-1"
                >
                <b-form-input
                  id="email-1"
                  type="email"
                  required
                  v-model="user.email"
                  placeholder="Enter email"
                  ></b-form-input>
              </b-form-group>


              <b-button variant="primary" v-on:click="confirmUser">Confirm</b-button>
              &nbsp;
              <b-button variant="outline-primary" to="/users">Cancel</b-button>
              &nbsp;
              <b-button variant="danger" v-on:click="deleteUser" to="/users">Delete</b-button>
            </b-form>
        </section>
    </div>
</template>

<script lang="ts">
import { Component, Prop, Vue } from "vue-property-decorator";
import { User } from "../models/user";
import { UsersService } from "@/services/users";

const UserEditProps = Vue.extend({
  props: {
    id: String
  }
})

@Component({
    name: "user"
})
export default class UserEdit extends UserEditProps {
    user: User | null = { id: "", fullname: "", login: "", email: "" };
    loading = false;
    errored = false;
    usersService = new UsersService();

    async mounted() {
        try {
            if (this.id !== 'add') {
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
                this.$router.push({ path: '/users' });
            }
        } catch(ex) {
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
                this.$router.push({ path: '/users' });
            }
        } catch(ex) {
            this.errored = true;
        } finally {
            this.loading = false;
        }
    }

}
</script>

<style scoped>
.userEdit {
    width: 50%;
    text-align: left;
    display: inline-block;
}

h2 {
    text-align: center;
}
</style>
