import {IRestaurant} from './restaurant.model';
import {IReview} from './review.model';

export interface IVisit {
  id?: number;
  date?: string;
  sponsored: boolean;
  restaurant?: IRestaurant;
  reviews?: IReview[];
}
