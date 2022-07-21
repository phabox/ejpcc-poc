<template>
  <div>
    <v-data-table
      :headers="headers"
      :items="companies"
      item-key="name"
      class="elevation-1 ma-3"
    >
    </v-data-table>
    <v-row justify="center" align="center" class="mt-2">
      <v-btn color="primary" class="white--text" @click="dialogVisible = true">
        <v-icon> mdi-plus </v-icon>
      </v-btn>
    </v-row>
    <CompanyDialog
      :visible="dialogVisible"
      :company="company"
      @confirmed="onCompanySave"
      @cancelled="onCompanyCancel"
    />
  </div>
</template>

<script>
import { mapGetters, mapActions } from "vuex";
import CompanyDialog from "@/components/company/CompanyDialog";
import companyService from "@/api/CompanyService";
export default {
  components: {
    CompanyDialog,
  },

  data: () => ({
    dialogVisible: false,
    company: {},
    polling: null,
  }),

  computed: {
    ...mapGetters("companies", ["companies"]),

    headers() {
      return [
        { text: "Name", value: "name" },
        { text: "Abkürzung", value: "abbreviation" },
        { text: "E-Mail", value: "email" },
        { text: "Telefon", value: "phone" },
        { text: "Fax", value: "fax" },
      ];
    },
  },

  created() {
    this.fetchCompanies();
    this.polling = setInterval(() => {
      this.fetchCompanies();
    }, 5000);
  },

  beforeDestroy() {
    clearInterval(this.polling);
  },

  methods: {
    ...mapActions("companies", ["fetchCompanies"]),

    onCompanyCancel() {
      this.dialogVisible = false;
      this.company = {};
    },

    onCompanySave() {
      companyService
        .save(Object.assign({}, this.company))
        .then((response) => {
          this.fetchCompanies();
          this.dialogVisible = false;
          this.company = {};
        })
        .catch((error) => {
          console.log(error);
        });
    },
  },
};
</script>
