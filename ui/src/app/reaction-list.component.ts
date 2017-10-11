import {Component, Input} from '@angular/core';
import {Reaction} from "./reaction";
import {Router} from "@angular/router";

@Component({
  selector: 'reaction-list',
  templateUrl: './reaction-list.component.html',
})
export class ReactionListComponent {
  @Input()
  reactions: Reaction[];

  constructor(private router: Router) {}

  onSelect(reaction: Reaction) {
    this.router.navigate(['/reaction', reaction.id])
  }
}
