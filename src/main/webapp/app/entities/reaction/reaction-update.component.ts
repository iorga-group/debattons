import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { JhiAlertService } from 'ng-jhipster';
import { IReaction } from 'app/shared/model/reaction.model';
import { ReactionService } from './reaction.service';
import { IUser, UserService } from 'app/core';

@Component({
    selector: 'dbt-reaction-update',
    templateUrl: './reaction-update.component.html'
})
export class ReactionUpdateComponent implements OnInit {
    reaction: IReaction;
    isSaving: boolean;

    users: IUser[];

    reactions: IReaction[];

    constructor(
        protected jhiAlertService: JhiAlertService,
        protected reactionService: ReactionService,
        protected userService: UserService,
        protected activatedRoute: ActivatedRoute
    ) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ reaction }) => {
            this.reaction = reaction;
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

    previousState() {
        window.history.back();
    }

    save() {
        this.isSaving = true;
        if (this.reaction.id !== undefined) {
            this.subscribeToSaveResponse(this.reactionService.update(this.reaction));
        } else {
            this.subscribeToSaveResponse(this.reactionService.create(this.reaction));
        }
    }

    protected subscribeToSaveResponse(result: Observable<HttpResponse<IReaction>>) {
        result.subscribe((res: HttpResponse<IReaction>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
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
