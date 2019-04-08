import {observer} from 'mobx-react';
import * as React from 'react';
import {Container} from 'reactstrap';
import authStore from '../../stores/authStore';

@observer
export default class Home extends React.Component {
  public render() {
    if (authStore.isAuthenticated) {
      return (
          <Container>
            <h1>Welcome</h1>
            Welcome to the WIP Burger Tuesday website, {authStore.userProfile != null ? authStore.userProfile.name : '' }.
            ID token: {authStore.idToken}
            Access token: {authStore.accessToken}
          </Container>
      )
    }
    return (
        <Container>
          <h1>Welcome</h1>
          Welcome to the WIP Burger Tuesday website. Please <a style={{ cursor: 'pointer' }} onClick={() => authStore.login()}>sign in</a> to continue.
        </Container>
    );
  }
}
