import * as React from 'react';
import {Router} from 'react-router';
import {ToastContainer} from 'react-toastify';
import 'react-toastify/dist/ReactToastify.css';
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
              <ToastContainer/>
            </ErrorBoundary>
          </div>
        </Router>
    );
  }
}
