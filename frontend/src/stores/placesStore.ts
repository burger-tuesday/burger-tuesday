import axios from 'axios';
import {action, observable} from 'mobx';
import authStore from './authStore';
import {CONSTANTS} from './constants';

class PlacesStore {
  @observable public address: string = "";
  @observable public placeId: string = "";

  @action public savePlace() {
    const headers = {'Authorization': `Bearer ${authStore.accessToken}`}
    axios.post(`${CONSTANTS.API_URL}/restaurant/add/${this.placeId}`, null, {headers})
    .then(response => {
      console.log(response);
      this.placeId = "";
      this.address = "";
    })
    .catch(error => {
      console.log(error);
      this.placeId = "";
      this.address = "";
    });
  }
}

export default new PlacesStore();
