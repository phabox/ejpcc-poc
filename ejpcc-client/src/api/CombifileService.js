import axios from "axios";
class CombifileService {
  constructor() {
    this.service = axios.create({
      baseURL: `${process.env.VUE_APP_BACKEND_URL}/`,
    });
  }

  fetchAll(drawId) {
    return this.service.get(`draws/${drawId}/combifiles`);
  }

  approve(combifile) {
    return this.service.post(
      `draws/${combifile.draw.id}/companies/${combifile.companyId}/combifiles/actions`,
      {
        type: "APPROVE_COMBIFILE",
        combifileApproved: true,
      }
    );
  }
}

export default new CombifileService();
