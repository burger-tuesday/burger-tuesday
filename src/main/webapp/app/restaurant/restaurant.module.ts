import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { BurgertuesdaySharedModule } from 'app/shared/shared.module';
import { RestaurantComponent } from './restaurant.component';
import { RestaurantDetailComponent } from './restaurant-detail.component';
import { restaurantRoute } from './restaurant.route';

const ENTITY_STATES = [...restaurantRoute];

@NgModule({
  imports: [BurgertuesdaySharedModule, RouterModule.forChild(ENTITY_STATES)],
  declarations: [
    RestaurantComponent,
    RestaurantDetailComponent,
  ],
})
export class BurgertuesdayRestaurantModule {}
