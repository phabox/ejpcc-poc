import axios from "axios";
class AccountingService {
  constructor() {
    this.service = axios.create({
      baseURL: `${process.env.VUE_APP_BACKEND_URL}/`,
    });
  }

  fetchAll(drawId) {
    return this.service.get(`draws/${drawId}/accountings`);
  }

  startAccounting(draw) {
    return this.service.post(`/draws/${draw.id}/accountings/actions`, {
      type: "CALCULATE_ACCOUNTINGS",
    });
  }

  approve(accountingValues) {
    return this.service.post(`/draws/${accountingValues.draw.id}/accountings/actions`, {
      type: "CONFIRM_ACCOUNTING_CALCULATION",
    });
  }
}

export default new AccountingService();
