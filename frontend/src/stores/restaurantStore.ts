import axios from 'axios';
import {none, Option, some} from 'fp-ts/lib/Option';
import {action, observable} from 'mobx';
import * as moment from 'moment';
import {Moment} from 'moment';
import {SortingRule} from 'react-table';
import {toast} from 'react-toastify';
import {IRestaurant} from '../shared/models/restaurant.model';
import {IVisit} from '../shared/models/visit.model';
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

  @observable public date: Moment | null = moment();
  @observable public sponsored: boolean = false;
  @observable public focused: boolean | null = false;


  @action
  public getRestaurants(page: number = 0, pageSize: number = 20) {
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

  @action
  public getRestaurant(id: string) {
    this.loading = true;
    const headers = {'Authorization': `Bearer ${authStore.accessToken}`};
    axios.get<IRestaurant>(`${CONSTANTS.API_URL}/restaurants/${id}`, {headers})
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

  @action
  public addVisit(id: string) {
    if (!this.date) {
      toast.error("Date must be selected!");
      return;
    }
    this.loading = true;
    const headers = {'Authorization': `Bearer ${authStore.accessToken}`};
    axios.post<IVisit>(`${CONSTANTS.API_URL}/visits/${id}/add/${this.date.format("YYYY-MM-DD")}?sponsored=${this.sponsored}`, {}, {headers})
    .then(response => {
      console.log(response);
      this.loading = false;
      this.getRestaurant(id);
      toast.success("Visit saved!");
    })
    .catch(error => {
      console.log(error);
      this.loading = false;
      toast.success(`Something went wrong! ${error}`);
    });
  }
}

export default new RestaurantStore();
