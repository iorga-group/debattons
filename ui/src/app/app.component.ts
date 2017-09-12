import {Component} from '@angular/core';
import {ReactionService} from "./reaction.service";
import {Reaction} from "./reaction";

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent {
  title = 'Débattons !';
  reaction: Reaction = null;

  constructor(private reactionService: ReactionService) {
  }

  createNewReaction() {
    let reaction = new Reaction();
    reaction.title = 'Test from app.component.ts';
    reaction.content = 'Test from app.component.ts content lorem';
    this.reactionService.createNewReaction(reaction)
      .then(reaction => this.reaction = reaction);
  }
}
