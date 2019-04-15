import axios from 'axios';
import {action, computed, observable} from 'mobx';
import {toast} from 'react-toastify';
import {IReview} from '../shared/models/review.model';
import {IVisit} from '../shared/models/visit.model';
import authStore from './authStore';
import {CONSTANTS} from './constants';

interface ISelectOptions {
  label: string
  value: string | undefined
}

class ReviewStore {
  @observable public visits: IVisit[] = [];
  @observable public selectedVisit: ISelectOptions | ISelectOptions[] | undefined = undefined;
  @observable public taste: number = 0;
  @observable public likeness: number = 0;
  @observable public menuDiversity: number = 0;
  @observable public service: number = 0;
  @observable public priceLevel: number = 0;
  @observable public recommended: boolean = false;
  @observable public text: string | null = null;

  @computed
  public get visitOptions(): ISelectOptions[] {
    return this.visits.map(value => {
      return {
        label: `${value.date} - ${value.restaurant ? value.restaurant.name : 'name missing'}`,
        value: value.id
      }
    })
  }

  @action
  public getVisits() {
    const headers = {'Authorization': `Bearer ${authStore.accessToken}`}
    axios.get<IVisit[]>(`${CONSTANTS.API_URL}/visits?sort=date,desc`, {headers})
    .then(response => {
      console.log(response);
      this.visits = response.data;
    })
    .catch(error => {
      console.log(error);
      this.visits = [];
    });
  }

  @action
  public saveReview() {
    if (!this.selectedVisit) {
      toast('Visit must be selected!', {type: 'error'});
      return;
    }
    const headers = {'Authorization': `Bearer ${authStore.accessToken}`}
    axios.post<IReview>(`${CONSTANTS.API_URL}/visit/${(this.selectedVisit as ISelectOptions).value}/review`, {
      taste: this.taste,
      likeness: this.likeness,
      menuDiversity: this.menuDiversity,
      service: this.service,
      priceLevel: this.priceLevel,
      recommended: this.recommended,
      text: this.text
    } as IReview, {headers})
    .then(response => {
      console.log(response);
      toast('Review successfully saved!', {type: 'success'});
    })
    .catch(error => {
      console.log(error);
      toast('Something went wrong while saving!', {type: 'error'});
    });
  }
}

export default new ReviewStore();
