<template>
  <v-container>
    <v-row>
      <v-col class="ma-3">
        <v-data-table
          :headers="headers"
          :items="items"
          :sort-by.sync="columnName"
          :sort-desc.sync="isDescending"
          item-key="name"
          class="elevation-1"
        >
        </v-data-table>
      </v-col>
    </v-row>
    <v-row no-gutters justify="end">
      <v-btn
        color="green"
        :disabled="draw.drawState != 'CLOSED'"
        class="white--text mr-3"
        @click="initDrawClicked"
      >
        Ziehung initialisieren
      </v-btn>
    </v-row>
  </v-container>
</template>

<script>
import { mapGetters, mapActions } from "vuex";

export default {
  data: () => ({
    columnName: "name",
    isDescending: false,
  }),

  props: ["draw", "companyStates", "companies"],

  computed: {
    items() {
      if (this.companyStates == null) return [];
      return this.companyStates.map((e) => {
        return {
          ...this.getCompany(e.company.id),
          status: e.companyState,
        };
      });
    },

    headers() {
      return [
        { text: "Name", value: "name" },
        { text: "Abkürzung", value: "abbreviation" },
        { text: "E-Mail", value: "email" },
        { text: "Telefon", value: "phone" },
        { text: "Fax", value: "fax" },
        { text: "Status", value: "status" },
      ];
    },
  },

  methods: {
    getCompany(companyId) {
      return this.companies.find((e) => e.id == companyId);
    },

    initDrawClicked() {
      this.$emit("initDraw");
    },
  },
};
</script>
