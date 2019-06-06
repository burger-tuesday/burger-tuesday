import * as React from 'react';
import {Route, RouteComponentProps, Switch} from 'react-router';
import Callback from './components/Callback/Callback';
import Home from './components/Home/Home';
import Profile from './components/Profile/Profile';
import Restaurant from './components/Restaurant/Restaurant';
import RestaurantDetails from './components/Restaurant/RestaurantDetails';
import Review from './components/Review/Review';
import Toplist from './components/Toplist/Toplist';
import ErrorBoundaryRoute from './shared/error/error-boundary-route';
import authStore from './stores/authStore';


const handleAuthentication = (props: RouteComponentProps) => {
  if (/access_token|id_token|error/.test(props.location.hash)) {
    authStore.handleAuthentication();
  }
};

export const AppRoutes = () => {
  return (
      <div>
        <Switch>
          <ErrorBoundaryRoute path='/home' component={Home}/>
          <ErrorBoundaryRoute path='/profile' component={Profile}/>
          <ErrorBoundaryRoute path='/restaurants' component={Restaurant}/>
          <ErrorBoundaryRoute path='/restaurant/:id' component={RestaurantDetails}/>
          <ErrorBoundaryRoute path='/review' component={Review}/>
          <ErrorBoundaryRoute path='/toplist' component={Toplist}/>

          <Route path='/callback' render={(props: RouteComponentProps) => {
            handleAuthentication(props);
            return <Callback {...props} />
          }}/>
          <ErrorBoundaryRoute path='/' component={Home}/>
        </Switch>
      </div>
  );
};
