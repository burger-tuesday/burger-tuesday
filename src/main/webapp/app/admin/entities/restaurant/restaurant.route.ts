import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { Observable, of } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { Restaurant } from 'app/shared/model/restaurant.model';
import { RestaurantService } from './restaurant.service';
import { RestaurantComponent } from './restaurant.component';
import { RestaurantDetailComponent } from './restaurant-detail.component';
import { RestaurantUpdateComponent } from './restaurant-update.component';
import { RestaurantDeletePopupComponent } from './restaurant-delete-dialog.component';
import { IRestaurant } from 'app/shared/model/restaurant.model';

@Injectable({ providedIn: 'root' })
export class RestaurantResolve implements Resolve<IRestaurant> {
  constructor(private service: RestaurantService) {}

  resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<IRestaurant> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        filter((response: HttpResponse<Restaurant>) => response.ok),
        map((restaurant: HttpResponse<Restaurant>) => restaurant.body)
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
      authorities: ['ROLE_ADMIN'],
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
      authorities: ['ROLE_ADMIN'],
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
      authorities: ['ROLE_ADMIN'],
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
      authorities: ['ROLE_ADMIN'],
      pageTitle: 'burgertuesdayApp.restaurant.home.title'
    },
    canActivate: [UserRouteAccessService]
  }
];

export const restaurantPopupRoute: Routes = [
  {
    path: ':id/delete',
    component: RestaurantDeletePopupComponent,
    resolve: {
      restaurant: RestaurantResolve
    },
    data: {
      authorities: ['ROLE_ADMIN'],
      pageTitle: 'burgertuesdayApp.restaurant.home.title'
    },
    canActivate: [UserRouteAccessService],
    outlet: 'popup'
  }
];
