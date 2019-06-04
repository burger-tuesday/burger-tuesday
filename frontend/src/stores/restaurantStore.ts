import axios from 'axios';
import {none, Option, some} from 'fp-ts/lib/Option';
import {action, observable} from 'mobx';
import {SortingRule} from 'react-table';
import {IRestaurant} from '../shared/models/restaurant.model';
import authStore from './authStore';
import {CONSTANTS} from './constants';

class RestaurantStore {
  @observable public restaurant: Option<IRestaurant> = none;
  @observable public restaurants: IRestaurant[] = [];
  @observable public loading: boolean = false;
  @observable public page: number;
  @observable public pages: number = -1;
  @observable public pageSize: number = 20;
  @observable public sorted: SortingRule[];


  @action public getRestaurants(page: number = 0, pageSize: number = 20) {
    this.loading = true;
    const headers = {'Authorization': `Bearer ${authStore.accessToken}`};
    axios.get<IRestaurant[]>(`${CONSTANTS.API_URL}/restaurants?page=${page}&size=${pageSize}`, {headers})
    .then(response => {
      console.log(response);
      this.restaurants = response.data;
      this.loading = false;
      this.pages = Math.ceil(response.headers['x-total-count'] / pageSize);
    })
    .catch(error => {
      console.log(error);
      this.restaurants = [];
      this.loading = false;
    });
  }

  @action public getRestaurant(id: string) {
    this.loading = true;
    const headers = {'Authorization': `Bearer ${authStore.accessToken}`};
    axios.get<IRestaurant>(`${CONSTANTS.API_URL}/restaurant/${id}`, {headers})
    .then(response => {
      console.log(response);
      this.restaurant = some(response.data);
      this.loading = false;
    })
    .catch(error => {
      console.log(error);
      this.restaurant = none;
      this.loading = false;
    });
  }
}

export default new RestaurantStore();
