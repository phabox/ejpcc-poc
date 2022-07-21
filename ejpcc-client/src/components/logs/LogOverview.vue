<template>
  <v-container>
    <v-row justify="center" no-gutters class="mt-5">
      <v-expansion-panels accordion>
        <v-expansion-panel>
          <v-expansion-panel-header>Logs</v-expansion-panel-header>
          <v-expansion-panel-content>
            <v-data-table
              :headers="headers"
              :items="logs"
              :search="search"
              item-key="id"
              class="elevation-1 ma-3"
            >
              <template v-slot:top>
                <v-text-field
                  v-model="search"
                  label="Filter"
                  class="mx-4"
                ></v-text-field>
              </template>
              <template v-slot:[`item.event`]="{ item }">
                <v-btn icon color="primary" @click="openEventDialog(item)">
                  <v-icon>mdi-magnify</v-icon>
                </v-btn>
              </template>
            </v-data-table>
          </v-expansion-panel-content>
        </v-expansion-panel>
      </v-expansion-panels>
    </v-row>
    <v-dialog v-model="dialogVisible" width="400" max-width="800px">
      <v-card>
        <v-card-title class="headline"> {{ currentEventName }} </v-card-title>

        <v-card-text>
          <pre>{{ currentEventText }}</pre>
        </v-card-text>

        <v-divider></v-divider>

        <v-card-actions>
          <v-spacer></v-spacer>
          <v-btn color="blue darken-1" text @click="closeDialog"> Close </v-btn>
        </v-card-actions>
      </v-card>
    </v-dialog>
  </v-container>
</template>

<script>
export default {
  props: ["logs"],

  data: () => ({
    dialogVisible: false,
    currentEventName: "",
    currentEventText: "",
    search: "",
    headers: [
      { text: "id", value: "id", align: " d-none" },
      { text: "Zeit", value: "time" },
      { text: "Service", value: "serviceName" },
      { text: "Art", value: "direction" },
      { text: "Event", value: "eventName" },
      { text: "Typ", value: "type" },
      { text: "Aktion", value: "action" },
      { text: "", value: "event" },
    ],
  }),

  methods: {
    openEventDialog(item) {
      this.currentEventName = item.eventName;
      this.currentEventText = JSON.stringify(
        JSON.parse(item.event).event,
        null,
        2
      );
      this.dialogVisible = true;
    },
    closeDialog() {
      this.dialogVisible = false;
    },
  },
};
</script>

<style></style>
