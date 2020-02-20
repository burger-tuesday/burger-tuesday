import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { of } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';

import { BurgertuesdayTestModule } from '../../../../test.module';
import { VisitDeleteDialogComponent } from 'app/admin/entities/visit/visit-delete-dialog.component';
import { VisitService } from 'app/admin/entities/visit/visit.service';

describe('Component Tests', () => {
  describe('Visit Management Delete Component', () => {
    let comp: VisitDeleteDialogComponent;
    let fixture: ComponentFixture<VisitDeleteDialogComponent>;
    let service: VisitService;
    let mockEventManager: any;
    let mockActiveModal: any;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [BurgertuesdayTestModule],
        declarations: [VisitDeleteDialogComponent]
      })
        .overrideTemplate(VisitDeleteDialogComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(VisitDeleteDialogComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(VisitService);
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
