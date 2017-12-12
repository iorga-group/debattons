import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';

import {HttpModule} from '@angular/http';

import {AppComponent} from './app.component';
import {ReactionService} from "./reaction.service";
import {FormsModule} from "@angular/forms";

import {ReactionEditorComponent} from "./reaction-editor.component";
import {AppRoutingModule} from "./app-routing.module";
import {ReactionDetailComponent} from "./reaction-detail.component";
import {ReactionListComponent} from "./reaction-list.component";
import {RootReactionsComponent} from "./root-reactions.component";
import {NewReactionComponent} from "./new-reaction.component";

import {EditorModule} from 'primeng/primeng';
import {BrowserAnimationsModule} from '@angular/platform-browser/animations';


@NgModule({
  declarations: [
    AppComponent,
    ReactionDetailComponent,
    ReactionEditorComponent,
    ReactionListComponent,
    RootReactionsComponent,
    NewReactionComponent
  ],
  imports: [
    BrowserModule,
    BrowserAnimationsModule,
    EditorModule,
    HttpModule,
    FormsModule,
    AppRoutingModule
  ],
  providers: [
    ReactionService
  ],
  bootstrap: [AppComponent]
})
export class AppModule {
}
