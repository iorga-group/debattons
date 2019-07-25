/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Observable, of } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';

import { DebattonsTestModule } from '../../../test.module';
import { ReactionDeleteDialogComponent } from 'app/entities/reaction/reaction-delete-dialog.component';
import { ReactionService } from 'app/entities/reaction/reaction.service';

describe('Component Tests', () => {
  describe('Reaction Management Delete Component', () => {
    let comp: ReactionDeleteDialogComponent;
    let fixture: ComponentFixture<ReactionDeleteDialogComponent>;
    let service: ReactionService;
    let mockEventManager: any;
    let mockActiveModal: any;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [DebattonsTestModule],
        declarations: [ReactionDeleteDialogComponent]
      })
        .overrideTemplate(ReactionDeleteDialogComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(ReactionDeleteDialogComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(ReactionService);
      mockEventManager = fixture.debugElement.injector.get(JhiEventManager);
      mockActiveModal = fixture.debugElement.injector.get(NgbActiveModal);
    });

    describe('confirmDelete', () => {
      it('Should call delete service on confirmDelete', inject(
        [],
        fakeAsync(() => {
          // GIVEN
          spyOn(service, 'delete').and.returnValue(of({}));

          // WHEN
          comp.confirmDelete(123);
          tick();

          // THEN
          expect(service.delete).toHaveBeenCalledWith(123);
          expect(mockActiveModal.dismissSpy).toHaveBeenCalled();
          expect(mockEventManager.broadcastSpy).toHaveBeenCalled();
        })
      ));
    });
  });
});
