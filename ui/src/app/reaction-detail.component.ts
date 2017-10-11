import {Component, OnInit} from "@angular/core";
import {Reaction} from "./reaction";
import {ReactionService} from "./reaction.service";
import {ActivatedRoute, ParamMap} from "@angular/router";
import {Location} from "@angular/common";

import 'rxjs/add/operator/switchMap';

@Component({
  selector: 'reaction-detail',
  templateUrl: './reaction-detail.component.html',
})
export class ReactionDetailComponent implements OnInit {
  reaction: Reaction;

  constructor(
    private reactionService: ReactionService,
    private route: ActivatedRoute,
    private location: Location
  ) {}

  ngOnInit(): void {
    this.route.paramMap
      .switchMap((params: ParamMap) => this.reactionService.findWithId(params.get('id')))
      .subscribe(reaction => this.reaction = reaction);
  }
}
