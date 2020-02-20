import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IReview } from 'app/shared/model/review.model';

@Component({
  selector: 'jhi-review-detail',
  templateUrl: './review-detail.component.html'
})
export class ReviewDetailComponent implements OnInit {
  review: IReview;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ review }) => {
      this.review = review;
    });
  }

  previousState() {
    window.history.back();
  }
}
