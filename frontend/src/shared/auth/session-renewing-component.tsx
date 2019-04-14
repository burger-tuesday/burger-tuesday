import {observer} from 'mobx-react';
import * as React from 'react';
import {Container} from 'reactstrap';
import authStore from '../../stores/authStore';
import commonStore from '../../stores/commonStore';

export function withAuthorization<T>(WrappedComponent: React.ComponentType<T>) {
  @observer
  class SessionRenewingComponent extends React.Component {
    public componentDidMount(): void {
      if (commonStore.isLoggedIn) {
        authStore.renewSession();
      }
    }

    public render() {
      if (authStore.isAuthenticated) {
        return <WrappedComponent {...this.props as T} />;
      }
      return (
          <Container>
            <h1>Forbidden</h1>
            You must be <a style={{cursor: 'pointer'}} onClick={() => authStore.login()}>signed
            in</a> to continue.
          </Container>
      )
    }
  }

  return SessionRenewingComponent;
}

