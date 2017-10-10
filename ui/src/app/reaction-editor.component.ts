import {Component, Input} from '@angular/core';
import {ReactionService} from "./reaction.service";
import {Reaction} from "./reaction";

@Component({
  selector: 'reaction-editor',
  templateUrl: './reaction-editor.component.html',
})
export class ReactionEditorComponent {
  @Input()
  reaction: Reaction;

  constructor(private reactionService: ReactionService) {
  }

  createNewReaction() {
    this.reactionService.createNewReaction(this.reaction)
      .then(reaction => this.reaction = reaction);
  }
}
