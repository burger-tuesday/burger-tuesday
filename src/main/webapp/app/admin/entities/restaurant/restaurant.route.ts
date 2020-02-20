import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Routes, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { flatMap } from 'rxjs/operators';

import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { IRestaurant, Restaurant } from 'app/shared/model/restaurant.model';
import { RestaurantService } from './restaurant.service';
import { RestaurantComponent } from './restaurant.component';
import { RestaurantDetailComponent } from './restaurant-detail.component';
import { RestaurantUpdateComponent } from './restaurant-update.component';

@Injectable({ providedIn: 'root' })
export class RestaurantResolve implements Resolve<IRestaurant> {
  constructor(private service: RestaurantService, private router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IRestaurant> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        flatMap((restaurant: HttpResponse<Restaurant>) => {
          if (restaurant.body) {
            return of(restaurant.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Restaurant());
  }
}

export const restaurantRoute: Routes = [
  {
    path: '',
    component: RestaurantComponent,
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'burgertuesdayApp.restaurant.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/view',
    component: RestaurantDetailComponent,
    resolve: {
      restaurant: RestaurantResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'burgertuesdayApp.restaurant.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'new',
    component: RestaurantUpdateComponent,
    resolve: {
      restaurant: RestaurantResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'burgertuesdayApp.restaurant.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/edit',
    component: RestaurantUpdateComponent,
    resolve: {
      restaurant: RestaurantResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'burgertuesdayApp.restaurant.home.title'
    },
    canActivate: [UserRouteAccessService]
  }
];
