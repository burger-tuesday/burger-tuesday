export interface IReview {
  id?: number;
  review?: string;
  taste?: number;
  likeness?: number;
  menuDiversity?: number;
  service?: number;
  priceLevel?: number;
  recommended?: boolean;
  visitId?: number;
  userId?: number;
}

export class Review implements IReview {
  constructor(
    public id?: number,
    public review?: string,
    public taste?: number,
    public likeness?: number,
    public menuDiversity?: number,
    public service?: number,
    public priceLevel?: number,
    public recommended?: boolean,
    public visitId?: number,
    public userId?: number
  ) {
    this.recommended = this.recommended || false;
  }
}
