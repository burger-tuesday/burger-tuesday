import Rating from 'react-rating';

export default class FixedRating extends Rating {
  componentWillReceiveProps(nextProps, nextContext) {
    // this is empty to avoid resetting of the ratings when new props are received
  }
}
