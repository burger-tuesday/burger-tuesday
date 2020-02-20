import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Routes, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { flatMap } from 'rxjs/operators';

import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { IReview, Review } from 'app/shared/model/review.model';
import { ReviewService } from './review.service';
import { ReviewComponent } from './review.component';
import { ReviewDetailComponent } from './review-detail.component';
import { ReviewUpdateComponent } from './review-update.component';

@Injectable({ providedIn: 'root' })
export class ReviewResolve implements Resolve<IReview> {
  constructor(private service: ReviewService, private router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IReview> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        flatMap((review: HttpResponse<Review>) => {
          if (review.body) {
            return of(review.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Review());
  }
}

export const reviewRoute: Routes = [
  {
    path: '',
    component: ReviewComponent,
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'burgertuesdayApp.review.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/view',
    component: ReviewDetailComponent,
    resolve: {
      review: ReviewResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'burgertuesdayApp.review.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'new',
    component: ReviewUpdateComponent,
    resolve: {
      review: ReviewResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'burgertuesdayApp.review.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/edit',
    component: ReviewUpdateComponent,
    resolve: {
      review: ReviewResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'burgertuesdayApp.review.home.title'
    },
    canActivate: [UserRouteAccessService]
  }
];
