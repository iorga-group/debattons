import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';

import {RootReactionsComponent} from "./root-reactions.component";
import {ReactionDetailComponent} from "./reaction-detail.component";
import {NewReactionComponent} from "./new-reaction.component";

const routes: Routes = [
  {path: '', redirectTo: '/root-reactions', pathMatch: 'full'},
  {path: 'root-reactions', component: RootReactionsComponent},
  {path: 'reaction/:id', component: ReactionDetailComponent},
  {path: 'new-reaction', component: NewReactionComponent},
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule {
}
