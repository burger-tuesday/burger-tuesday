import {observer} from 'mobx-react';
import * as React from 'react';
import {toast} from 'react-toastify';
import {IReviewEvent} from '../../shared/models/review.model';
import {CONSTANTS} from '../../stores/constants';

@observer
class LiveReviews extends React.Component<{}, {}> {
  private eventSource: EventSource;

  public componentDidMount() {
    this.eventSource = new EventSource(`${CONSTANTS.API_URL}/reviews/live`);
    this.eventSource.addEventListener('review', e => this.handleEvent(e));
    this.eventSource.addEventListener('closedConnection',
        () => this.eventSource.close());
  }

  public render() {
    return null;
  }

  private handleEvent(event: Event) {
    if (event instanceof MessageEvent) {
      const data: IReviewEvent = JSON.parse(event.data);
      toast.info(`${data.author} just reviewed our visit to ${data.visitName} on ${data.visitDate}. He would ${data.recommended ? '' : 'not '} recommend it.`, {position: 'bottom-left'});
    }
  }
}

export default LiveReviews
