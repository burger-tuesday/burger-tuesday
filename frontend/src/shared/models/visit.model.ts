import {IRestaurant} from './restaurant.model';
import {IReview} from './review.model';

export interface IVisit {
  id?: string;
  date?: string;
  sponsored: boolean;
  restaurant?: IRestaurant;
  reviews?: IReview[];
}
