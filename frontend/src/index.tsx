import * as Sentry from '@sentry/browser';
import * as React from 'react';
import * as ReactDOM from 'react-dom';
import App from './App';
import './index.css';
import registerServiceWorker from './registerServiceWorker';

declare var SENTRY_RELEASE: any;
Sentry.init({ dsn: 'https://ed58b1b25311434aa2c3a346a0755c8e@sentry.io/1448944', environment: process.env.NODE_ENV, release: SENTRY_RELEASE });

ReactDOM.render(
    <App/>,
    document.getElementById('root') as HTMLElement
);
registerServiceWorker();
