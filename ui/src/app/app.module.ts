import {BrowserModule} from '@angular/platform-browser';
import {NgModule} from '@angular/core';

import {HttpModule} from '@angular/http';

import {AppComponent} from './app.component';
import {ReactionService} from "./reaction.service";

@NgModule({
  declarations: [
    AppComponent
  ],
  imports: [
    BrowserModule,
    HttpModule
  ],
  providers: [
    ReactionService
  ],
  bootstrap: [AppComponent]
})
export class AppModule {
}
