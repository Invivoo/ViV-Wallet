<template>
    <div class="users">
        <UserList />
    </div>
</template>

<script>
// @ is an alias to /src
import UserList from "@/components/UserList.vue";

export default {
    name: "users",
    components: {
        UserList
    }
};
</script>
