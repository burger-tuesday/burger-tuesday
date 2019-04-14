import * as React from 'react';
import {Router} from 'react-router';
import './App.css';
import NavigationMenu from './components/Navigation/Navigation';
import history from './history';
import {AppRoutes} from './routes';
import ErrorBoundary from './shared/error/error-boundary';

export default class App extends React.Component<{}, {}> {
  public render() {
    return (
        <Router history={history}>
          <div>
            <NavigationMenu/>
            <ErrorBoundary>
              <AppRoutes/>
            </ErrorBoundary>
          </div>
        </Router>
    );
  }
}
