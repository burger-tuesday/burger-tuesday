import axios from 'axios';
import {action, observable} from 'mobx';
import {IToplistEntry} from '../shared/models/review.model';
import authStore from './authStore';
import {CONSTANTS} from './constants';

class ToplistStore {
  @observable public entries: IToplistEntry[] = [];
  @observable public loading: boolean = false;

  @action
  public getToplist() {
    this.loading = true;
    const headers = {'Authorization': `Bearer ${authStore.accessToken}`};
    axios.get<IToplistEntry[]>(`${CONSTANTS.API_URL}/reviews/top`, {headers})
    .then(response => {
      console.log(response);
      this.entries = response.data;
      this.loading = false;
    })
    .catch(error => {
      console.log(error);
      this.entries = [];
      this.loading = false;
    });
  }
}

export default new ToplistStore();
