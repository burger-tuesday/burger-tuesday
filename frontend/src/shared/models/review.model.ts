import {IVisit} from './visit.model';

export interface IReview {
  id?: number;
  review?: string;
  createdByName?: string;
  visit?: IVisit;
}
