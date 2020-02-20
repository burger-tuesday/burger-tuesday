import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IRestaurant } from 'app/shared/model/restaurant.model';
import { RestaurantService } from './restaurant.service';

@Component({
  templateUrl: './restaurant-delete-dialog.component.html'
})
export class RestaurantDeleteDialogComponent {
  restaurant?: IRestaurant;

  constructor(
    protected restaurantService: RestaurantService,
    public activeModal: NgbActiveModal,
    protected eventManager: JhiEventManager
  ) {}

  clear(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.restaurantService.delete(id).subscribe(() => {
      this.eventManager.broadcast('restaurantListModification');
      this.activeModal.close();
    });
  }
}
