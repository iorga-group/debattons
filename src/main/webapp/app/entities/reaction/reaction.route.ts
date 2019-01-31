import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiPaginationUtil, JhiResolvePagingParams } from 'ng-jhipster';
import { UserRouteAccessService } from 'app/core';
import { Observable, of } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { Reaction } from 'app/shared/model/reaction.model';
import { ReactionService } from './reaction.service';
import { ReactionComponent } from './reaction.component';
import { ReactionDetailComponent } from './reaction-detail.component';
import { ReactionUpdateComponent } from './reaction-update.component';
import { ReactionDeletePopupComponent } from './reaction-delete-dialog.component';
import { IReaction } from 'app/shared/model/reaction.model';

@Injectable({ providedIn: 'root' })
export class ReactionResolve implements Resolve<IReaction> {
    constructor(private service: ReactionService) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<IReaction> {
        const id = route.params['id'] ? route.params['id'] : null;
        if (id) {
            return this.service.find(id).pipe(
                filter((response: HttpResponse<Reaction>) => response.ok),
                map((reaction: HttpResponse<Reaction>) => reaction.body)
            );
        }
        return of(new Reaction());
    }
}

export const reactionRoute: Routes = [
    {
        path: '',
        component: ReactionComponent,
        resolve: {
            pagingParams: JhiResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            defaultSort: 'id,asc',
            pageTitle: 'debattonsApp.reaction.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: ':id/view',
        component: ReactionDetailComponent,
        resolve: {
            reaction: ReactionResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'debattonsApp.reaction.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'new',
        component: ReactionUpdateComponent,
        resolve: {
            reaction: ReactionResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'debattonsApp.reaction.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: ':id/edit',
        component: ReactionUpdateComponent,
        resolve: {
            reaction: ReactionResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'debattonsApp.reaction.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const reactionPopupRoute: Routes = [
    {
        path: ':id/delete',
        component: ReactionDeletePopupComponent,
        resolve: {
            reaction: ReactionResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'debattonsApp.reaction.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
