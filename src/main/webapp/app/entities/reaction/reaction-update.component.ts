import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpErrorResponse, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { JhiAlertService } from 'ng-jhipster';
import { IReaction, Reaction } from 'app/shared/model/reaction.model';
import { ReactionService } from './reaction.service';
import { IUser } from 'app/core';

@Component({
    selector: 'dbt-reaction-update',
    templateUrl: './reaction-update.component.html'
})
export class ReactionUpdateComponent implements OnInit {
    reaction: IReaction;
    isSaving: boolean;

    reactions: IReaction[];

    constructor(
        protected jhiAlertService: JhiAlertService,
        protected reactionService: ReactionService,
        protected activatedRoute: ActivatedRoute
    ) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ reaction, reactionType }) => {
            if (reactionType === 'react') {
                this.reaction = new Reaction();
                this.reaction.parentReaction = reaction;
            } else {
                this.reaction = reaction;
            }
        });
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
