<template>
  <v-app>
    <v-app-bar app color="primary" dark>
      <v-spacer />
      <v-img
        alt="Vuetify Logo"
        class="shrink mr-2"
        contain
        src="https://cdn-icons-png.flaticon.com/512/3712/3712259.png"
        transition="scale-transition"
        width="40"
      />
      <h2>Eurojackpot Control Center</h2>
      <v-spacer />
    </v-app-bar>

    <v-main>
      <router-view> </router-view>

      <v-container>
        <v-row justify="center" align="center" no-gutters>
          <LogOverview :logs="logs"> </LogOverview>
        </v-row>
      </v-container>
    </v-main>

    <v-bottom-navigation app color="primary">
      <v-btn to="/draws">
        <span>Ziehungen</span>
        <v-icon>mdi-vote</v-icon>
      </v-btn>
      <v-btn to="/companies">
        <span>Gesellschaften</span>
        <v-icon>mdi-office-building</v-icon>
      </v-btn>
      <v-btn to="/about">
        <span>About</span>
        <v-icon>mdi-comment-question</v-icon>
      </v-btn>
    </v-bottom-navigation>
  </v-app>
</template>

<script>
import logService from "@/api/LogService";
import LogOverview from "@/components/logs/LogOverview";
export default {
  name: "App",
  components: {
    LogOverview,
  },

  data: () => ({
    logs: [],
    polling: null,
  }),

  created() {
    this.polling = setInterval(async() => {
      this.logs = (await logService.fetchAll()).data;
    }, 1000);
  },

  beforeDestroy() {
    clearInterval(this.polling);
  },

  mounted() {},
};
</script>

<style>
body {
  font-family: "Roboto";
}
</style>

<style scoped>
a {
  color: white;
}
.active {
  border-bottom: white solid 1px;
}
</style>
