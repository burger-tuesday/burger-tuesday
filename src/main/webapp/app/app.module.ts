import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import './vendor';
import { BurgertuesdaySharedModule } from 'app/shared/shared.module';
import { BurgertuesdayCoreModule } from 'app/core/core.module';
import { BurgertuesdayAppRoutingModule } from './app-routing.module';
import { BurgertuesdayHomeModule } from './home/home.module';
import { BurgertuesdayRestaurantModule } from 'app/restaurant/restaurant.module';
// jhipster-needle-angular-add-module-import JHipster will add new module here
import { MainComponent } from './layouts/main/main.component';
import { NavbarComponent } from './layouts/navbar/navbar.component';
import { FooterComponent } from './layouts/footer/footer.component';
import { PageRibbonComponent } from './layouts/profiles/page-ribbon.component';
import { ActiveMenuDirective } from './layouts/navbar/active-menu.directive';
import { ErrorComponent } from './layouts/error/error.component';

@NgModule({
  imports: [
    BrowserModule,
    BurgertuesdaySharedModule,
    BurgertuesdayCoreModule,
    BurgertuesdayHomeModule,
    // jhipster-needle-angular-add-module JHipster will add new module here
    BurgertuesdayRestaurantModule,
    BurgertuesdayAppRoutingModule
  ],
  declarations: [MainComponent, NavbarComponent, ErrorComponent, PageRibbonComponent, ActiveMenuDirective, FooterComponent],
  bootstrap: [MainComponent]
})
export class BurgertuesdayAppModule {}
