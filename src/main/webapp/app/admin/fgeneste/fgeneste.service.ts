import axios, { AxiosInstance } from 'axios';

export default class FgenesteService {
  private axios: AxiosInstance;

  constructor() {
    this.axios = axios;
  }

  public resetMails(): Promise<any> {
    return new Promise(resolve => {
      axios.get(`/api/gmail/reset-mails`).then(res => resolve(res));
    });
  }

  public getMails(): Promise<any> {
    return new Promise(resolve => {
      axios.get(`/api/gmail/get-mails`).then(res => resolve(res));
    });
  }

}
