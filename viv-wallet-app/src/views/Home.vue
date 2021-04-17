<template>
    <div class="home">
        <div class="container">
            <div class="text">
                <h1>Gérer facilement vos ViVs <br />avec le ViV Wallet</h1>
                <router-link class="primary-button" v-bind:to="{ path: `/wallet` }">Accéder à mon wallet</router-link>
            </div>
            <img src="../assets/home.svg" class="illustration" alt="" />
        </div>
    </div>
</template>

<style lang="scss" scoped>
@import "../styles/buttons.scss";

.home {
    width: 95%;
    margin: $m-7 auto;
    padding: $m-3 $m-5;
}

.container {
    display: flex;
    flex-direction: row;
    align-items: center;
}

.illustration {
    width: 60%;
    height: auto;
    margin-left: $m-3;
    color: $primary-700;
}

.text {
    display: flex;
    flex-direction: column;
    justify-content: center;
    width: 40%;
    margin-right: $m-2;
    button {
        width: max-content;
    }
}

h1 {
    font-size: $text-4xl;
    color: $gray-700;
    font-weight: 600;
    margin: $m-6 0 $m-6 0;
}
</style>
