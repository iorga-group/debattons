/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { DebattonsTestModule } from '../../../test.module';
import { ReactionDetailComponent } from 'app/entities/reaction/reaction-detail.component';
import { Reaction } from 'app/shared/model/reaction.model';
import { VisNetworkService } from 'ngx-vis';

describe('Component Tests', () => {
    describe('Reaction Management Detail Component', () => {
        let comp: ReactionDetailComponent;
        let fixture: ComponentFixture<ReactionDetailComponent>;
        const route = ({ data: of({ reaction: new Reaction(123) }) } as any) as ActivatedRoute;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [DebattonsTestModule],
                declarations: [ReactionDetailComponent],
                providers: [{ provide: ActivatedRoute, useValue: route }, VisNetworkService]
            })
                .overrideTemplate(ReactionDetailComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(ReactionDetailComponent);
            comp = fixture.componentInstance;
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(comp.reaction).toEqual(jasmine.objectContaining({ id: 123 }));
            });
        });
    });
});
