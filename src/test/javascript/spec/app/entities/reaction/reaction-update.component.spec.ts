/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { Observable, of } from 'rxjs';

import { DebattonsTestModule } from '../../../test.module';
import { ReactionUpdateComponent } from 'app/entities/reaction/reaction-update.component';
import { ReactionService } from 'app/entities/reaction/reaction.service';
import { Reaction } from 'app/shared/model/reaction.model';

describe('Component Tests', () => {
  describe('Reaction Management Update Component', () => {
    let comp: ReactionUpdateComponent;
    let fixture: ComponentFixture<ReactionUpdateComponent>;
    let service: ReactionService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [DebattonsTestModule],
        declarations: [ReactionUpdateComponent],
        providers: [FormBuilder]
      })
        .overrideTemplate(ReactionUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(ReactionUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(ReactionService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new Reaction(123);
        spyOn(service, 'update').and.returnValue(of(new HttpResponse({ body: entity })));
        comp.updateForm(entity);
        // WHEN
        comp.save();
        tick(); // simulate async

        // THEN
        expect(service.update).toHaveBeenCalledWith(entity);
        expect(comp.isSaving).toEqual(false);
      }));

      it('Should call create service on save for new entity', fakeAsync(() => {
        // GIVEN
        const entity = new Reaction();
        spyOn(service, 'create').and.returnValue(of(new HttpResponse({ body: entity })));
        comp.updateForm(entity);
        // WHEN
        comp.save();
        tick(); // simulate async

        // THEN
        expect(service.create).toHaveBeenCalledWith(entity);
        expect(comp.isSaving).toEqual(false);
      }));
    });
  });
});
