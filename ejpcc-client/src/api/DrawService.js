import axios from "axios";
class DrawService {
  constructor() {
    this.service = axios.create({
      baseURL: `${process.env.VUE_APP_BACKEND_URL}/`,
    });
  }

  fetchAll() {
    return this.service.get("draws");
  }

  fetchById(id) {
    return this.service.get(`draws/${id}`);
  }

  create(draw) {
    return this.service.post("draws", draw);
  }

  initDraw(drawId) {
    return this.service.post(`draws/${drawId}/actions`, {
      type: "CHANGE_STATE",
      state: "INIT",
    });
  }
}

export default new DrawService();
