import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';

import { IReview, Review } from 'app/shared/model/review.model';
import { ReviewService } from './review.service';
import { IVisit } from 'app/shared/model/visit.model';
import { VisitService } from 'app/admin/entities/visit/visit.service';
import { IUser } from 'app/core/user/user.model';
import { UserService } from 'app/core/user/user.service';

type SelectableEntity = IVisit | IUser;

@Component({
  selector: 'jhi-review-update',
  templateUrl: './review-update.component.html'
})
export class ReviewUpdateComponent implements OnInit {
  isSaving = false;

  visits: IVisit[] = [];

  users: IUser[] = [];

  editForm = this.fb.group({
    id: [],
    review: [],
    taste: [],
    likeness: [],
    menuDiversity: [],
    service: [],
    priceLevel: [],
    recommended: [],
    visitId: [null, Validators.required],
    userId: [null, Validators.required]
  });

  constructor(
    protected reviewService: ReviewService,
    protected visitService: VisitService,
    protected userService: UserService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ review }) => {
      this.updateForm(review);

      this.visitService
        .query()
        .pipe(
          map((res: HttpResponse<IVisit[]>) => {
            return res.body ? res.body : [];
          })
        )
        .subscribe((resBody: IVisit[]) => (this.visits = resBody));

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

  updateForm(review: IReview): void {
    this.editForm.patchValue({
      id: review.id,
      review: review.review,
      taste: review.taste,
      likeness: review.likeness,
      menuDiversity: review.menuDiversity,
      service: review.service,
      priceLevel: review.priceLevel,
      recommended: review.recommended,
      visitId: review.visitId,
      userId: review.userId
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const review = this.createFromForm();
    if (review.id !== undefined) {
      this.subscribeToSaveResponse(this.reviewService.update(review));
    } else {
      this.subscribeToSaveResponse(this.reviewService.create(review));
    }
  }

  private createFromForm(): IReview {
    return {
      ...new Review(),
      id: this.editForm.get(['id'])!.value,
      review: this.editForm.get(['review'])!.value,
      taste: this.editForm.get(['taste'])!.value,
      likeness: this.editForm.get(['likeness'])!.value,
      menuDiversity: this.editForm.get(['menuDiversity'])!.value,
      service: this.editForm.get(['service'])!.value,
      priceLevel: this.editForm.get(['priceLevel'])!.value,
      recommended: this.editForm.get(['recommended'])!.value,
      visitId: this.editForm.get(['visitId'])!.value,
      userId: this.editForm.get(['userId'])!.value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IReview>>): void {
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
