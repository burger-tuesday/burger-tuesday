import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IRestaurant } from 'app/shared/model/restaurant.model';
import { RestaurantService } from './restaurant.service';

@Component({
  selector: 'jhi-restaurant-delete-dialog',
  templateUrl: './restaurant-delete-dialog.component.html'
})
export class RestaurantDeleteDialogComponent {
  restaurant: IRestaurant;

  constructor(
    protected restaurantService: RestaurantService,
    public activeModal: NgbActiveModal,
    protected eventManager: JhiEventManager
  ) {}

  clear() {
    this.activeModal.dismiss('cancel');
  }

  confirmDelete(id: number) {
    this.restaurantService.delete(id).subscribe(response => {
      this.eventManager.broadcast({
        name: 'restaurantListModification',
        content: 'Deleted an restaurant'
      });
      this.activeModal.dismiss(true);
    });
  }
}

@Component({
  selector: 'jhi-restaurant-delete-popup',
  template: ''
})
export class RestaurantDeletePopupComponent implements OnInit, OnDestroy {
  protected ngbModalRef: NgbModalRef;

  constructor(protected activatedRoute: ActivatedRoute, protected router: Router, protected modalService: NgbModal) {}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ restaurant }) => {
      setTimeout(() => {
        this.ngbModalRef = this.modalService.open(RestaurantDeleteDialogComponent as Component, { size: 'lg', backdrop: 'static' });
        this.ngbModalRef.componentInstance.restaurant = restaurant;
        this.ngbModalRef.result.then(
          result => {
            this.router.navigate(['/restaurant', { outlets: { popup: null } }]);
            this.ngbModalRef = null;
          },
          reason => {
            this.router.navigate(['/restaurant', { outlets: { popup: null } }]);
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
