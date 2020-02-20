import { Moment } from 'moment';
import { IReview } from 'app/shared/model/review.model';

export interface IVisit {
  id?: number;
  date?: Moment;
  sponsored?: boolean;
  restaurantId?: number;
  userId?: number;
  reviews?: IReview[];
}

export class Visit implements IVisit {
  constructor(
    public id?: number,
    public date?: Moment,
    public sponsored?: boolean,
    public restaurantId?: number,
    public userId?: number,
    public reviews?: IReview[]
  ) {
    this.sponsored = this.sponsored || false;
  }
}
