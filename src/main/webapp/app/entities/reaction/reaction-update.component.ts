import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { JhiAlertService } from 'ng-jhipster';
import { IReaction, Reaction } from 'app/shared/model/reaction.model';
import { ReactionService } from './reaction.service';
import { IUser, UserService } from 'app/core';

@Component({
  selector: 'dbt-reaction-update',
  templateUrl: './reaction-update.component.html'
})
export class ReactionUpdateComponent implements OnInit {
  isSaving: boolean;

  parentReaction: Reaction;

  editForm = this.fb.group({
    id: [],
    title: [null, [Validators.required, Validators.maxLength(280)]],
    content: [],
    type: [null, [Validators.required]],
    typeLevel: [null, [Validators.min(1), Validators.max(120)]],
    supportScore: [],
    creator: [],
    parentReaction: []
  });

  constructor(
    protected jhiAlertService: JhiAlertService,
    protected reactionService: ReactionService,
    protected userService: UserService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit() {
    this.isSaving = false;
    this.activatedRoute.data.subscribe(({ reaction, reactionType }) => {
      if (reactionType === 'react') {
        this.parentReaction = reaction;
        reaction = new Reaction();
      }
      this.updateForm(reaction);
    });
  }

  updateForm(reaction: IReaction) {
    this.editForm.patchValue({
      id: reaction.id,
      title: reaction.title,
      content: reaction.content,
      type: reaction.type,
      typeLevel: reaction.typeLevel
    });
  }

  previousState() {
    window.history.back();
  }

  save() {
    this.isSaving = true;
    const reaction = this.createFromForm();
    if (reaction.id !== undefined) {
      this.subscribeToSaveResponse(this.reactionService.update(reaction));
    } else {
      this.subscribeToSaveResponse(this.reactionService.create(reaction));
    }
  }

  private createFromForm(): IReaction {
    return {
      ...new Reaction(),
      id: this.editForm.get(['id']).value,
      title: this.editForm.get(['title']).value,
      content: this.editForm.get(['content']).value,
      type: this.editForm.get(['type']).value,
      typeLevel: this.editForm.get(['typeLevel']).value,
      supportScore: this.editForm.get(['supportScore']).value,
      totalChildrenCount: 0,
      parentReaction: this.parentReaction
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IReaction>>) {
    result.subscribe(() => this.onSaveSuccess(), () => this.onSaveError());
  }

  protected onSaveSuccess() {
    this.isSaving = false;
    this.previousState();
  }

  protected onSaveError() {
    this.isSaving = false;
  }
  protected onError(errorMessage: string) {
    this.jhiAlertService.error(errorMessage, null, null);
  }

  trackUserById(index: number, item: IUser) {
    return item.id;
  }

  trackReactionById(index: number, item: IReaction) {
    return item.id;
  }
}
