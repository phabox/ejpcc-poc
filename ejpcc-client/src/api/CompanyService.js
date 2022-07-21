import axios from "axios";
class CompanyService {
  constructor() {
    this.service = axios.create({
      baseURL: `${process.env.VUE_APP_BACKEND_URL}/`,
      //baseURL: 'http://localhost:8085/'
    });
  }

  fetchAll() {
    return this.service.get("companies");
  }

  save(company){
    return this.service.post('companies', company);
  }

}

export default new CompanyService();
