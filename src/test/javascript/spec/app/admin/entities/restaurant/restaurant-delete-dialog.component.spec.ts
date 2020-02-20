import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { of } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';

import { BurgertuesdayTestModule } from '../../../../test.module';
import { RestaurantDeleteDialogComponent } from 'app/admin/entities/restaurant/restaurant-delete-dialog.component';
import { RestaurantService } from 'app/admin/entities/restaurant/restaurant.service';

describe('Component Tests', () => {
  describe('Restaurant Management Delete Component', () => {
    let comp: RestaurantDeleteDialogComponent;
    let fixture: ComponentFixture<RestaurantDeleteDialogComponent>;
    let service: RestaurantService;
    let mockEventManager: any;
    let mockActiveModal: any;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [BurgertuesdayTestModule],
        declarations: [RestaurantDeleteDialogComponent]
      })
        .overrideTemplate(RestaurantDeleteDialogComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(RestaurantDeleteDialogComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(RestaurantService);
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
