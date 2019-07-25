import { Route } from '@angular/router';

import { DbtConfigurationComponent } from './configuration.component';

export const configurationRoute: Route = {
  path: 'dbt-configuration',
  component: DbtConfigurationComponent,
  data: {
    pageTitle: 'configuration.title'
  }
};
