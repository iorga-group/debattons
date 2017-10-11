import {Component, OnInit} from '@angular/core';
import {Reaction} from "./reaction";
import {ReactionService} from "./reaction.service";

@Component({
  selector: 'root-reactions',
  templateUrl: './root-reactions.component.html',
})
export class RootReactionsComponent implements OnInit {
  reactions: Reaction[];

  constructor(private reactionService: ReactionService) {
  }

  ngOnInit(): void {
    this.reactionService.findRoot()
      .then(reactions => this.reactions = reactions);
  }
}
