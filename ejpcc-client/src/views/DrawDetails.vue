<template>
  <v-container v-if="draw">
    <v-row class="ma-3" justify="center" align="start">
      <v-banner
        single-line
        rounded
        color="primary"
        style="color: white"
        elevation="12"
        class="mr-5"
        icon="mdi-account-check"
        >Ziehungsdatum:
        {{
          new Date(draw.drawDate).toLocaleString("de-DE", {
            year: "numeric",
            month: "numeric",
            day: "numeric",
          })
        }}</v-banner
      >
      <v-banner
        rounded
        elevation="12"
        class="ml-5"
        icon="mdi-information"
        single-line
        color="primary"
        style="color: white"
        >Status: {{ draw.drawState }}</v-banner
      >
    </v-row>

    <v-tabs centered>
      <v-tab href="#CompanyOverview"> Gesellschaften </v-tab>
      <v-tab-item id="CompanyOverview">
        <CompanyStatusOverview
          :draw="draw"
          :companies="companies"
          :companyStates="draw.companyDrawStates"
          @initDraw="initDraw"
        />
      </v-tab-item>

      <v-tab href="#Combifiles"> Combifiles </v-tab>
      <v-tab-item id="Combifiles">
        <Combifiles
          :companies="companies"
          :combifiles="combifiles"
          @approveCombifile="approveCombifile($event)"
        />
      </v-tab-item>

      <v-tab href="#WinningNumbers"> Gewinnzahlen </v-tab>
      <v-tab-item id="WinningNumbers">
        <WinningNumbers
          :draw="draw"
          :winningNumbers="winningNumbers"
          @acceptWinningNumbers="acceptWinningNumbers($event)"
          @approveWinningNumbers="approveWinningNumbers($event)"
        />
      </v-tab-item>

      <v-tab href="#WinnerEvaluation"> Gewinnermittlung </v-tab>
      <v-tab-item id="WinnerEvaluation">
        <WinnerEvaluation
          :companies="companies"
          :draw="draw"
          :winnerEvaluations="winnerEvaluations"
          @startWinnerEvaluation="startWinnerEvaluation($event)"
          @approveWinnerEvaluation="approveWinnerEvaluation($event)"
        />
      </v-tab-item>

      <v-tab href="#Accounting"> Abrechnung </v-tab>
      <v-tab-item id="Accounting">
        <Accounting
          :companies="companies"
          :draw="draw"
          :accountingValues="accountingValues"
          @startAccounting="startAccounting($event)"
          @approveAccounting="approveAccounting($event)"
        />
      </v-tab-item>
    </v-tabs>
  </v-container>
</template>

<script>
import CompanyStatusOverview from "@/components/draw/CompanyStatusOverview";
import Combifiles from "@/components/draw/Combifiles";
import WinningNumbers from "@/components/draw/WinningNumbers";
import WinnerEvaluation from "@/components/draw/WinnerEvaluation";
import Accounting from "@/components/draw/Accounting";

import { mapActions, mapGetters } from "vuex";
import drawService from "@/api/DrawService";
import combifileService from "@/api/CombifileService";
import winningNumberService from "@/api/WinningNumbersService";
import winnerEvaluationService from "@/api/WinnerEvaluationService";
import accountingService from "@/api/AccountingService.js";

export default {
  components: {
    CompanyStatusOverview,
    Combifiles,
    WinningNumbers,
    WinnerEvaluation,
    Accounting,
  },

  props: ["drawId"],

  data() {
    return {
      draw: null,
      combifiles: null,
      winningNumbers: null,
      winnerEvaluations: null,
      accountingValues: null,

      polling: null,
    };
  },

  computed: {
    ...mapGetters("companies", ["companies"]),
  },

  async created() {
    this.fetchData();
    this.polling = setInterval(() => {
      this.fetchData();
    }, 5000);
  },

  beforeDestroy() {
    clearInterval(this.polling);
  },

  methods: {
    ...mapActions("companies", ["fetchCompanies"]),

    async fetchData() {
      this.fetchCompanies();
      this.draw = (await drawService.fetchById(this.drawId)).data;
      this.combifiles = (await combifileService.fetchAll(this.drawId)).data;
      this.winningNumbers = (
        await winningNumberService.fetchAll(this.drawId)
      ).data;
      this.winnerEvaluations = (
        await winnerEvaluationService.fetchAll(this.drawId)
      ).data;
      this.accountingValues = (
        await accountingService.fetchAll(this.drawId)
      ).data;
    },

    initDraw() {
      drawService
        .initDraw(this.drawId)
        .then(async (response) => {
          this.draw = response.data;
        })
        .catch((error) => console.log(error));
    },

    approveCombifile(combifile) {
      combifileService
        .approve(combifile)
        .then(async (response) => {
          this.combifiles = (await combifileService.fetchAll(this.drawId)).data;
          this.draw = (await drawService.fetchById(this.drawId)).data;
        })
        .catch((error) => console.log(error));
    },

    approveWinningNumbers(winningNumbers) {
      winningNumberService
        .approve(winningNumbers)
        .then(async (response) => {
          this.winningNumbers = response.data;
          this.draw = (await drawService.fetchById(this.drawId)).data;
        })
        .catch((error) => console.log(error));
    },

    acceptWinningNumbers(draw) {
      winningNumberService
        .accept(draw)
        .then(async (response) => {
          this.winningNumbers = (
            await winningNumberService.fetchAll(this.drawId)
          ).data;
          this.draw = (await drawService.fetchById(this.drawId)).data;
        })
        .catch((error) => console.log(error));
    },

    startWinnerEvaluation(draw) {
      winnerEvaluationService
        .runEvaluation(draw)
        .then(async (response) => {
          this.winnerEvaluations = response.data;
          this.draw = (await drawService.fetchById(this.drawId)).data;
        })
        .catch((error) => console.log(error));
    },

    approveWinnerEvaluation(winnerEvaluation) {
      winnerEvaluationService
        .approve(winnerEvaluation)
        .then(async (response) => {
          this.winnerEvaluations = response.data;
          this.draw = (await drawService.fetchById(this.drawId)).data;
        })
        .catch((error) => console.log(error));
    },

    startAccounting(draw) {
      accountingService
        .startAccounting(draw)
        .then(async (response) => {
          this.accountingValues = response.data;
          this.draw = (await drawService.fetchById(this.drawId)).data;
        })
        .catch((error) => console.log(error));
    },

    approveAccounting(accountingValues) {
      accountingService
        .approve(accountingValues)
        .then(async (response) => {
          this.accountingValues = response.data;
          this.draw = (await drawService.fetchById(this.drawId)).data;
        })
        .catch((error) => console.log(error));
    },
  },

  destroyed() {},
};
</script>

<style>
.v-banner__icon > .theme--light.v-icon {
  color: white;
}
</style>
