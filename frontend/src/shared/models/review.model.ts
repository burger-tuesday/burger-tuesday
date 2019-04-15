import {IVisit} from './visit.model';

export interface IReview {
  id?: string;
  taste: number;
  likeness: number;
  menuDiversity: number;
  service: number;
  priceLevel: number;
  recommended: boolean;
  text?: string;
  createdByName?: string;
  visit?: IVisit;
}
