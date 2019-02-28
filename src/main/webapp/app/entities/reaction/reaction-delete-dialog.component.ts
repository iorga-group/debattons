import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IReaction } from 'app/shared/model/reaction.model';
import { ReactionService } from './reaction.service';

@Component({
    selector: 'dbt-reaction-delete-dialog',
    templateUrl: './reaction-delete-dialog.component.html'
})
export class ReactionDeleteDialogComponent {
    reaction: IReaction;

    constructor(protected reactionService: ReactionService, public activeModal: NgbActiveModal, protected eventManager: JhiEventManager) {}

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.reactionService.delete(id).subscribe(response => {
            this.eventManager.broadcast({
                name: 'reactionListModification',
                content: 'Deleted an reaction'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'dbt-reaction-delete-popup',
    template: ''
})
export class ReactionDeletePopupComponent implements OnInit, OnDestroy {
    protected ngbModalRef: NgbModalRef;

    constructor(protected activatedRoute: ActivatedRoute, protected router: Router, protected modalService: NgbModal) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ reaction }) => {
            setTimeout(() => {
                this.ngbModalRef = this.modalService.open(ReactionDeleteDialogComponent as Component, { size: 'lg', backdrop: 'static' });
                this.ngbModalRef.componentInstance.reaction = reaction;
                this.ngbModalRef.result.then(
                    result => {
                        this.router.navigate(['/reaction', { outlets: { popup: null } }]);
                        this.ngbModalRef = null;
                    },
                    reason => {
                        this.router.navigate(['/reaction', { outlets: { popup: null } }]);
                        this.ngbModalRef = null;
                    }
                );
            }, 0);
        });
    }

    ngOnDestroy() {
        this.ngbModalRef = null;
    }
}
