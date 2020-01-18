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


              <b-button type="submit" variant="primary">Confirm</b-button>
              &nbsp;
              <b-button type="reset"  variant="danger" to="/users">Cancel</b-button>
            </b-form>
        </section>
    </div>
</template>

<script lang="ts">
import { Component, Prop, Vue } from "vue-property-decorator";
import { User } from "../models/user";
import { UsersService } from "@/services/users";

@Component({
    name: "user"
})
export default class UserEdit extends Vue {
    user: User | null = { id: "0", fullname: "myName0", login: "login0", email: "test0@test" };
    loading = false;
    errored = false;
    usersService = new UsersService();
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
