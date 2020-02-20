import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IReview } from 'app/shared/model/review.model';
import { ReviewService } from './review.service';

@Component({
  templateUrl: './review-delete-dialog.component.html'
})
export class ReviewDeleteDialogComponent {
  review?: IReview;

  constructor(protected reviewService: ReviewService, public activeModal: NgbActiveModal, protected eventManager: JhiEventManager) {}

  clear(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.reviewService.delete(id).subscribe(() => {
      this.eventManager.broadcast('reviewListModification');
      this.activeModal.close();
    });
  }
}
