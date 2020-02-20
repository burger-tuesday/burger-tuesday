import { IVisit } from 'app/shared/model/visit.model';

export interface IRestaurant {
  id?: number;
  placeId?: string;
  name?: string;
  address?: string;
  vicinity?: string;
  url?: string;
  website?: string;
  googleRating?: number;
  btRating?: number;
  numberOfReviews?: number;
  priceLevel?: string;
  permanentlyClosed?: boolean;
  userId?: number;
  visits?: IVisit[];
}

export class Restaurant implements IRestaurant {
  constructor(
    public id?: number,
    public placeId?: string,
    public name?: string,
    public address?: string,
    public vicinity?: string,
    public url?: string,
    public website?: string,
    public googleRating?: number,
    public btRating?: number,
    public numberOfReviews?: number,
    public priceLevel?: string,
    public permanentlyClosed?: boolean,
    public userId?: number,
    public visits?: IVisit[]
  ) {
    this.permanentlyClosed = this.permanentlyClosed || false;
  }
}
