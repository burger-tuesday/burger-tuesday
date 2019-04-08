import * as React from 'react';
import * as ReactDOM from 'react-dom';
import {Router} from 'react-router';
import App from './App';
import history from './history';

it('renders without crashing', () => {
  const div = document.createElement('div');
  ReactDOM.render(<Router history={history}><App/></Router>, div);
  ReactDOM.unmountComponentAtNode(div);
});
