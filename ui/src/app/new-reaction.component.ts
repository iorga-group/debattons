import {Component, OnInit} from "@angular/core";
import {Reaction} from "./reaction";

import 'rxjs/add/operator/switchMap';

@Component({
  selector: 'new-reaction',
  templateUrl: './new-reaction.component.html',
})
export class NewReactionComponent {
  reaction: Reaction = new Reaction();
}
