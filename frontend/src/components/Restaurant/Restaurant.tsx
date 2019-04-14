import {observer} from 'mobx-react';
import * as React from 'react';
import {ReactNode} from 'react';
import {Container} from 'reactstrap';
import {withAuthorization} from '../../shared/auth/session-renewing-component';
import authStore from '../../stores/authStore';
import AddRestaurant from './AddRestaurant';
import RestaurantList from './RestaurantList';

@observer
class Restaurant extends React.Component<{}, {}> {
  public render() {
    return (
        <Container fluid={true}>
          {this.addComponentIfAuthenticated()}
          <RestaurantList />
        </Container>
    );
  }

  private addComponentIfAuthenticated(): ReactNode | null {
    if (authStore.isAuthenticated && authStore.isAuthorized('manage:restaurants')) {
      return (<AddRestaurant />);
    }
    return null;
  }
}

export default withAuthorization(Restaurant);
