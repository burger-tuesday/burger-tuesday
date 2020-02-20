import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { BurgertuesdaySharedModule } from 'app/shared/shared.module';
import { ReviewComponent } from './review.component';
import { ReviewDetailComponent } from './review-detail.component';
import { ReviewUpdateComponent } from './review-update.component';
import { ReviewDeletePopupComponent, ReviewDeleteDialogComponent } from './review-delete-dialog.component';
import { reviewRoute, reviewPopupRoute } from './review.route';

const ENTITY_STATES = [...reviewRoute, ...reviewPopupRoute];

@NgModule({
  imports: [BurgertuesdaySharedModule, RouterModule.forChild(ENTITY_STATES)],
  declarations: [ReviewComponent, ReviewDetailComponent, ReviewUpdateComponent, ReviewDeleteDialogComponent, ReviewDeletePopupComponent],
  entryComponents: [ReviewDeleteDialogComponent]
})
export class BurgertuesdayReviewModule {}
