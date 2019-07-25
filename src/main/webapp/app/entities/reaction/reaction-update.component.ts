import { Component, OnInit } from '@angular/core';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { JhiAlertService, JhiDataUtils } from 'ng-jhipster';
import { IReaction, Reaction } from 'app/shared/model/reaction.model';
import { ReactionService } from './reaction.service';
import { IUser, UserService } from 'app/core';

@Component({
  selector: 'dbt-reaction-update',
  templateUrl: './reaction-update.component.html'
})
export class ReactionUpdateComponent implements OnInit {
  isSaving: boolean;

  users: IUser[];

  reactions: IReaction[];

  editForm = this.fb.group({
    id: [],
    title: [null, [Validators.maxLength(280)]],
    content: [],
    type: [null, [Validators.required]],
    typeLevel: [null, [Validators.min(1), Validators.max(120)]],
    supportScore: [],
    totalChildrenCount: [null, [Validators.required]],
    creator: [],
    parentReaction: []
  });

  constructor(
    protected dataUtils: JhiDataUtils,
    protected jhiAlertService: JhiAlertService,
    protected reactionService: ReactionService,
    protected userService: UserService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit() {
    this.isSaving = false;
    this.activatedRoute.data.subscribe(({ reaction }) => {
      this.updateForm(reaction);
    });
    this.userService
      .query()
      .pipe(
        filter((mayBeOk: HttpResponse<IUser[]>) => mayBeOk.ok),
        map((response: HttpResponse<IUser[]>) => response.body)
      )
      .subscribe((res: IUser[]) => (this.users = res), (res: HttpErrorResponse) => this.onError(res.message));
    this.reactionService
      .query()
      .pipe(
        filter((mayBeOk: HttpResponse<IReaction[]>) => mayBeOk.ok),
        map((response: HttpResponse<IReaction[]>) => response.body)
      )
      .subscribe((res: IReaction[]) => (this.reactions = res), (res: HttpErrorResponse) => this.onError(res.message));
  }

  updateForm(reaction: IReaction) {
    this.editForm.patchValue({
      id: reaction.id,
      title: reaction.title,
      content: reaction.content,
      type: reaction.type,
      typeLevel: reaction.typeLevel,
      supportScore: reaction.supportScore,
      totalChildrenCount: reaction.totalChildrenCount,
      creator: reaction.creator,
      parentReaction: reaction.parentReaction
    });
  }

  byteSize(field) {
    return this.dataUtils.byteSize(field);
  }

  openFile(contentType, field) {
    return this.dataUtils.openFile(contentType, field);
  }

  setFileData(event, field: string, isImage) {
    return new Promise((resolve, reject) => {
      if (event && event.target && event.target.files && event.target.files[0]) {
        const file = event.target.files[0];
        if (isImage && !/^image\//.test(file.type)) {
          reject(`File was expected to be an image but was found to be ${file.type}`);
        } else {
          const filedContentType: string = field + 'ContentType';
          this.dataUtils.toBase64(file, base64Data => {
            this.editForm.patchValue({
              [field]: base64Data,
              [filedContentType]: file.type
            });
          });
        }
      } else {
        reject(`Base64 data was not set as file could not be extracted from passed parameter: ${event}`);
      }
    }).then(
      () => console.log('blob added'), // sucess
      this.onError
    );
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
      totalChildrenCount: this.editForm.get(['totalChildrenCount']).value,
      creator: this.editForm.get(['creator']).value,
      parentReaction: this.editForm.get(['parentReaction']).value
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
