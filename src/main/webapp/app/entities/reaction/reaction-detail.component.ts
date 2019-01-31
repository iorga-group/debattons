import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IReaction } from 'app/shared/model/reaction.model';

@Component({
    selector: 'jhi-reaction-detail',
    templateUrl: './reaction-detail.component.html'
})
export class ReactionDetailComponent implements OnInit {
    reaction: IReaction;

    constructor(protected activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ reaction }) => {
            this.reaction = reaction;
        });
    }

    previousState() {
        window.history.back();
    }
}
