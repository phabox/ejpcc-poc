<template>
  <v-dialog v-model="visible" persistent width="500" max-width="600px">
    <v-card>
      <v-card-title class="headline grey lighten-2">
        Gesellschaft hinzufügen
      </v-card-title>

      <v-card-text>
        <v-form class="mt-7" ref="form" v-model="valid" lazy-validation>
          <v-text-field
            :counter="32"
            label="Name"
            required
            v-model="company.name"
            class="mb-8"
          />
          <v-text-field
            :counter="32"
            label="Abkürzung"
            required
            v-model="company.abbreviation"
            class="mb-8"
          />
          <v-text-field
            :counter="32"
            label="E-Mail"
            required
            v-model="company.email"
            class="mb-8"
          />
          <v-text-field
            :counter="32"
            label="Telefon"
            required
            v-model="company.phone"
            class="mb-8"
          />
          <v-text-field
            :counter="32"
            label="Fax"
            required
            v-model="company.fax"
            class="mb-8"
          />
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
  }),

  props: {
    company: {
      type: Object,
    },
    visible: {
      type: Boolean,
      default: false,
    },
  },

  methods: {
    onSave() {
      if (this.$refs.form.validate()) {
        this.$emit("confirmed", this.company);
      }
    },

    onCancel() {
      this.$emit("cancelled");
    },
  },
};
</script>
