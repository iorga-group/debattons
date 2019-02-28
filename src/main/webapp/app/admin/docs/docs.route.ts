import { Route } from '@angular/router';

import { DbtDocsComponent } from './docs.component';

export const docsRoute: Route = {
    path: 'docs',
    component: DbtDocsComponent,
    data: {
        pageTitle: 'global.menu.admin.apidocs'
    }
};
