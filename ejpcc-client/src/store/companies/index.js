import companyService from "@/api/CompanyService";

export default {
  namespaced: true,
  state: () => ({
    companies: [],
  }),
  getters: {
    companies: (state) => state.companies,
  },
  mutations: {
    setCompanies(state, value) {
      state.companies = value;
    },
  },
  actions: {
    async fetchCompanies(ctx) {
      ctx.commit("setCompanies", (await companyService.fetchAll()).data);
    },
  },
};
