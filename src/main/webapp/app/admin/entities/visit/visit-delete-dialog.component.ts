import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IVisit } from 'app/shared/model/visit.model';
import { VisitService } from './visit.service';

@Component({
  selector: 'jhi-visit-delete-dialog',
  templateUrl: './visit-delete-dialog.component.html'
})
export class VisitDeleteDialogComponent {
  visit: IVisit;

  constructor(protected visitService: VisitService, public activeModal: NgbActiveModal, protected eventManager: JhiEventManager) {}

  clear() {
    this.activeModal.dismiss('cancel');
  }

  confirmDelete(id: number) {
    this.visitService.delete(id).subscribe(response => {
      this.eventManager.broadcast({
        name: 'visitListModification',
        content: 'Deleted an visit'
      });
      this.activeModal.dismiss(true);
    });
  }
}

@Component({
  selector: 'jhi-visit-delete-popup',
  template: ''
})
export class VisitDeletePopupComponent implements OnInit, OnDestroy {
  protected ngbModalRef: NgbModalRef;

  constructor(protected activatedRoute: ActivatedRoute, protected router: Router, protected modalService: NgbModal) {}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ visit }) => {
      setTimeout(() => {
        this.ngbModalRef = this.modalService.open(VisitDeleteDialogComponent as Component, { size: 'lg', backdrop: 'static' });
        this.ngbModalRef.componentInstance.visit = visit;
        this.ngbModalRef.result.then(
          result => {
            this.router.navigate(['/visit', { outlets: { popup: null } }]);
            this.ngbModalRef = null;
          },
          reason => {
            this.router.navigate(['/visit', { outlets: { popup: null } }]);
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
