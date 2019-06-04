import * as _ from 'lodash';
import {observer} from 'mobx-react';
import * as React from 'react';
import {Link} from 'react-router-dom';
import ReactTable from 'react-table';
import 'react-table/react-table.css';
import {IReview} from '../../shared/models/review.model';
import restaurantStore from '../../stores/restaurantStore';

@observer
class RestaurantList extends React.Component<{}, {}> {
  public componentDidMount(): void {
    this.fetchData();
  }

  public render() {
    return (
        <ReactTable
            columns={[
              {
                Header: 'Name',
                accessor: 'name',
                Cell: row => <Link to={`/restaurant/${row.original.id}`}>{row.value}</Link>
              },
              {
                Header: 'Visits',
                id: 'visits',
                accessor: data => data.visits ? data.visits.map((v) => v.date).join(', ') : 'No visits'
              },
              {
                Header: 'Address',
                accessor: 'vicinity'
              },
              {
                Header: 'Google Rating',
                accessor: 'googleRating'
              },
              {
                Header: 'BT Rating',
                id: 'btRating',
                accessor: data => {
                  const reviews: IReview[] = _.flatMap(data.visits, v => v.reviews ? v.reviews : []);
                  if (_.isEmpty(reviews)) {
                    return null;
                  }
                  const taste = _.mean(reviews.map(r => r.taste)) / 2 * 3;
                  const likeness = _.mean(reviews.map(r => r.likeness)) * 3;
                  const menuDiversity = _.mean(reviews.map(r => r.menuDiversity));
                  const service = _.mean(reviews.map(r => r.service)) * 2;
                  const recommended = _.mean(reviews.map(r => r.recommended)) * 5 / 3;
                  const rating = (taste + likeness + menuDiversity + service + recommended) / 5;
                  return `${_.round(((((rating - (1 + 1 / 2)) / ((9 + 1 / 3) - (1 + 1 / 2))) * 4) + 1), 2)}/5`;

                  /*
                  const reviews: IReview[] = _.flatMap(data.visits, v => v.reviews ? v.reviews : []);
                  const taste = (((_.mean(reviews.map(r => r.taste))-1)/9)*4)+1;
                  const likeness = _.mean(reviews.map(r => r.likeness));
                  const service = _.mean(reviews.map(r => r.service)) * 2;
                  const recommended = (_.mean(reviews.map(r => r.recommended))*4)+1;
                  const rating = (taste + likeness + service + recommended) / 4;
                  return rating.toString();
                   */
                }
              }
            ]}
            manual={true} // Forces table not to paginate or sort automatically, so we can handle it server-side
            data={restaurantStore.restaurants}
            pages={restaurantStore.pages} // Display the total number of pages
            loading={restaurantStore.loading} // Display the loading overlay when we need it
            pageSize={restaurantStore.pageSize}
            sortable={false}
            onFetchData={() => this.fetchData} // Request new data when things change
            className='-highlight'
            onPageChange={(p) => {
              restaurantStore.page = p;
              this.fetchData();
            }}
            onPageSizeChange={(ps, p) => {
              restaurantStore.pageSize = ps;
              restaurantStore.page = p;
              this.fetchData();
            }}
        />
    );
  }

  private fetchData() {
    restaurantStore.getRestaurants(restaurantStore.page, restaurantStore.pageSize);
  }
}

export default RestaurantList;
