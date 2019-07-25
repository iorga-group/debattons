import { NgModule } from '@angular/core';

import { DebattonsSharedLibsModule, FindLanguageFromKeyPipe, DbtAlertComponent, DbtAlertErrorComponent } from './';

@NgModule({
  imports: [DebattonsSharedLibsModule],
  declarations: [FindLanguageFromKeyPipe, DbtAlertComponent, DbtAlertErrorComponent],
  exports: [DebattonsSharedLibsModule, FindLanguageFromKeyPipe, DbtAlertComponent, DbtAlertErrorComponent]
})
export class DebattonsSharedCommonModule {}
