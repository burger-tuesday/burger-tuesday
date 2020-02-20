import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IReview } from 'app/shared/model/review.model';
import { ReviewService } from './review.service';

@Component({
  selector: 'jhi-review-delete-dialog',
  templateUrl: './review-delete-dialog.component.html'
})
export class ReviewDeleteDialogComponent {
  review: IReview;

  constructor(protected reviewService: ReviewService, public activeModal: NgbActiveModal, protected eventManager: JhiEventManager) {}

  clear() {
    this.activeModal.dismiss('cancel');
  }

  confirmDelete(id: number) {
    this.reviewService.delete(id).subscribe(response => {
      this.eventManager.broadcast({
        name: 'reviewListModification',
        content: 'Deleted an review'
      });
      this.activeModal.dismiss(true);
    });
  }
}

@Component({
  selector: 'jhi-review-delete-popup',
  template: ''
})
export class ReviewDeletePopupComponent implements OnInit, OnDestroy {
  protected ngbModalRef: NgbModalRef;

  constructor(protected activatedRoute: ActivatedRoute, protected router: Router, protected modalService: NgbModal) {}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ review }) => {
      setTimeout(() => {
        this.ngbModalRef = this.modalService.open(ReviewDeleteDialogComponent as Component, { size: 'lg', backdrop: 'static' });
        this.ngbModalRef.componentInstance.review = review;
        this.ngbModalRef.result.then(
          result => {
            this.router.navigate(['/review', { outlets: { popup: null } }]);
            this.ngbModalRef = null;
          },
          reason => {
            this.router.navigate(['/review', { outlets: { popup: null } }]);
            this.ngbModalRef = null;
          }
        );
      }, 0);
    });
  }

  ngOnDestroy() {
    this.ngbModalRef = null;
  }
}
