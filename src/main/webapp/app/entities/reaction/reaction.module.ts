import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';
import { JhiLanguageService } from 'ng-jhipster';
import { JhiLanguageHelper } from 'app/core';

import { DebattonsSharedModule } from 'app/shared';
import {
    ReactionComponent,
    ReactionDetailComponent,
    ReactionUpdateComponent,
    ReactionDeletePopupComponent,
    ReactionDeleteDialogComponent,
    reactionRoute,
    reactionPopupRoute
} from './';

const ENTITY_STATES = [...reactionRoute, ...reactionPopupRoute];

@NgModule({
    imports: [DebattonsSharedModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [
        ReactionComponent,
        ReactionDetailComponent,
        ReactionUpdateComponent,
        ReactionDeleteDialogComponent,
        ReactionDeletePopupComponent
    ],
    entryComponents: [ReactionComponent, ReactionUpdateComponent, ReactionDeleteDialogComponent, ReactionDeletePopupComponent],
    providers: [{ provide: JhiLanguageService, useClass: JhiLanguageService }],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class DebattonsReactionModule {
    constructor(private languageService: JhiLanguageService, private languageHelper: JhiLanguageHelper) {
        this.languageHelper.language.subscribe((languageKey: string) => {
            if (languageKey !== undefined) {
                this.languageService.changeLanguage(languageKey);
            }
        });
    }
}
