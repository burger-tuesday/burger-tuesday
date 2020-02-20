import { Routes } from '@angular/router';

import { auditsRoute } from './audits/audits.route';
import { configurationRoute } from './configuration/configuration.route';
import { docsRoute } from './docs/docs.route';
import { healthRoute } from './health/health.route';
import { logsRoute } from './logs/logs.route';
import { metricsRoute } from './metrics/metrics.route';
import { trackerRoute } from './tracker/tracker.route';
import {entityRoutes} from './entities/entity.routes';

import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';

const ADMIN_ROUTES = [auditsRoute, configurationRoute, docsRoute, healthRoute, logsRoute, trackerRoute, metricsRoute, ...entityRoutes];

export const adminState: Routes = [
  {
    path: '',
    data: {
      authorities: ['ROLE_ADMIN']
    },
    canActivate: [UserRouteAccessService],
    children: ADMIN_ROUTES
  }
  /* jhipster-needle-add-admin-route - JHipster will add admin routes here */
];
