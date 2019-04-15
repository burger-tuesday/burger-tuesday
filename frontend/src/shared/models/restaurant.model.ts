import {IVisit} from './visit.model';

export interface IRestaurant {
  id?: string;
  placeId?: string;
  name?: string;
  address?: string;
  vicinity?: string;
  url?: string;
  website?: string;
  googleRating?: number;
  priceLevel?: string;
  permanentlyClosed?: boolean;
  visits?: IVisit[];
}
