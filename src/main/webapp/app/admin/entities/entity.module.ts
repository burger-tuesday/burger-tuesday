import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'restaurant',
        loadChildren: () => import('./restaurant/restaurant.module').then(m => m.BurgertuesdayRestaurantModule)
      },
      {
        path: 'visit',
        loadChildren: () => import('./visit/visit.module').then(m => m.BurgertuesdayVisitModule)
      },
      {
        path: 'review',
        loadChildren: () => import('./review/review.module').then(m => m.BurgertuesdayReviewModule)
      }
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ])
  ]
})
export class EntityModule {}
