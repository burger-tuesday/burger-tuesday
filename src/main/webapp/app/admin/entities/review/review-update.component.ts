import { Component, OnInit } from '@angular/core';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { JhiAlertService } from 'ng-jhipster';
import { IReview, Review } from 'app/shared/model/review.model';
import { ReviewService } from './review.service';
import { IVisit } from 'app/shared/model/visit.model';
import { VisitService } from '../visit/visit.service';
import { IUser } from 'app/core/user/user.model';
import { UserService } from 'app/core/user/user.service';

@Component({
  selector: 'jhi-review-update',
  templateUrl: './review-update.component.html'
})
export class ReviewUpdateComponent implements OnInit {
  isSaving: boolean;

  visits: IVisit[];

  users: IUser[];

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
    protected jhiAlertService: JhiAlertService,
    protected reviewService: ReviewService,
    protected visitService: VisitService,
    protected userService: UserService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit() {
    this.isSaving = false;
    this.activatedRoute.data.subscribe(({ review }) => {
      this.updateForm(review);
    });
    this.visitService
      .query()
      .pipe(
        filter((mayBeOk: HttpResponse<IVisit[]>) => mayBeOk.ok),
        map((response: HttpResponse<IVisit[]>) => response.body)
      )
      .subscribe((res: IVisit[]) => (this.visits = res), (res: HttpErrorResponse) => this.onError(res.message));
    this.userService
      .query()
      .pipe(
        filter((mayBeOk: HttpResponse<IUser[]>) => mayBeOk.ok),
        map((response: HttpResponse<IUser[]>) => response.body)
      )
      .subscribe((res: IUser[]) => (this.users = res), (res: HttpErrorResponse) => this.onError(res.message));
  }

  updateForm(review: IReview) {
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

  previousState() {
    window.history.back();
  }

  save() {
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
      id: this.editForm.get(['id']).value,
      review: this.editForm.get(['review']).value,
      taste: this.editForm.get(['taste']).value,
      likeness: this.editForm.get(['likeness']).value,
      menuDiversity: this.editForm.get(['menuDiversity']).value,
      service: this.editForm.get(['service']).value,
      priceLevel: this.editForm.get(['priceLevel']).value,
      recommended: this.editForm.get(['recommended']).value,
      visitId: this.editForm.get(['visitId']).value,
      userId: this.editForm.get(['userId']).value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IReview>>) {
    result.subscribe(() => this.onSaveSuccess(), () => this.onSaveError());
  }

  protected onSaveSuccess() {
    this.isSaving = false;
    this.previousState();
  }

  protected onSaveError() {
    this.isSaving = false;
  }
  protected onError(errorMessage: string) {
    this.jhiAlertService.error(errorMessage, null, null);
  }

  trackVisitById(index: number, item: IVisit) {
    return item.id;
  }

  trackUserById(index: number, item: IUser) {
    return item.id;
  }
}
