import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { DebattonsSharedCommonModule, DbtLoginModalComponent, HasAnyAuthorityDirective } from './';

@NgModule({
  imports: [DebattonsSharedCommonModule],
  declarations: [DbtLoginModalComponent, HasAnyAuthorityDirective],
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
