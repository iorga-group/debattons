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

  @Input()
  reactToReactionId: string;

  constructor(private reactionService: ReactionService) {
  }

  createNewReaction() {
    this.reactionService.createNewReaction(this.reaction, this.reactToReactionId)
      .then(reaction => this.reaction = reaction);
  }
}
