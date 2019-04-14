import * as React from 'react';
import {Route, RouteProps,} from 'react-router-dom';
import ErrorBoundary from './error-boundary';

const ErrorBoundaryRoute = (props: RouteProps) => {
  const {component: Component, ...rest} = props;

  if (!Component) {
    throw new Error(`A component needs to be specified for path ${(rest as any).path}`);
  }
  return (
      <Route
          {...rest}
          render={(routeProps) =>
              (<ErrorBoundary>
                <Component {...routeProps} />
              </ErrorBoundary>)
          }
      />
  );
};

export default ErrorBoundaryRoute;
