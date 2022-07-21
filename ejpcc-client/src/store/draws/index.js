import drawService from "@/api/DrawService";

export default {
  namespaced: true,
  state: () => ({
    draws: null,
  }),
  getters: {
    draws: (state) => state.draws,
  },
  mutations: {
    setDraws(state, value) {
      state.draws = value;
    },
  },
  actions: {
    async fetchDraws(ctx) {
      ctx.commit("setDraws", (await drawService.fetchAll()).data);
    },
  },
};
