import {BrowserModule} from '@angular/platform-browser';
import {NgModule} from '@angular/core';

import {HttpModule} from '@angular/http';

import {AppComponent} from './app.component';
import {ReactionService} from "./reaction.service";
import {FormsModule} from "@angular/forms";

import {LMarkdownEditorModule} from 'ngx-markdown-editor';
import {ReactionEditorComponent} from "./reaction-editor.component";

@NgModule({
  declarations: [
    AppComponent,
    ReactionEditorComponent
  ],
  imports: [
    BrowserModule,
    HttpModule,
    FormsModule,
    LMarkdownEditorModule
  ],
  providers: [
    ReactionService
  ],
  bootstrap: [AppComponent]
})
export class AppModule {
}
