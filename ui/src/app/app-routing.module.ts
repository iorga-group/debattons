import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';

import {RootReactionsComponent} from "./root-reactions.component";
import {ReactionDetailComponent} from "./reaction-detail.component";
import {NewReactionComponent} from "./new-reaction.component";
import {CreateAccountComponent} from "./create-account.component";

const routes: Routes = [
  {path: '', redirectTo: '/root-reactions', pathMatch: 'full'},
  {path: 'root-reactions', component: RootReactionsComponent},
  {path: 'reaction/:id', component: ReactionDetailComponent},
  {path: 'new-reaction', component: NewReactionComponent},
  {path: 'new-reaction/:reactToReactionId', component: NewReactionComponent},
  {path: 'create-account', component: CreateAccountComponent}
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule {
}
