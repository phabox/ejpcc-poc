<template>
  <v-container>
    <v-row class="mt-12" justify="center" v-if="!draws || draws.length == 0">
      <v-alert dense type="info"> Aktuell existieren keine Ziehungen </v-alert>
    </v-row>
    <div v-else>
      <v-row justify="center">
        <v-card
          outlined
          elevation="2"
          v-for="(draw, index) in sortedDraws"
          :key="index"
          @click="openDraw(draw)"
          class="ma-3"
        >
          <v-img
            :src="'https://picsum.photos/seed/' + (index + 1) + '/350/200'"
            height="200"
            class="white--text align-end"
            gradient="to bottom, rgba(0,0,0,.1), rgba(0,0,0,.5)"
          >
            <v-card-subtitle class="white--text"
              >Ziehungsdatum:
              {{
                new Date(draw.drawDate).toLocaleString("de-DE", {
                  weekday: "short",
                  year: "numeric",
                  month: "long",
                  day: "numeric",
                })
              }}</v-card-subtitle
            >
          </v-img>
        </v-card>
      </v-row>
    </div>
    <v-row no-gutters justify="center" align="center" class="mt-2">
      <v-btn color="primary" class="white--text" @click="dialog.visible = true">
        <v-icon> mdi-plus </v-icon>
      </v-btn>
    </v-row>
    <DrawCreationDialog
      :visible="dialog.visible"
      :companyList="companies"
      @confirmed="onDrawSave"
      @cancelled="onDrawCancel"
    />
  </v-container>
</template>

<script>
import { mapGetters, mapActions } from "vuex";
import DrawCreationDialog from "@/components/draw/DrawCreationDialog";
import drawService from "@/api/DrawService";
export default {
  components: {
    DrawCreationDialog,
  },

  data: () => ({
    dialog: {
      visible: false,
      drawDate: null,
      selectedCompanies: [],
    },
    polling: null,
  }),

  computed: {
    ...mapGetters("draws", ["draws"]),
    ...mapGetters("companies", ["companies"]),

    sortedDraws() {
      if (this.draws == null) return [];

      return [...this.draws].sort(function (a, b) {
        return new Date(b.drawDate) - new Date(a.drawDate);
      });
    },
  },

  created() {
    this.fetchDraws();
    this.fetchCompanies();
    this.polling = setInterval(() => {
      this.fetchDraws();
      this.fetchCompanies();
    }, 5000);
  },

  beforeDestroy() {
    clearInterval(this.polling);
  },

  methods: {
    ...mapActions("draws", ["fetchDraws"]),
    ...mapActions("companies", ["fetchCompanies"]),
    openDraw(draw) {
      this.$router.push({
        name: "DrawDetails",
        params: {
          drawId: draw.id,
        },
      });
    },

    onDrawCancel() {
      this.dialog.visible = false;
    },

    onDrawSave(drawData) {
      var payload = {
        drawDate: drawData.drawDate,
        drawState: "CLOSED",
        companyDrawStates: drawData.selectedCompanies.map((e) => {
          return {
            company: {
              id: e.id,
            },
            companyState: "NOT_READY",
          };
        }),
      };
      drawService
        .create(payload)
        .then((response) => {
          this.dialog.visible = false;
          this.fetchDraws();
        })
        .catch((error) => console.log(error));
    },
  },
};
</script>
