import Rating, {RatingComponentProps} from 'react-rating';

export default class FixedRating extends Rating {
  public componentWillReceiveProps(nextProps: Readonly<RatingComponentProps>, nextContext: any): void {
    // this is empty to avoid resetting of the ratings when new props are received
  }
}
