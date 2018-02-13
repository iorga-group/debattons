import {Component, OnInit} from '@angular/core';
import {Reaction} from "./reaction";
import {ReactionService} from "./reaction.service";
import {Observable} from "rxjs/Observable";

@Component({
  selector: 'root-reactions',
  templateUrl: './root-reactions.component.html',
})
export class RootReactionsComponent implements OnInit {
  reactions$: Observable<Reaction[]>;

  constructor(private reactionService: ReactionService) {
  }

  ngOnInit(): void {
    this.reactions$ = this.reactionService.findRoot();
  }
}
