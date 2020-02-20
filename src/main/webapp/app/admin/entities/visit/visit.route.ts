import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { Observable, of } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { Visit } from 'app/shared/model/visit.model';
import { VisitService } from './visit.service';
import { VisitComponent } from './visit.component';
import { VisitDetailComponent } from './visit-detail.component';
import { VisitUpdateComponent } from './visit-update.component';
import { VisitDeletePopupComponent } from './visit-delete-dialog.component';
import { IVisit } from 'app/shared/model/visit.model';

@Injectable({ providedIn: 'root' })
export class VisitResolve implements Resolve<IVisit> {
  constructor(private service: VisitService) {}

  resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<IVisit> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        filter((response: HttpResponse<Visit>) => response.ok),
        map((visit: HttpResponse<Visit>) => visit.body)
      );
    }
    return of(new Visit());
  }
}

export const visitRoute: Routes = [
  {
    path: '',
    component: VisitComponent,
    data: {
      authorities: ['ROLE_ADMIN'],
      pageTitle: 'burgertuesdayApp.visit.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/view',
    component: VisitDetailComponent,
    resolve: {
      visit: VisitResolve
    },
    data: {
      authorities: ['ROLE_ADMIN'],
      pageTitle: 'burgertuesdayApp.visit.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'new',
    component: VisitUpdateComponent,
    resolve: {
      visit: VisitResolve
    },
    data: {
      authorities: ['ROLE_ADMIN'],
      pageTitle: 'burgertuesdayApp.visit.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/edit',
    component: VisitUpdateComponent,
    resolve: {
      visit: VisitResolve
    },
    data: {
      authorities: ['ROLE_ADMIN'],
      pageTitle: 'burgertuesdayApp.visit.home.title'
    },
    canActivate: [UserRouteAccessService]
  }
];

export const visitPopupRoute: Routes = [
  {
    path: ':id/delete',
    component: VisitDeletePopupComponent,
    resolve: {
      visit: VisitResolve
    },
    data: {
      authorities: ['ROLE_ADMIN'],
      pageTitle: 'burgertuesdayApp.visit.home.title'
    },
    canActivate: [UserRouteAccessService],
    outlet: 'popup'
  }
];
