<template>
  <div>
    <v-data-table
      :headers="headers"
      :items="items"
      item-key="name"
      class="elevation-1 ma-3"
    >
    </v-data-table>
    <v-row no-gutters justify="end">
      <v-btn
        :disabled="!acceptButtonEnabled()"
        @click="acceptWinningNumbers()"
        class="white--text ma-3"
        color="green"
      >
      Empfang freischalten
      </v-btn>
      <v-btn
        :disabled="!approveButtonEnabled()"
        @click="approveWinningNumbers()"
        class="white--text ma-3"
        color="green"
      >
      Gewinnzahlen bestätigen
      </v-btn>
    </v-row>
  </div>
</template>

<script>
export default {
  props: ["winningNumbers", "draw"],

  computed: {
    items() {
      if (
        this.winningNumbers == null ||
        this.winningNumbers.winningNumbers == null ||
        this.winningNumbers.winningNumbers.length == 0
      )
        return [];

      return this.winningNumbers.winningNumbers;
    },

    headers() {
      return [
        { text: "Position", value: "position" },
        { text: "Gewinnzahl", value: "number" },
      ];
    },
  },

  methods: {
    approveWinningNumbers() {
      this.$emit("approveWinningNumbers", this.winningNumbers);
    },

    acceptWinningNumbers() {
      this.$emit("acceptWinningNumbers", this.draw);
    },

    acceptButtonEnabled() {
      return this.draw.drawState == "INIT";
    },

    approveButtonEnabled() {
      return this.draw.drawState == "WINNING_NUMBERS_RECEIVED";
    },
  },
};
</script>
