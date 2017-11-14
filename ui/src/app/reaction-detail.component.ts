import {Component, OnInit} from "@angular/core";
import {Reaction} from "./reaction";
import {ReactionService} from "./reaction.service";
import {ActivatedRoute, ParamMap, Router} from "@angular/router";

import 'rxjs/add/operator/switchMap';

@Component({
  selector: 'reaction-detail',
  templateUrl: './reaction-detail.component.html',
})
export class ReactionDetailComponent implements OnInit {
  reaction: Reaction;

  constructor(private reactionService: ReactionService,
              private route: ActivatedRoute,
              private router: Router) {
  }

  ngOnInit(): void {
    this.route.paramMap
      .switchMap((params: ParamMap) => this.reactionService.findByIdLoadingDepth(params.get('id'), 1))
      .subscribe(reaction => this.reaction = reaction);
  }

  reactToThis() {
    this.router.navigate(['/new-reaction/', this.reaction.id]);
  }
}
