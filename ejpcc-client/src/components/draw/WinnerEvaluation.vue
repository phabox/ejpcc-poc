<template>
  <div>
    <v-data-table
      :headers="headers"
      :items="items"
      :sort-by.sync="columnName"
      :sort-desc.sync="isDescending"
      item-key="name"
      class="elevation-1 ma-3"
    >
      <template slot="body.append" v-if="aggregateEvaluation">
        <tr class="green--text">
          <th>Gesamt</th>
          <th
            v-for="(winnerCount, index) in aggregateEvaluation.winnerCounts"
            :key="index"
          >
            {{ winnerCount.count }}
          </th>
          <th>{{ aggregateEvaluation.totalWinners }}</th>
          <th>{{ aggregateEvaluation.approved }}</th>
        </tr>
      </template>
    </v-data-table>
    <v-row no-gutters justify="end">
      <v-btn
        :disabled="!startWinnerEvaluationEnabled()"
        @click="startWinnerEvaluation()"
        class="white--text ma-3"
        color="green"
      >
        Gewinnermittlung
      </v-btn>
      <v-btn
        :disabled="!approveButtonEnabled()"
        @click="approveWinnerEvaluation()"
        class="white--text ma-3"
        color="green"
      >
        Bestätigen
      </v-btn>
    </v-row>
  </div>
</template>

<script>
import { mapActions } from "vuex";
export default {
  props: ["draw", "companies", "winnerEvaluations"],

  data: () => ({
    columnName: "companyName",
    isDescending: false,
  }),

  computed: {
    aggregateEvaluation() {
      if (this.winnerEvaluations == null || this.winnerEvaluations.length == 0)
        return null;

      return this.winnerEvaluations.find((e) => e.company == null);
    },

    items() {
      if (
        this.winnerEvaluations == null ||
        this.winnerEvaluations.length == 0
      ) {
        return [];
      }

      var result = [];
      this.winnerEvaluations
        .filter((e) => e.company != null)
        .forEach((e) => {
          var row = {
            companyName: this.getCompanyName(e.company.id),
            totalWinners: e.totalWinners,
            approved: e.approved,
          };
          e.winnerCounts.forEach((wc) => {
            row[wc.winningClass] = wc.count;
          });

          result.push(row);
        });

      return result;
    },

    headers() {
      var result = [
        {
          text: "Gesellschaft",
          value: "companyName",
        },
      ];

      if (
        this.winnerEvaluations == null ||
        this.winnerEvaluations.length == 0
      ) {
        return result;
      }

      var firstEvaluation = this.winnerEvaluations[0];
      for (var i = 0; i < firstEvaluation.winnerCounts.length; i++) {
        const winnerCount = firstEvaluation.winnerCounts[i];
        result.push({
          text: winnerCount.winningClass,
          value: String(winnerCount.winningClass),
        });
      }
      result.push({
        text: "Gesamt",
        value: "totalWinners",
      });
      result.push({
        text: "Bestätigt",
        value: "approved",
      });

      return result;
    },
  },

  methods: {
    ...mapActions("companies", ["fetchCompanies"]),

    getCompanyName(id) {
      var company = this.companies.find((e) => e.id == id);
      return company == undefined ? "Unbekannt" : company.name;
    },

    startWinnerEvaluation() {
      this.$emit("startWinnerEvaluation", this.draw);
    },

    approveWinnerEvaluation() {
      this.$emit("approveWinnerEvaluation", this.aggregateEvaluation);
    },

    startWinnerEvaluationEnabled() {
      return this.draw.drawState == "WINNING_NUMBERS_CONFIRMED";
    },

    approveButtonEnabled() {
      return this.draw.drawState == "WINNER_EVALUATION";
    },
  },
};
</script>
