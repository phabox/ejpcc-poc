import axios from "axios";
class WinningNumberService {
  constructor() {
    this.service = axios.create({
      baseURL: `${process.env.VUE_APP_BACKEND_URL}/`,
    });
  }

  fetchAll(drawId) {
    return this.service.get(`draws/${drawId}/winningnumbers`);
  }

  accept(draw) {
    return this.service.post(`draws/${draw.id}/winningnumbers/actions`, {
      type: "READY_TO_RECEIVE",
    });
  }

  approve(winningNumbers) {
    return this.service.post(
      `draws/${winningNumbers.draw.id}/winningnumbers/actions`,
      {
        type: "APPROVE_WINNINGNUMBERS",
      }
    );
  }
}

export default new WinningNumberService();
