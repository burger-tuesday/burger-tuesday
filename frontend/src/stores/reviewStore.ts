import axios from 'axios';
import {FieldState, FormState} from 'formstate';
import {action, computed, observable} from 'mobx';
import {toast} from 'react-toastify';
import {IReview} from '../shared/models/review.model';
import {IVisit} from '../shared/models/visit.model';
import authStore from './authStore';
import {CONSTANTS} from './constants';

export interface ISelectOptions {
  label: string
  value: string
}

class ReviewStore {
  @observable public visits: IVisit[] = [];
  @observable public formState = new FormState({
    selectedVisit: new FieldState<ISelectOptions | undefined>(undefined).validators(this.requiredVisit),
    taste: new FieldState(0).validators(this.requiredReview('Taste')),
    likeness: new FieldState(0).validators(this.requiredReview('Likeness')),
    menuDiversity: new FieldState(0).validators(this.requiredReview('Menu Diversity')),
    service: new FieldState(0).validators(this.requiredReview('Service')),
    priceLevel: new FieldState(0).validators(this.requiredReview('Price Level')),
    recommended: new FieldState<boolean | null>(null).validators(this.requiredRecommendation),
    text: new FieldState<string | undefined>(undefined)
  });

  @computed
  public get visitOptions(): ISelectOptions[] {
    return this.visits.map(value => {
      return {
        label: `${value.date} - ${value.restaurant ? value.restaurant.name : 'name missing'}`,
        value: value.id!
      }
    })
  }

  @action
  public getVisits() {
    const headers = {'Authorization': `Bearer ${authStore.accessToken}`};
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
  public async saveReview() {
    await this.formState.validate();
    if (this.formState.error) {
      toast(this.formState.error, {type: 'error'});
      return;
    }
    const headers = {'Authorization': `Bearer ${authStore.accessToken}`};
    axios.post<IReview>(`${CONSTANTS.API_URL}/visit/${(this.formState.$.selectedVisit.$ as ISelectOptions).value}/review`, {
      taste: this.formState.$.taste.$,
      likeness: this.formState.$.likeness.$,
      menuDiversity: this.formState.$.menuDiversity.$,
      service: this.formState.$.service.$,
      priceLevel: this.formState.$.priceLevel.$,
      recommended: this.formState.$.recommended.$,
      text: this.formState.$.text.$
    } as IReview, {headers})
    .then(response => {
      console.log(response);
      toast('Review successfully saved!', {type: 'success'});
      this.formState.reset();
    })
    .catch(error => {
      if (error.response && error.response.data && error.response.data.message) {
        toast(error.response.data.message, {type: 'error'});
      } else {
        toast('Something went wrong while saving!', {type: 'error'});
      }
      throw error;
    });
  }

  private requiredVisit(val: ISelectOptions | ISelectOptions[] | undefined): string {
    if (!val) {
      return 'You must select a visit!'
    }
    return '';
  }

  private requiredRecommendation(val: boolean | null): string {
    if (!val) {
      return 'You must select a recommendation!'
    }
    return '';
  }

  private requiredReview(name: string): ((v: number) => string) {
    return (v: number) => {
      if (v === 0) {
        return `Value for ${name} required!`;
      }
      return '';
    }
  }
}

export default new ReviewStore();
