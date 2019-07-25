import { Route } from '@angular/router';

import { DbtHealthCheckComponent } from './health.component';

export const healthRoute: Route = {
  path: 'dbt-health',
  component: DbtHealthCheckComponent,
  data: {
    pageTitle: 'health.title'
  }
};
