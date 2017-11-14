import {Component, OnInit} from "@angular/core";
import {Reaction} from "./reaction";

import 'rxjs/add/operator/switchMap';
import {ActivatedRoute, ParamMap} from "@angular/router";

@Component({
  selector: 'new-reaction',
  templateUrl: './new-reaction.component.html',
})
export class NewReactionComponent implements OnInit {
  reaction: Reaction = new Reaction();
  reactToReactionId: string;

  constructor(
    private route: ActivatedRoute
  ) {}

  ngOnInit(): void {
    this.route.paramMap
      .subscribe((params: ParamMap) => this.reactToReactionId = params.get('reactToReactionId'));
  }
}
