import {observer} from 'mobx-react';
import * as React from 'react';
import ReactTable from 'react-table';
import 'react-table/react-table.css';
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
                Header: "Name",
                accessor: "name"
              },
              {
                Header: "Visits",
                id: "visits",
                accessor: (data) => data.visits ? data.visits.map((v) => v.date).join(', ') : 'No visits'
              },
              {
                Header: "Address",
                accessor: "vicinity"
              },
              {
                Header: "Google Rating",
                accessor: "googleRating"
              }
            ]}
            manual={true} // Forces table not to paginate or sort automatically, so we can handle it server-side
            data={restaurantStore.restaurants}
            pages={restaurantStore.pages} // Display the total number of pages
            loading={restaurantStore.loading} // Display the loading overlay when we need it
            pageSize={restaurantStore.pageSize}
            sortable={false}
            onFetchData={() => this.fetchData} // Request new data when things change
            className="-highlight"
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
