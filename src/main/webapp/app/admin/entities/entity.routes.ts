import {Routes} from '@angular/router';

export const entityRoutes: Routes = [
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
];
