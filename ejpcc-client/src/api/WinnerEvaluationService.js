import axios from "axios";
class WinnerEvaluationService {
  constructor() {
    this.service = axios.create({
      baseURL: `${process.env.VUE_APP_BACKEND_URL}/`,
    });
  }

  fetchAll(drawId) {
    return this.service.get(`/draws/${drawId}/winners`);
  }

  runEvaluation(draw) {
    return this.service.post(`/draws/${draw.id}/winners/actions`, {
      type: "RUN_WINNER_EVALUATION",
    });
  }

  approve(winnerEvaluation) {
    return this.service.post(
      `/draws/${winnerEvaluation.draw.id}/winners/actions`,
      {
        type: "CONFIRM_WINNER_EVALUATION",
      }
    );
  }
}

export default new WinnerEvaluationService();
