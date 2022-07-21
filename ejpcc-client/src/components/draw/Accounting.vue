<template>
  <v-container>
    <v-row justify="center">
      <v-col cols="7">
        <v-card v-if="accountingValues" elevation="3" class="ma-2">
          <v-card-text>
            <v-container>
              <v-row>
                <v-col cols="6">
                  <v-row no-gutters align="center" justify="start">
                    <v-icon color="primary" left>mdi-cash-multiple</v-icon>
                    <span class="mr-3">Gesamteinsatz:</span>
                    <span
                      >{{
                        (accountingValues.totalStake / 100).toFixed(2)
                      }}
                      €</span
                    >
                  </v-row>
                </v-col>
                <v-col cols="6">
                  <v-row no-gutters align="center" justify="start">
                    <v-icon color="primary" left>mdi-account-cash</v-icon>
                    <span class="mr-3">Gewinner:</span>
                    <span>{{ accountingValues.totalWinners }} </span>
                  </v-row>
                </v-col>
              </v-row>
              <v-row>
                <v-col cols="6">
                  <v-row no-gutters align="center" justify="start">
                    <v-icon color="primary" left>mdi-cash-refund</v-icon>
                    <span class="mr-3">Nicht ausgezahlte Gewinne:</span>
                    <span
                      >{{
                        (accountingValues.leftoverStake / 100).toFixed(2)
                      }}
                      €</span
                    >
                  </v-row>
                </v-col>
                <v-col cols="6">
                  <v-row no-gutters align="center" justify="start">
                    <v-icon color="primary" left>mdi-information</v-icon>
                    <span class="mr-3">Bestätigt:</span>
                    <span>{{ accountingValues.approved }}</span>
                  </v-row>
                </v-col>
              </v-row>
            </v-container>
          </v-card-text>
          <v-card-actions>
            <v-spacer />
            <v-btn
              :disabled="!approveAccountingButtonEnabled()"
              @click="approveAccounting()"
              class="white--text"
              color="green"
            >
              Bestätigen
            </v-btn>
          </v-card-actions>
        </v-card>
      </v-col>
    </v-row>
    <v-row justify="center">
      <v-col cols="12">
        <v-data-table
          :headers="headers"
          :items="items"
          :sort-by.sync="columnName"
          :sort-desc.sync="isDescending"
          item-key="name"
          class="elevation-1 ma-3"
        >
        </v-data-table>
      </v-col>
    </v-row>
    <v-row no-gutters justify="end">
      <v-btn
        :disabled="!startAccountingButtonEnabled()"
        @click="startAccounting()"
        class="white--text ma-3"
        color="green"
      >
        Abrechnung
      </v-btn>
    </v-row>
  </v-container>
</template>

<script>
export default {
  props: ["companies", "draw", "accountingValues"],

  data: () => ({
    columnName: "companyName",
    isDescending: false,
  }),

  computed: {
    items() {
      if (
        this.accountingValues == null ||
        this.accountingValues.companyAccountingValues == null ||
        this.accountingValues.companyAccountingValues.length == 0
      ) {
        return [];
      }

      return this.accountingValues.companyAccountingValues.map((e) => {
        return {
          ...e,
          stake: (e.stake / 100).toFixed(2),
          payout: (e.payout / 100).toFixed(2),
          companyName: this.getCompanyName(e.company.id),
        };
      });
    },

    headers() {
      return [
        {
          text: "Gesellschaft",
          value: "companyName",
        },
        {
          text: "Einsatz",
          value: "stake",
        },
        {
          text: "Auszahlung",
          value: "payout",
        },
      ];
    },
  },

  methods: {
    getCompanyName(id) {
      var company = this.companies.find((e) => e.id == id);
      return company == undefined ? "Unbekannt" : company.name;
    },

    startAccounting() {
      this.$emit("startAccounting", this.draw);
    },

    approveAccounting() {
      this.$emit("approveAccounting", this.accountingValues);
    },

    startAccountingButtonEnabled() {
      return this.draw.drawState == "WINNER_EVALUATION_CONFIRMED";
    },

    approveAccountingButtonEnabled() {
      return this.draw.drawState == "ACCOUNTING_CALCULATION";
    },
  },
};
</script>
