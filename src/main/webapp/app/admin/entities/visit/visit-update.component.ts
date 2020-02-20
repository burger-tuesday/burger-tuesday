import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as moment from 'moment';

import { IVisit, Visit } from 'app/shared/model/visit.model';
import { VisitService } from './visit.service';
import { IRestaurant } from 'app/shared/model/restaurant.model';
import { RestaurantService } from 'app/admin/entities/restaurant/restaurant.service';
import { IUser } from 'app/core/user/user.model';
import { UserService } from 'app/core/user/user.service';

type SelectableEntity = IRestaurant | IUser;

@Component({
  selector: 'jhi-visit-update',
  templateUrl: './visit-update.component.html'
})
export class VisitUpdateComponent implements OnInit {
  isSaving = false;

  restaurants: IRestaurant[] = [];

  users: IUser[] = [];
  dateDp: any;

  editForm = this.fb.group({
    id: [],
    date: [],
    sponsored: [],
    restaurantId: [null, Validators.required],
    userId: [null, Validators.required]
  });

  constructor(
    protected visitService: VisitService,
    protected restaurantService: RestaurantService,
    protected userService: UserService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ visit }) => {
      this.updateForm(visit);

      this.restaurantService
        .query()
        .pipe(
          map((res: HttpResponse<IRestaurant[]>) => {
            return res.body ? res.body : [];
          })
        )
        .subscribe((resBody: IRestaurant[]) => (this.restaurants = resBody));

      this.userService
        .query()
        .pipe(
          map((res: HttpResponse<IUser[]>) => {
            return res.body ? res.body : [];
          })
        )
        .subscribe((resBody: IUser[]) => (this.users = resBody));
    });
  }

  updateForm(visit: IVisit): void {
    this.editForm.patchValue({
      id: visit.id,
      date: visit.date,
      sponsored: visit.sponsored,
      restaurantId: visit.restaurantId,
      userId: visit.userId
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const visit = this.createFromForm();
    if (visit.id !== undefined) {
      this.subscribeToSaveResponse(this.visitService.update(visit));
    } else {
      this.subscribeToSaveResponse(this.visitService.create(visit));
    }
  }

  private createFromForm(): IVisit {
    return {
      ...new Visit(),
      id: this.editForm.get(['id'])!.value,
      date: this.editForm.get(['date'])!.value,
      sponsored: this.editForm.get(['sponsored'])!.value,
      restaurantId: this.editForm.get(['restaurantId'])!.value,
      userId: this.editForm.get(['userId'])!.value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IVisit>>): void {
    result.subscribe(
      () => this.onSaveSuccess(),
      () => this.onSaveError()
    );
  }

  protected onSaveSuccess(): void {
    this.isSaving = false;
    this.previousState();
  }

  protected onSaveError(): void {
    this.isSaving = false;
  }

  trackById(index: number, item: SelectableEntity): any {
    return item.id;
  }
}
