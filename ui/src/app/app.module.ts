import {BrowserModule} from '@angular/platform-browser';
import {NgModule} from '@angular/core';

import { HTTP_INTERCEPTORS, HttpClientModule } from '@angular/common/http';

import {AppComponent} from './app.component';
import {ReactionService} from "./reaction.service";
import {FormsModule, ReactiveFormsModule} from "@angular/forms";

import {ReactionEditorComponent} from "./reaction-editor.component";
import {AppRoutingModule} from "./app-routing.module";
import {ReactionDetailComponent} from "./reaction-detail.component";
import {ReactionListComponent} from "./reaction-list.component";
import {RootReactionsComponent} from "./root-reactions.component";
import {NewReactionComponent} from "./new-reaction.component";
import {VisModule} from "ngx-vis";
import { MatIconModule } from "@angular/material/icon";

import {EditorModule} from 'primeng/primeng';
import {BrowserAnimationsModule} from '@angular/platform-browser/animations';
import {CreateAccountComponent} from "./user/create-account.component";
import {UIMessagesComponent} from "./ui-messages.component";
import {UIMessageService} from "./ui-message.service";
import {UserService} from "./user/user.service";
import { LoginComponent } from './user/login.component';
import { UnauthorizedInterceptor } from './user/unauthorized.interceptor';

import {MatInputModule} from '@angular/material/input';
import {MatButtonModule} from '@angular/material/button';
import {MatCardModule} from '@angular/material/card';

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
    LoginComponent,
  ],
  imports: [
    BrowserModule,
    BrowserAnimationsModule,
    EditorModule, 
    HttpClientModule,
    FormsModule,
    ReactiveFormsModule,
    AppRoutingModule,
    VisModule,
    MatInputModule,
    MatButtonModule,
    MatCardModule,
    MatIconModule
  ],
  providers: [
    ReactionService,
    UIMessageService,
    UserService,
    {
      provide: HTTP_INTERCEPTORS,
      useClass: UnauthorizedInterceptor,
      multi: true
    }
  ],
  bootstrap: [AppComponent]
})
export class AppModule {
}
