import {BrowserModule} from '@angular/platform-browser';
import {NgModule} from '@angular/core';

import {HttpModule} from '@angular/http';
import {HttpClientModule} from '@angular/common/http';

import {AppComponent} from './app.component';
import {ReactionService} from "./reaction.service";
import {FormsModule} from "@angular/forms";

import {ReactionEditorComponent} from "./reaction-editor.component";
import {AppRoutingModule} from "./app-routing.module";
import {ReactionDetailComponent} from "./reaction-detail.component";
import {ReactionListComponent} from "./reaction-list.component";
import {RootReactionsComponent} from "./root-reactions.component";
import {NewReactionComponent} from "./new-reaction.component";
import {VisModule} from "ngx-vis";

import {EditorModule} from 'primeng/primeng';
import {BrowserAnimationsModule} from '@angular/platform-browser/animations';
import {CreateAccountComponent} from "./create-account.component";
import {UIMessagesComponent} from "./ui-messages.component";
import {UIMessageService} from "./ui-message.service";
import {UserService} from "./user.service";


@NgModule({
  declarations: [
    AppComponent,
    ReactionDetailComponent,
    ReactionEditorComponent,
    ReactionListComponent,
    RootReactionsComponent,
    NewReactionComponent,
    CreateAccountComponent,
    UIMessagesComponent,
  ],
  imports: [
    BrowserModule,
    BrowserAnimationsModule,
    EditorModule,
    HttpClientModule,
    HttpModule,
    FormsModule,
    AppRoutingModule,
    VisModule,
  ],
  providers: [
    ReactionService,
    UIMessageService,
    UserService,
  ],
  bootstrap: [AppComponent]
})
export class AppModule {
}
