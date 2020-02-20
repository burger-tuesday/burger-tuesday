import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { BurgertuesdaySharedModule } from 'app/shared/shared.module';
import { RestaurantComponent } from './restaurant.component';
import { RestaurantDetailComponent } from './restaurant-detail.component';
import { restaurantRoute } from './restaurant.route';

@NgModule({
  imports: [BurgertuesdaySharedModule, RouterModule.forChild(restaurantRoute)],
  declarations: [RestaurantComponent, RestaurantDetailComponent],
  entryComponents: []
})
export class BurgertuesdayRestaurantModule {}
