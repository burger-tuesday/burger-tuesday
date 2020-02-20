import { NgModule } from '@angular/core';
import { BurgertuesdaySharedLibsModule } from './shared-libs.module';
import { FindLanguageFromKeyPipe } from './language/find-language-from-key.pipe';
import { AlertComponent } from './alert/alert.component';
import { AlertErrorComponent } from './alert/alert-error.component';
import { HasAnyAuthorityDirective } from './auth/has-any-authority.directive';
import { LoadingIndicatorComponent } from 'app/shared/loading-indicator/loading-indicator.component';

@NgModule({
  imports: [BurgertuesdaySharedLibsModule],
  declarations: [FindLanguageFromKeyPipe, AlertComponent, AlertErrorComponent, HasAnyAuthorityDirective, LoadingIndicatorComponent],
  exports: [
    BurgertuesdaySharedLibsModule,
    FindLanguageFromKeyPipe,
    AlertComponent,
    AlertErrorComponent,
    HasAnyAuthorityDirective,
    LoadingIndicatorComponent
  ]
})
export class BurgertuesdaySharedModule {}
