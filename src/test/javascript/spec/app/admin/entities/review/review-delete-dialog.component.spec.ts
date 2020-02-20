import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { of } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';

import { BurgertuesdayTestModule } from '../../../../test.module';
import { ReviewDeleteDialogComponent } from 'app/admin/entities/review/review-delete-dialog.component';
import { ReviewService } from 'app/admin/entities/review/review.service';

describe('Component Tests', () => {
  describe('Review Management Delete Component', () => {
    let comp: ReviewDeleteDialogComponent;
    let fixture: ComponentFixture<ReviewDeleteDialogComponent>;
    let service: ReviewService;
    let mockEventManager: any;
    let mockActiveModal: any;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [BurgertuesdayTestModule],
        declarations: [ReviewDeleteDialogComponent]
      })
        .overrideTemplate(ReviewDeleteDialogComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(ReviewDeleteDialogComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(ReviewService);
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
