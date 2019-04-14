import {observer} from 'mobx-react';
import * as React from 'react';
import {Container} from 'reactstrap';
import {withAuthorization} from '../../shared/auth/session-renewing-component';
import authStore from '../../stores/authStore';

@observer
class Home extends React.Component {
  public render() {
    if (authStore.isAuthenticated) {
      return (
          <Container>
            <h1>Welcome</h1>
            Welcome to the WIP Burger Tuesday
            website, {authStore.userProfile != null ? authStore.userProfile.name : ''}.<br/>
            <pre>
              id_token: {authStore.idToken}<br/>
              access_token: {authStore.accessToken}<br/>
              scope: {authStore.scope}
            </pre>
          </Container>
      )
    }
    return (
        <Container>
          <h1>Welcome</h1>
          Welcome to the WIP Burger Tuesday website. Please <a style={{cursor: 'pointer'}}
                                                               onClick={() => authStore.login()}>sign
          in</a> to continue.
        </Container>
    );
  }
}

export default withAuthorization(Home);
