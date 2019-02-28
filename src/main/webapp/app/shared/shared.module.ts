import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { NgbDateAdapter } from '@ng-bootstrap/ng-bootstrap';

import { NgbDateMomentAdapter } from './util/datepicker-adapter';
import { DebattonsSharedLibsModule, DebattonsSharedCommonModule, DbtLoginModalComponent, HasAnyAuthorityDirective } from './';

@NgModule({
    imports: [DebattonsSharedLibsModule, DebattonsSharedCommonModule],
    declarations: [DbtLoginModalComponent, HasAnyAuthorityDirective],
    providers: [{ provide: NgbDateAdapter, useClass: NgbDateMomentAdapter }],
    entryComponents: [DbtLoginModalComponent],
    exports: [DebattonsSharedCommonModule, DbtLoginModalComponent, HasAnyAuthorityDirective],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class DebattonsSharedModule {
    static forRoot() {
        return {
            ngModule: DebattonsSharedModule
        };
    }
}
