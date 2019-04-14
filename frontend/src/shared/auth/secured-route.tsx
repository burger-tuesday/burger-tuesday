import * as React from 'react';
import {Route, RouteProps} from 'react-router';
import {Container} from 'reactstrap';
import authStore from '../../stores/authStore';
import ErrorBoundary from '../error/error-boundary';


const SecuredRoute = (props: RouteProps) => {
  const {component: Component, ...rest} = props;

  if (!Component) {
    throw new Error(`A component needs to be specified for path ${(rest as any).path}`);
  }
  return (
      <Route
          {...rest}
          render={(routeProps) => {
            if (authStore.isAuthenticated) {
              return (
                  <ErrorBoundary>
                    <Component {...routeProps} />
                  </ErrorBoundary>
              )
            } else {
              return (
                  <Container>
                    <h1>Forbidden</h1>
                    You must be <a style={{cursor: 'pointer'}} onClick={() => authStore.login()}>signed
                    in</a> to continue.
                  </Container>
              )
            }
          }
          }
      />
  );
};

export default SecuredRoute;
