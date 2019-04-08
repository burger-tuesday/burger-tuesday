import * as React from 'react';
import {Route, RouteComponentProps, Router} from 'react-router';
import App from './App';
import Callback from './components/Callback/Callback';
import Home from './components/Home/Home';
import Profile from './components/Profile/Profile';
import history from './history';
import authStore from './stores/authStore';


const handleAuthentication = (props:  RouteComponentProps) => {
  if (/access_token|id_token|error/.test(props.location.hash)) {
    authStore.handleAuthentication();
  }
}

export const makeMainRoutes = () => {
  return (
      <Router history={history}>
        <div>
          <Route path="/" component={App} />
          <Route path="/home" component={Home}/>
          <Route path="/profile" component={Profile}/>

          <Route path="/callback" render={(props: RouteComponentProps) => {
            handleAuthentication(props);
            return <Callback {...props} />
          }}/>
        </div>
      </Router>
  );
}
