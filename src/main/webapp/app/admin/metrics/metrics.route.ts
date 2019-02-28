import { Route } from '@angular/router';

import { DbtMetricsMonitoringComponent } from './metrics.component';

export const metricsRoute: Route = {
    path: 'dbt-metrics',
    component: DbtMetricsMonitoringComponent,
    data: {
        pageTitle: 'metrics.title'
    }
};
