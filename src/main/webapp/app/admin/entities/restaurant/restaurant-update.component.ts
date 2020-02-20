import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';

import { IRestaurant, Restaurant } from 'app/shared/model/restaurant.model';
import { RestaurantService } from './restaurant.service';
import { IUser } from 'app/core/user/user.model';
import { UserService } from 'app/core/user/user.service';

@Component({
  selector: 'jhi-restaurant-update',
  templateUrl: './restaurant-update.component.html'
})
export class RestaurantUpdateComponent implements OnInit {
  isSaving = false;

  users: IUser[] = [];

  editForm = this.fb.group({
    id: [],
    placeId: [],
    name: [],
    address: [],
    vicinity: [],
    url: [],
    website: [],
    googleRating: [],
    btRating: [],
    numberOfReviews: [],
    priceLevel: [],
    permanentlyClosed: [],
    userId: [null, Validators.required]
  });

  constructor(
    protected restaurantService: RestaurantService,
    protected userService: UserService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ restaurant }) => {
      this.updateForm(restaurant);

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

  updateForm(restaurant: IRestaurant): void {
    this.editForm.patchValue({
      id: restaurant.id,
      placeId: restaurant.placeId,
      name: restaurant.name,
      address: restaurant.address,
      vicinity: restaurant.vicinity,
      url: restaurant.url,
      website: restaurant.website,
      googleRating: restaurant.googleRating,
      btRating: restaurant.btRating,
      numberOfReviews: restaurant.numberOfReviews,
      priceLevel: restaurant.priceLevel,
      permanentlyClosed: restaurant.permanentlyClosed,
      userId: restaurant.userId
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const restaurant = this.createFromForm();
    if (restaurant.id !== undefined) {
      this.subscribeToSaveResponse(this.restaurantService.update(restaurant));
    } else {
      this.subscribeToSaveResponse(this.restaurantService.create(restaurant));
    }
  }

  private createFromForm(): IRestaurant {
    return {
      ...new Restaurant(),
      id: this.editForm.get(['id'])!.value,
      placeId: this.editForm.get(['placeId'])!.value,
      name: this.editForm.get(['name'])!.value,
      address: this.editForm.get(['address'])!.value,
      vicinity: this.editForm.get(['vicinity'])!.value,
      url: this.editForm.get(['url'])!.value,
      website: this.editForm.get(['website'])!.value,
      googleRating: this.editForm.get(['googleRating'])!.value,
      btRating: this.editForm.get(['btRating'])!.value,
      numberOfReviews: this.editForm.get(['numberOfReviews'])!.value,
      priceLevel: this.editForm.get(['priceLevel'])!.value,
      permanentlyClosed: this.editForm.get(['permanentlyClosed'])!.value,
      userId: this.editForm.get(['userId'])!.value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IRestaurant>>): void {
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

  trackById(index: number, item: IUser): any {
    return item.id;
  }
}
