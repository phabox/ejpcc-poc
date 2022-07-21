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
      <template v-slot:[`item.approve`]="{ item }">
        <v-btn
          :disabled="!approveButtonEnabled(item)"
          @click="approveCombifile(item)"
          class="white--text"
          color="green"
        >
          <v-icon left>mdi-check</v-icon>
          Bestätigen
        </v-btn>
      </template>
    </v-data-table>
  </div>
</template>

<script>
export default {
  data: () => ({
    columnName: "companyName",
    isDescending: false,
  }),

  props: ["combifiles", "companies"],

  computed: {
    items() {
      if (this.combifiles == null || this.combifiles.length == 0) return [];

      return this.combifiles.map((e) => {
        return {
          ...e,
          amount: (e.amount / 100).toFixed(2) + "€",
          companyName: this.getCompanyName(e.companyId),
        };
      });
    },

    headers() {
      return [
        { text: "Gesellschaft", value: "companyName" },
        { text: "Dateiname", value: "filename" },
        { text: "Einsatz", value: "amount" },
        { text: "Bestätigt (Gesellschaft)", value: "confirmedByCompany" },
        { text: "Bestätigt (CC)", value: "approvedByCC" },
        { text: "", value: "approve" },
      ];
    },
  },

  methods: {
    getCompanyName(id) {
      var company = this.companies.find((e) => e.id == id);
      return company == undefined ? "Unbekannt" : company.name;
    },

    approveButtonEnabled(combifile) {
      var draw = combifile.draw;
      return (
        draw.drawState == "INIT" &&
        combifile.approvedByCC == false &&
        combifile.confirmedByCompany == true
      );
    },

    approveCombifile(combifile) {
      this.$emit("approveCombifile", combifile);
    },
  },
};
</script>
