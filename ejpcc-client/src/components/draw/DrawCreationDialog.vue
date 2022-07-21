<template>
  <v-dialog v-model="visible" persistent width="800" max-width="800px">
    <v-card>
      <v-card-title class="headline grey lighten-2">
        Ziehung erstellen
      </v-card-title>

      <v-card-text>
        <v-form class="mt-7" ref="form" v-model="valid" lazy-validation>
          <v-row>
            <v-col>
              <v-date-picker v-model="drawDate"></v-date-picker>
            </v-col>

            <v-col>
              <v-select
                v-model="selectedCompanies"
                :items="companyList"
                :item-text="'name'"
                return-object
                label="Auswahl"
                multiple
                chips
                hint="Teilnehmende Gesellschaften"
                persistent-hint
              ></v-select>
            </v-col>
          </v-row>
        </v-form>
      </v-card-text>

      <v-divider></v-divider>

      <v-card-actions>
        <v-spacer></v-spacer>
        <v-btn color="blue darken-1" text @click="onCancel"> Close </v-btn>
        <v-btn
          color="green"
          :disabled="!valid"
          class="white--text"
          @click="onSave"
        >
          Save
        </v-btn>
      </v-card-actions>
    </v-card>
  </v-dialog>
</template>

<script>
export default {
  data: () => ({
    valid: true,
    nameRules: [(v) => !!v || "Name is required"],
    drawDate: null,
    selectedCompanies: [],
  }),

  props: {
    companyList: {
      type: Array,
    },
    visible: {
      type: Boolean,
      default: false,
    },
  },

  methods: {
    onSave() {
      if (this.$refs.form.validate()) {
        this.$emit("confirmed", {
          drawDate: this.drawDate,
          selectedCompanies: this.selectedCompanies,
        });
      }
    },

    onCancel() {
      this.$emit("cancelled");
    },
  },
};
</script>
