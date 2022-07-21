import axios from "axios";
class LogService {
  constructor() {
    this.service = axios.create({
      baseURL: `${process.env.VUE_APP_BACKEND_URL}/`,
    });
  }

  fetchAll() {
    return this.service.get(`logs`);
  }
}

export default new LogService();
