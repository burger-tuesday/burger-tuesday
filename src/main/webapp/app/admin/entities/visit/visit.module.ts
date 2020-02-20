import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { BurgertuesdaySharedModule } from 'app/shared/shared.module';
import { VisitComponent } from './visit.component';
import { VisitDetailComponent } from './visit-detail.component';
import { VisitUpdateComponent } from './visit-update.component';
import { VisitDeletePopupComponent, VisitDeleteDialogComponent } from './visit-delete-dialog.component';
import { visitRoute, visitPopupRoute } from './visit.route';

const ENTITY_STATES = [...visitRoute, ...visitPopupRoute];

@NgModule({
  imports: [BurgertuesdaySharedModule, RouterModule.forChild(ENTITY_STATES)],
  declarations: [VisitComponent, VisitDetailComponent, VisitUpdateComponent, VisitDeleteDialogComponent, VisitDeletePopupComponent],
  entryComponents: [VisitDeleteDialogComponent]
})
export class BurgertuesdayVisitModule {}
