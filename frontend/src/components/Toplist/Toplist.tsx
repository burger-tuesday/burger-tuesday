import {observer} from 'mobx-react';
import * as React from 'react';
import ReactTable from 'react-table';
import {Container, Jumbotron} from 'reactstrap';
import {withAuthorization} from '../../shared/auth/session-renewing-component';
import toplistStore from '../../stores/toplistStore';

@observer
class RestaurantDetails extends React.Component<{}, {}> {
  public componentDidMount(): void {
    toplistStore.getToplist();
  }

  public render() {
    return (
        <Container>
          <Jumbotron>
            <ReactTable
                columns={[
                  {
                    Header: 'Rank',
                    accessor: 'rank',
                  },
                  {
                    Header: '# Reviews',
                    accessor: 'amount',
                  },
                  {
                    Header: 'Reviewer',
                    accessor: 'reviewerName',
                  },
                ]}
                data={toplistStore.entries}
                loading={toplistStore.loading}
                className='-highlight'
                defaultPageSize={10}
            />
          </Jumbotron>
        </Container>
    );
  }
}

export default withAuthorization(RestaurantDetails);
