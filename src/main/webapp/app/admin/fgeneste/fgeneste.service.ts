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

  public getfetchedMails(): Promise<any> {
    return new Promise(resolve => {
      axios.get(`/api/gmail/get-fetched-mails`).then(res => resolve(res));
    });
  }

  public getunfetchedMails(): Promise<any> {
    return new Promise(resolve => {
      axios.get(`/api/gmail/get-unfetched-mails`).then(res => resolve(res));
    });
  }

  public stop(): Promise<any> {
    return new Promise(resolve => {
      axios.post(`/api/gmail/stop-get-mails`).then(res => resolve(res));
    });
  }

  public countByFrom(account): Promise<any> {
    return new Promise(resolve => {
      axios.get(`/api/messagescountbyfrom/` + account).then(res => resolve(res));
    });
  }

  public countVoids(account): Promise<any> {
    return new Promise(resolve => {
      axios.get(`/api/messagescountofvoids/` + account).then(res => resolve(res));
    });
  }
}
